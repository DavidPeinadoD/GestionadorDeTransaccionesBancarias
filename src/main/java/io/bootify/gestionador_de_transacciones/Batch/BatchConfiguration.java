package io.bootify.gestionador_de_transacciones.Batch;

import io.bootify.gestionador_de_transacciones.domain.Transaccion;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    // tag::readerwriterprocessor[]
    @Bean
    public FlatFileItemReader<Transaccion> reader() {
        FlatFileItemReader<Transaccion> reader = new
                FlatFileItemReader<Transaccion>();
        reader.setResource(new ClassPathResource("csv/DocumentoBaseDeDatos"));
        reader.setLineMapper(new DefaultLineMapper<Transaccion>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("codigo", "nombre");
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
        writer.setItemSqlParameterSourceProvider(new
                BeanPropertyItemSqlParameterSourceProvider<Transaccion>());
        writer.setSql("INSERT INTO comunidad (id, ibanbeneficiario, ibantransactor, cantidad, fecha_transaccion) VALUES (:id, :iBANBeneficiario, :iBANTransactor, :cantidad, :fechaTransaccion)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Transaccion, Transaccion>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

}