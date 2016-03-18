package com.annagurban.eziz.batch.processors;

import com.annagurban.eziz.batch.models.User;
import org.springframework.batch.item.ItemProcessor;

public class XmlToDbProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(User i) throws Exception {
        
        
        return i;
    }

}
