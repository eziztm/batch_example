package com.annagurban.eziz.batch.listeners;

import com.annagurban.eziz.batch.models.User;
import java.sql.ResultSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author g016501
 */
@Component(value = "xmlToDbJobCompletionListener")
public class XmlToDbJobCompletionListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(XmlToDbJobCompletionListener.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

            log.info("!!! JOB FINISHED! Time to verify the results");

            List<User> results = jdbcTemplate.query("SELECT username FROM USERS", (ResultSet rs, int row) -> {
                User user = new User();
                user.setUsername(rs.getString(1));

                return user;
            });

            log.info("Successfully imported {}", results.size());

        }
    }

}
