package io.bootify.gestionador_de_transacciones.config;

import io.bootify.gestionador_de_transacciones.domain.Transaccion;
import io.bootify.gestionador_de_transacciones.repos.TransaccionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DbInit {
/*
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("transaccionesJob")
    private Job transaccionesJob;

    @Autowired
    private ListableJobLocator listableJobLocator;

    @PostConstruct
    public void runJob() throws JobExecutionException {
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(transaccionesJob, params);
    }
*/
}