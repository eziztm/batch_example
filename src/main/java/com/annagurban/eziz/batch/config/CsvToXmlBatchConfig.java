package com.annagurban.eziz.batch.config;

import com.annagurban.eziz.batch.models.FootballClub;
import com.annagurban.eziz.batch.processors.CsvToXmlProcessor;
import java.util.HashMap;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
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
public class CsvToXmlBatchConfig {

    String FILENAME_INPUT = "football-clubs.csv";
    String FILENAME_OUTPUT = "output-football-clubs.xml";

    // tag::readerwriterprocessor[]
    @Bean
    public ItemReader<FootballClub> csvToXmlReader() {
        FlatFileItemReader<FootballClub> reader = new FlatFileItemReader();
        reader.setResource(new FileSystemResource(FILENAME_INPUT));
        reader.setLineMapper(new DefaultLineMapper<FootballClub>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setDelimiter(";");
                        setIncludedFields(new int[]{0, 1, 2, 3});
                        setNames(new String[]{"id", "name", "city", "country"});
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<FootballClub>() {
                    {
                        setTargetType(FootballClub.class);
                    }
                });
            }
        });

        return reader;
    }

    @Bean
    public ItemProcessor csvToXmlProcessor() {
        return new CsvToXmlProcessor();
    }

    @Bean
    public ItemWriter<FootballClub> csvToXmlWriter() {
        StaxEventItemWriter<FootballClub> writer = new StaxEventItemWriter();
        writer.setResource(new FileSystemResource(FILENAME_OUTPUT));
        writer.setOverwriteOutput(true);
        writer.setRootTagName("clubs");

        XStreamMarshaller marshaller = new XStreamMarshaller();
        Map<String, Class> aliases = new HashMap();
        aliases.put("club", FootballClub.class);
        marshaller.setAliases(aliases);
        writer.setMarshaller(marshaller);

        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job csvToXmlJob(JobBuilderFactory jobs,
            @Qualifier("csvToXmlStep") Step step,
            @Qualifier("csvToXmlJobCompletionListener") JobExecutionListener listener
    ) {
        return jobs.get("csvToXmlJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step csvToXmlStep(StepBuilderFactory stepBuilderFactory,
            @Qualifier("csvToXmlReader") ItemReader<FootballClub> reader,
            @Qualifier("csvToXmlWriter") ItemWriter<FootballClub> writer,
            @Qualifier("csvToXmlProcessor") ItemProcessor<FootballClub, FootballClub> processor) {

        // Wait 10 seconds before retrying
        FixedBackOffPolicy policy = new FixedBackOffPolicy();
        policy.setBackOffPeriod(10000L);

        return stepBuilderFactory.get("csvToXmlStep")
                .<FootballClub, FootballClub>chunk(20)
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
