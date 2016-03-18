package com.annagurban.eziz.batch;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

//
//public class DbWriter implements ItemWriter<LdapUser> {
//
//    private static final Logger log = LoggerFactory.getLogger(LdapUserWriter.class);
//
//    private static final String INSERT_USER = "insert into LDAPUSER (username,partref,email) values (?,?,?)";
//    private static final String INSERT_GROUPS = "insert into LDAPUSER_GROUPS (username,groupname) values (?,?)";
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Override
//    public void write(List<? extends LdapUser> list) throws Exception {
//
//        list.stream().forEach((user) -> {
//            try {
//                jdbcTemplate.update(INSERT_USER, user.getUsername(), user.getPartref(), user.getEmail());
//                for (String s : user.getGroups()) {
//                    jdbcTemplate.update(INSERT_GROUPS, user.getUsername(), s);
//                }
//            } catch (DataAccessException e) {
//                log.error("Failed to write to DB: {}", user.getPartref());
//                log.error("Exception: {}", e.getMessage());
//            }
//        });
//    }
//
//}