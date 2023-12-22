package io.bootify.gestionador_de_transacciones.Batch;

import io.bootify.gestionador_de_transacciones.domain.Transaccion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class CsvToDatabaseJob {

    public static final Logger logger = LoggerFactory.getLogger(CsvToDatabaseJob.class);

    private static final String INSERT_QUERY = """
            insert into transaccion (id, iBANBeneficiario, iBANTransactor, cantidad, fechaTransaccion)
            values (:id, :iBANBeneficiario, :iBANTransactor, :cantidad, :fechaTransaccion)""";

    private final JobRepository jobRepository;
    @Value("csv/DocumentoBaseDeDatos.csv")
    private Resource inputFeed;

    public CsvToDatabaseJob(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Bean
    public Job insertIntoDbFromCsvJob(Step step1) {
        var name = "Transacciones Import Job";
        var builder = new JobBuilder(name, jobRepository);
        return builder.start(step1).listener(new JobCompletionNotificationListener()).build();
    }

    @Bean
    public Step step1(ItemReader<Transaccion> reader, ItemWriter<Transaccion> writer, ItemProcessor<Transaccion, Transaccion> processor, PlatformTransactionManager txManager) {
        var name = "INSERT CSV RECORDS To DB Step";
        var builder = new StepBuilder(name, jobRepository);
        return builder.<Transaccion, Transaccion>chunk(100).reader(reader).writer(writer).processor(processor).taskExecutor(taskExecutor()).transactionManager(txManager).build();
    }

    @Bean
    public FlatFileItemReader<Transaccion> csvFileReader(LineMapper<Transaccion> lineMapper) {
        var itemReader = new FlatFileItemReader<Transaccion>();
        itemReader.setLineMapper(lineMapper);
        itemReader.setResource(inputFeed);
        return itemReader;
    }

    @Bean
    public DefaultLineMapper<Transaccion> lineMapper(LineTokenizer tokenizer, FieldSetMapper<Transaccion> mapper) {
        var lineMapper = new DefaultLineMapper<Transaccion>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(mapper);
        return lineMapper;
    }

    @Bean
    public BeanWrapperFieldSetMapper<Transaccion> fieldSetMapper() {
        var fieldSetMapper = new BeanWrapperFieldSetMapper<Transaccion>();
        fieldSetMapper.setTargetType(Transaccion.class);
        return fieldSetMapper;
    }

    @Bean
    public DelimitedLineTokenizer tokenizer() {
        var tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("id", "iBANBeneficiario", "iBANTransactor", "cantidad", "fechaTransaccion");
        return tokenizer;
    }

    @Bean
    public JdbcBatchItemWriter<Transaccion> jdbcItemWriter(DataSource dataSource) {
        var provider = new BeanPropertyItemSqlParameterSourceProvider<Transaccion>();
        var itemWriter = new JdbcBatchItemWriter<Transaccion>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql(INSERT_QUERY);
        itemWriter.setItemSqlParameterSourceProvider(provider);
        return itemWriter;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    @Bean
    public ItemProcessor<Transaccion, Transaccion> processor() {
        return transaccion -> {
            // Realiza cualquier procesamiento necesario
            // Puedes realizar conversiones, validaciones, etc.
            return transaccion;
        };
    }
}
