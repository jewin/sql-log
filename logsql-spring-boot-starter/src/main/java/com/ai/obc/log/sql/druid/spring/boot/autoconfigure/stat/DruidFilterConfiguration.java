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
package com.ai.obc.log.sql.druid.spring.boot.autoconfigure.stat;

import com.ai.obc.log.filter.DruidStatLogSqlFilter;
import com.alibaba.druid.filter.config.ConfigFilter;
import com.alibaba.druid.filter.encoding.EncodingConvertFilter;
import com.alibaba.druid.filter.logging.*;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lihengming [89921218@qq.com]
 */
public class DruidFilterConfiguration {


    @Bean
    @ConfigurationProperties(FILTER_LOGSQL_PREFIX)
    @ConditionalOnProperty(prefix = FILTER_LOGSQL_PREFIX, name = "enabled")
    @ConditionalOnMissingBean
    public DruidStatLogSqlFilter statLogSqlFilter() {
        return new DruidStatLogSqlFilter();
    }


    private static final String FILTER_LOGSQL_PREFIX = "spring.datasource.druid.filter.logsql";



}
