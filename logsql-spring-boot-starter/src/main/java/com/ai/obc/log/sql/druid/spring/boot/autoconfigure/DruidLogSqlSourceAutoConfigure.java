/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ai.obc.log.sql.druid.spring.boot.autoconfigure;

import javax.sql.DataSource;

import com.ai.obc.log.filter.DruidStatLogSqlFilter;
import com.ai.obc.log.servlet.DruidStatSqlLogViewServlet;
import com.ai.obc.log.sql.druid.spring.boot.autoconfigure.stat.DruidStatSqlLogViewServletConfiguration;
import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.ai.obc.log.sql.druid.spring.boot.autoconfigure.properties.DruidLogSqlStatProperties;
import com.ai.obc.log.sql.druid.spring.boot.autoconfigure.stat.DruidFilterConfiguration;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.LinkedList;
import java.util.List;

/**
 * @author lihengming [89921218@qq.com]
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureAfter(DruidDataSourceAutoConfigure.class)
@EnableConfigurationProperties({DruidLogSqlStatProperties.class})
@Import({DruidFilterConfiguration.class,
        DruidStatLogSqlFilter.class,
        DruidStatSqlLogViewServletConfiguration.class})
public class DruidLogSqlSourceAutoConfigure implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DruidLogSqlSourceAutoConfigure.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DruidStatLogSqlFilter statLogSqlFilter;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (dataSource instanceof DruidDataSource) {
            DruidDataSource druidDataSource = (DruidDataSource) dataSource;
            List<Filter> filters = new LinkedList<Filter>();
            filters.add(statLogSqlFilter);
            druidDataSource.setProxyFilters(filters);
        }
    }
}
