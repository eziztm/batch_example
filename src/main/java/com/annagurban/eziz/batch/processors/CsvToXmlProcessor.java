package com.annagurban.eziz.batch.processors;

import com.annagurban.eziz.batch.models.FootballClub;
import org.springframework.batch.item.ItemProcessor;

public class CsvToXmlProcessor implements ItemProcessor<FootballClub, FootballClub> {

    @Override
    public FootballClub process(FootballClub i) throws Exception {
        
        return i;
    }

}
