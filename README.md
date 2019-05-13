sql-log是一个基于druid数据库连接池的sql执行记录监控
可以监控指定用户的sql执行记录
也可以监控应有的所有sql执行记录
简单说来，就是将完整的sql执行记录（包括where条件中的所有参数）都展示到页面上。

使用方式
        <dependency>
            <groupId>com.ai.obc.log</groupId>
            <artifactId>logsql-spring-boot-starter</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        
        spring.datasource.druid.stat-view-servlet.enabled=true
        spring.datasource.druid.stat-view-servlet.url-pattern=/druid/*
        spring.datasource.druid.stat-view-servlet.reset-enable=true
        spring.datasource.druid.stat-view-servlet.login-username=Jewin
        spring.datasource.druid.stat-view-servlet.login-password=Jewin-benhua-erp
        #spring.datasource.druid.stat-view-servlet.allow=
        #spring.datasource.druid.stat-view-servlet.deny=
        
        spring.datasource.druid.filter.logsql.enabled=true
        spring.datasource.druid.stat-sql-log-view-servlet.enabled=true
        spring.datasource.druid.stat-sql-log-view-servlet.url-pattern=/druid/logsql/*
