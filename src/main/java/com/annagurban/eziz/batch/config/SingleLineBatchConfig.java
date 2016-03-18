package com.annagurban.eziz.batch.config;

import com.annagurban.eziz.batch.processors.SingleLineProcessor;
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
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.retry.backoff.FixedBackOffPolicy;

@Configuration
@EnableBatchProcessing
@Import(ApplicationConfig.class)
public class SingleLineBatchConfig {

    String FILENAME_INPUT = "single-line.txt";
    String FILENAME_OUTPUT = "output-single-line.txt";

    // tag::readerwriterprocessor[]
    @Bean
    public ItemReader<String> singleLineReader() {
        FlatFileItemReader<String> reader = new FlatFileItemReader();
        reader.setResource(new FileSystemResource(FILENAME_INPUT));
        reader.setLineMapper(new PassThroughLineMapper());
        return reader;
    }

    @Bean
    public ItemProcessor singleLineProcessor() {
        return new SingleLineProcessor();
    }

    @Bean
    public ItemWriter<String> singleLineWriter() {
        FlatFileItemWriter<String> writer = new FlatFileItemWriter();
        writer.setResource(new FileSystemResource(FILENAME_OUTPUT));
        writer.setLineAggregator(new PassThroughLineAggregator());

        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job singleLineJob(JobBuilderFactory jobs,
            @Qualifier("singleLineStep") Step step,
            @Qualifier("singleLineJobCompletionListener") JobExecutionListener listener
    ) {
        return jobs.get("singleLineJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step)
                .end()
                .build();
    }

    @Bean
    public Step singleLineStep(StepBuilderFactory stepBuilderFactory,
            @Qualifier("singleLineReader") ItemReader<String> reader,
            @Qualifier("singleLineWriter") ItemWriter<String> writer,
            @Qualifier("singleLineProcessor") ItemProcessor<String, String> processor) {

        // Wait 10 seconds before retrying
        FixedBackOffPolicy policy = new FixedBackOffPolicy();
        policy.setBackOffPeriod(10000L);

        return stepBuilderFactory.get("singleLineStep")
                .<String, String>chunk(20)
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
