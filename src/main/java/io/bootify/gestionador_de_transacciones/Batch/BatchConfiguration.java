package io.bootify.gestionador_de_transacciones.Batch;

import io.bootify.gestionador_de_transacciones.domain.Transaccion;
import jakarta.annotation.PostConstruct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public DataSource dataSource;

    @Bean
    public FlatFileItemReader<Transaccion> reader() {
        FlatFileItemReader<Transaccion> reader = new
                FlatFileItemReader<Transaccion>();
        reader.setResource(new ClassPathResource("csv/DocumentoBaseDeDatos2.csv"));
        reader.setLineMapper(new DefaultLineMapper<Transaccion>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("id", "iBANBeneficiario", "iBANTransactor", "cantidad", "fechaTransaccion");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Transaccion>() {{
                setTargetType(Transaccion.class);
            }});
        }});
        return reader;
    }

    @Bean
    public TransaccionItemProcessor processor() {
        return new TransaccionItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Transaccion> writer() {
        JdbcBatchItemWriter<Transaccion> writer = new
                JdbcBatchItemWriter<Transaccion>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Transaccion>());
        writer.setSql("INSERT INTO transaccion (id, ibanbeneficiario, ibantransactor, cantidad, fecha_transaccion) VALUES (:id, :iBANBeneficiario, :iBANTransactor, :cantidad, :fechaTransaccion)");
        writer.setDataSource(dataSource);
        return writer;
    }

    /*@Bean
    public Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }*/
    @Bean(name = "transaccionesJob")
    public Job myJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("transaccionesJob", jobRepository)
                .start(step)
                .build();
    }

    /*@Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Transaccion, Transaccion>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }*/

    @Bean
    protected Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<Transaccion> reader,
                        ItemProcessor<Transaccion, Transaccion> processor, JdbcBatchItemWriter<Transaccion> writer) {
        return new StepBuilder("processLines", jobRepository).<Transaccion, Transaccion>chunk(2, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}