package com.ai.obc.log.sql.druid.spring.boot.autoconfigure.stat;


import com.ai.obc.log.servlet.DruidStatSqlLogViewServlet;
import com.ai.obc.log.sql.druid.spring.boot.autoconfigure.properties.DruidLogSqlStatProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@ConditionalOnWebApplication
@ConditionalOnProperty(name = {"spring.datasource.druid.stat-sql-log-view-servlet.enabled"}, havingValue = "true")
public class DruidStatSqlLogViewServletConfiguration {

    public DruidStatSqlLogViewServletConfiguration() {
    }

    @Bean
    public ServletRegistrationBean statSqlLogViewServletRegistrationBean(DruidLogSqlStatProperties properties) {
        DruidLogSqlStatProperties.StatSqlLogViewServlet config = properties.getStatSqlLogViewServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean();
        registrationBean.setServlet(new DruidStatSqlLogViewServlet());
        registrationBean.addUrlMappings(config.getUrlPattern() != null ? config.getUrlPattern() : "/druid/logsql/*");
        return registrationBean;
    }
}
