package com.ai.obc.log.servlet;

import com.ai.obc.log.util.SqlLogGlobalCache;
import com.alibaba.druid.support.http.ResourceServlet;
import com.alibaba.druid.support.json.JSONUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jianyang E-mail:jjzxjgy@126.com
 * @version create time：5/8/19 14:08
 */
public class DruidStatSqlLogViewServlet extends ResourceServlet {

    public final static int               RESULT_CODE_SUCCESS    = 1;
    public final static int               RESULT_CODE_ERROR      = -1;

    public DruidStatSqlLogViewServlet() {
        super("support/http/resources");
    }

    public DruidStatSqlLogViewServlet(String resourcePath) {
        super(resourcePath);
    }


    @Override
    protected String process(String url) {
        //url格式为 /addMoniterSqlTraceKey.json?traceKey=logsql
        Map<String,String> paraMap = getUrlParams(url);
        String traceKey = paraMap.get("traceKey");

        if (null == traceKey || "".equals(traceKey.trim())) {
            return returnJSONResult(RESULT_CODE_ERROR, "traceKey is null");
        }

        if (url.contains("sqlTrace.json")) {
            return returnJSONResult(RESULT_CODE_SUCCESS, sqlTrace(traceKey));
        } else if (url.contains("addMoniterSqlTraceKey.json")) {
            return returnJSONResult(RESULT_CODE_SUCCESS, addMoniterSqlTraceKey(traceKey));
        } else if(url.contains("removeMoniterSqlTraceKey.json")) {
            return returnJSONResult(RESULT_CODE_SUCCESS, removeMoniterSqlTraceKey(traceKey));
        } else {

        }
        return returnJSONResult(RESULT_CODE_ERROR, "not support action[" + url + "]");
    }

    /**
     * 打印sql跟踪记录
     * @param traceKey
     * @return
     */
    private String sqlTrace(String traceKey) {
        List<String> logs = SqlLogGlobalCache.listSqlLog(traceKey);

        StringBuilder sb = new StringBuilder();
        for (String log : logs) {
            sb.append(log);
        }

        return sb.toString();
    }

    /**
     * 添加跟踪的key
     * @param traceKey
     * @return
     */
    private String addMoniterSqlTraceKey(String traceKey) {
        boolean isOk = true;
        try {
            if ("ALL".equalsIgnoreCase(traceKey)) {
                SqlLogGlobalCache.MONITER_ALL = Boolean.TRUE;
            } else {
                SqlLogGlobalCache.SQL_LOG_MONITER_CACHE.put(traceKey, Boolean.TRUE);
            }
        } catch (Exception e) {
            isOk = false;
        }

        return isOk ? "SUCCESS" : "FAIL";
    }

    /**
     * 移除跟踪的key
     * @param traceKey
     * @return
     */
    private String removeMoniterSqlTraceKey(String traceKey) {
        boolean isOk = true;
        try {
            SqlLogGlobalCache.SQL_LOG_MONITER_CACHE.invalidate(traceKey);
        } catch (Exception e) {
            isOk = false;
        }

        return isOk ? "SUCCESS" : "FAIL";
    }

    private static String returnJSONResult(int resultCode, Object content) {
        Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
        dataMap.put("ResultCode", resultCode);
        dataMap.put("Content", content);
        return JSONUtils.toJSONString(dataMap);
    }


    private static Map<String, String> getUrlParams(String url) {
        Map<String, String> map = new HashMap<String, String>();
        if ("".equals(url) || null == url) {
            return map;
        }

        if (url.contains("?")) {
            url = url.split("\\?")[1];
        }

        String[] params = url.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }
}
