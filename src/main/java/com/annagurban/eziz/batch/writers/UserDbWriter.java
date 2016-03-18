package com.annagurban.eziz.batch.writers;

import com.annagurban.eziz.batch.models.User;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDbWriter implements ItemWriter<User> {

    private static final Logger log = LoggerFactory.getLogger(UserDbWriter.class);

    private static final String INSERT_USER = "insert into USERS (username) values (?)";
    private static final String INSERT_ROLES = "insert into ROLES (username,rolename) values (?,?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void write(List<? extends User> list) throws Exception {
        list.stream().forEach((user) -> {
            try {
                jdbcTemplate.update(INSERT_USER, user.getUsername());
                for (String rolename : user.getRoles()) {
                    jdbcTemplate.update(INSERT_ROLES, user.getUsername(), rolename);
                }

                log.info("User {} is written to DB", user.getUsername());
            } catch (DataAccessException e) {
                log.error("Failed to write to DB: {}", user.getUsername());
                log.error("Exception: {}", e.getMessage());
            }
        });

    }

}
