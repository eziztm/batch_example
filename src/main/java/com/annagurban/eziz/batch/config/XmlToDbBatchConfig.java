package com.annagurban.eziz.batch.config;

import com.annagurban.eziz.batch.models.User;
import com.annagurban.eziz.batch.processors.XmlToDbProcessor;
import com.annagurban.eziz.batch.writers.UserDbWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.retry.backoff.FixedBackOffPolicy;

@Configuration
@EnableBatchProcessing
@Import(ApplicationConfig.class)
public class XmlToDbBatchConfig {

    String FILENAME_INPUT = "users.xml";

    // tag::readerwriterprocessor[]
    @Bean
    public ItemReader<User> xmlToDbReader() {
        StaxEventItemReader<User> reader = new StaxEventItemReader<>();
        reader.setResource(new FileSystemResource(FILENAME_INPUT));
        reader.setFragmentRootElementName("user");

        XStreamMarshaller marshaller = new XStreamMarshaller();

        Map<String, Class> aliases = new HashMap();
        aliases.put("users", List.class);
        aliases.put("user", User.class);
        aliases.put("role", String.class);
        marshaller.setAliases(aliases);

        reader.setUnmarshaller(marshaller);
        //reader.setUnmarshaller(new XStreamMarshaller());

        return reader;
    }

    @Bean
    public ItemProcessor xmlToDbProcessor() {
        return new XmlToDbProcessor();
    }

    @Bean
    public ItemWriter<User> xmlToDbWriter() {
        return new UserDbWriter();
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job xmlToDbJob(JobBuilderFactory jobs,
            @Qualifier("xmlToDbStep") Step step,
            @Qualifier("xmlToDbJobCompletionListener") JobExecutionListener listener
    ) {
        return jobs.get("xmlToDbJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step xmlToDbStep(StepBuilderFactory stepBuilderFactory,
            @Qualifier("xmlToDbReader") ItemReader<User> reader,
            @Qualifier("xmlToDbWriter") ItemWriter<User> writer,
            @Qualifier("xmlToDbProcessor") ItemProcessor<User, User> processor) {

        // Wait 10 seconds before retrying
        FixedBackOffPolicy policy = new FixedBackOffPolicy();
        policy.setBackOffPeriod(10000L);

        return stepBuilderFactory.get("xmlToDbStep")
                .<User, User>chunk(20)
                .faultTolerant()
                .retry(TimeoutException.class)
                .retryLimit(5)
                .backOffPolicy(policy)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    // end::jobstep[]
}
