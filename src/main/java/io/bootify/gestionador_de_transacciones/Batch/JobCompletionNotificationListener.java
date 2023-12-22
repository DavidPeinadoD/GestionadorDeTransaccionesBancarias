package io.bootify.gestionador_de_transacciones.Batch;

import io.bootify.gestionador_de_transacciones.domain.Transaccion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log =
            LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Fin del job. Resultado para verificación:");

            List<Transaccion> results = jdbcTemplate.query("SELECT code, nombre FROM comunidad", new RowMapper<Transaccion>() {
            @Override
            public Transaccion mapRow(ResultSet rs, int row) throws
                    SQLException {
                return new Transaccion(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getTimestamp(5).toLocalDateTime());
            }
        });

        for (Transaccion transaccion : results) {
            log.info("[" + transaccion + "] en base.");
        }

    }
}
}