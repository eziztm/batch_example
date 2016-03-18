package com.annagurban.eziz.batch.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@PropertySources({
    @PropertySource(value = "classpath:application.properties"), 
   //@PropertySource(value = "file:application-${app.env:test}.properties")
})
public class ApplicationConfig {

    @Autowired
    Environment env;

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

      // MySQL with a populator
//    @Value("classpath:schema-mysql.sql")
//    private Resource schemaScript;
//
//    @Bean
//    public DataSource mysqlDataSource() throws SQLException {
//        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//        dataSource.setDriver(new com.mysql.jdbc.Driver());
//        dataSource.setUrl("jdbc:mysql://localhost:3306/");
//        dataSource.setUsername("root");
//        dataSource.setPassword("admin");
//        DatabasePopulatorUtils.execute(databasePopulator(), dataSource);
//        return dataSource;
//    }
//
//    @Bean
//    public JdbcTemplate jdbcTemplate(@Qualifier(value = "mysqlDataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }
//
//    private DatabasePopulator databasePopulator() {
//        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//        populator.addScript(schemaScript);
//        return populator;
//    }
}
