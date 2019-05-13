package com.ai.obc.log.sql.druid.spring.boot.autoconfigure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("spring.datasource.druid")
public class DruidLogSqlStatProperties {
    private String[] aopPatterns;
    private StatSqlLogViewServlet statSqlLogViewServlet = new StatSqlLogViewServlet();


    public String[] getAopPatterns() {
        return aopPatterns;
    }

    public void setAopPatterns(String[] aopPatterns) {
        this.aopPatterns = aopPatterns;
    }

    public StatSqlLogViewServlet getStatSqlLogViewServlet() {
        return statSqlLogViewServlet;
    }

    public void setStatSqlLogViewServlet(StatSqlLogViewServlet statSqlLogViewServlet) {
        this.statSqlLogViewServlet = statSqlLogViewServlet;
    }

    public static class StatSqlLogViewServlet {
        /**
         * Enable StatViewServlet, default false.
         */
        private boolean enabled;
        private String urlPattern;


        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getUrlPattern() {
            return urlPattern;
        }

        public void setUrlPattern(String urlPattern) {
            this.urlPattern = urlPattern;
        }
    }

}
