package com.ai.obc.log.filter;

import com.ai.obc.log.util.SqlLogGlobalCache;
import com.ai.obc.log.util.StringDiffUtil;
import com.alibaba.druid.filter.logging.LogFilter;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.sql.SQLUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author jianyang E-mail:jjzxjgy@126.com
 * @version create time：5/6/19 13:35
 */
public class DruidStatLogSqlFilter extends LogFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(DruidStatLogSqlFilter.class);

    private final static SQLUtils.FormatOption FORMAT_OPTION = new SQLUtils.FormatOption(true, false);

    private final StringDiffUtil STRING_DIFF_UTIL = new StringDiffUtil();

    @Override
    protected  void connectionLog(String message) {

    }

    @Override
    protected  void statementLog(String message){

    }

    @Override
    protected  void statementLogError(String message, Throwable error){

    }

    @Override
    protected  void resultSetLog(String message){

    }

    @Override
    protected  void resultSetLogError(String message, Throwable error){

    }

    @Override
    protected void statementExecuteUpdateAfter(StatementProxy statement, String sql, int updateCount) {
        logExecutableSql(statement, sql);
    }

    @Override
    protected void statementExecuteQueryAfter(StatementProxy statement, String sql, ResultSetProxy resultSet) {
        logExecutableSql(statement, sql);
    }


    @Override
    protected void statementExecuteAfter(StatementProxy statement, String sql, boolean firstResult) {
        logExecutableSql(statement, sql);
    }


    @Override
    protected void statementExecuteBatchAfter(StatementProxy statement, int[] result) {
        logExecutableSql(statement, statement.getBatchSql());
    }


    private void logExecutableSql(StatementProxy statement, String sql) {
        String sessionId = "";
        String url = "";
        String queryStr = "";
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != requestAttributes) {
            HttpServletRequest request = requestAttributes.getRequest();
            sessionId = request.getRequestedSessionId();
            url = request.getRequestURL().toString();
            queryStr = request.getQueryString();
        }

        try {
            //如果当前请求没有被监控，则不记录sql
            if (!SqlLogGlobalCache.SQL_LOG_MONITER_CACHE.get(sessionId) && !SqlLogGlobalCache.MONITER_ALL) {
                return;
            }
        } catch (ExecutionException e) {
            return;
        }

        statement.setLastExecuteTimeNano();
        double nanos = statement.getLastExecuteTimeNano();
        double millis = nanos / (1000 * 1000);



        if (null != queryStr && queryStr.trim().length() > 0) {
            queryStr = "?" + queryStr;
        } else {
            queryStr = "";
        }

        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss").format(new Date());
        String logSql = sessionId + " " + timeStamp + "   " + url + queryStr + " execute elapsed " + millis + "millis.  <br/>";
        int parametersSize = statement.getParametersSize();
        if (parametersSize == 0) {
            String traceSql = logSql + sql;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info(traceSql);
            }
            if (SqlLogGlobalCache.MONITER_ALL) {
                SqlLogGlobalCache.addSqlLog("ALL", traceSql.replaceAll("\\n", "<br/>"));
            }
            SqlLogGlobalCache.addSqlLog(sessionId, traceSql.replaceAll("\\n", "<br/>"));
            return;
        }

        List<Object> parameters = new ArrayList<Object>(parametersSize);
        for (int i = 0; i < parametersSize; ++i) {
            JdbcParameter jdbcParam = statement.getParameter(i);
            parameters.add(jdbcParam != null
                    ? jdbcParam.getValue()
                    : null);
        }

        String dbType = statement.getConnectionProxy().getDirectDataSource().getDbType();
        String formatSql = SQLUtils.format(sql, dbType, parameters, FORMAT_OPTION);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.info(formatSql);
        }

        formatSql = STRING_DIFF_UTIL.diffPrettyHtmlWithNoneDelete(sql.replaceAll(", ", ",").replaceAll(" ,", ",").replaceAll(" , ", ","), formatSql.replaceAll(", ", ",").replaceAll(" ,", ",").replaceAll(" , ", ","));
        String traceSql = logSql + formatSql;

        if (SqlLogGlobalCache.MONITER_ALL) {
            SqlLogGlobalCache.addSqlLog("ALL", traceSql.replaceAll("\\n", "<br/>"));
        }
        SqlLogGlobalCache.addSqlLog(sessionId, traceSql.replaceAll("\\n", "<br/>"));
    }

    @Override
    public String getDataSourceLoggerName() {
        return null;
    }

    @Override
    public void setDataSourceLoggerName(String loggerName) {

    }

    @Override
    public String getConnectionLoggerName() {
        return null;
    }

    @Override
    public void setConnectionLoggerName(String loggerName) {

    }

    @Override
    public String getStatementLoggerName() {
        return null;
    }

    @Override
    public void setStatementLoggerName(String loggerName) {

    }

    @Override
    public String getResultSetLoggerName() {
        return null;
    }

    @Override
    public void setResultSetLoggerName(String loggerName) {

    }


}
