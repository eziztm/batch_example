package com.annagurban.eziz.batch.processors;

import org.springframework.batch.item.ItemProcessor;

public class SingleLineProcessor implements ItemProcessor<String, String>{

    @Override
    public String process(String i) throws Exception {
       
        return "Processed in batch: " + i;   
    }

}
