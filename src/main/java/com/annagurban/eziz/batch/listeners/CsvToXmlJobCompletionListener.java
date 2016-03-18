package com.annagurban.eziz.batch.listeners;

import com.annagurban.eziz.batch.models.FootballClub;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;

/**
 *
 * @author g016501
 */
@Component(value = "csvToXmlJobCompletionListener")
public class CsvToXmlJobCompletionListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(CsvToXmlJobCompletionListener.class);

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

            log.info("!!! JOB FINISHED! Time to verify the results");

            List<FootballClub> clubs;

            XStreamMarshaller marshaller = new XStreamMarshaller();
            Map<String, Class> aliases = new HashMap();
            aliases.put("clubs", List.class);
            aliases.put("club", FootballClub.class);
            marshaller.setAliases(aliases);
            try {
                clubs = (List<FootballClub>) marshaller.unmarshal(new StreamSource(new FileInputStream("output-football-clubs.xml")));
            } catch (IOException ex) {
                log.error("Error while reading {} for verification. {}", " output-football-clubs.xml", ex.getMessage());
                return;
            }
            
            log.info("Processed items: " + clubs.size());

        }
    }

}
