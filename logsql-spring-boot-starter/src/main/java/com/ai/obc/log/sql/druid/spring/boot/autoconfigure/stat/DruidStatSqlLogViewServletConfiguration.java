package com.ai.obc.log.sql.druid.spring.boot.autoconfigure.stat;


import com.ai.obc.log.filter.DruidStaticResourcesFilter;
import com.ai.obc.log.servlet.DruidStatSqlLogViewServlet;
import com.ai.obc.log.sql.druid.spring.boot.autoconfigure.properties.DruidLogSqlStatProperties;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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

    @Bean
    public FilterRegistrationBean druidStaticResourcesRewriteFilterRegistrationBean(DruidStatProperties properties) {

        DruidStaticResourcesFilter druidStaticResourcesFilter = new DruidStaticResourcesFilter();
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(druidStaticResourcesFilter);
        registrationBean.addInitParameter("URL_REWRITE_PARAM", properties.getStatViewServlet().getUrlPattern());

        return registrationBean;
    }
}
