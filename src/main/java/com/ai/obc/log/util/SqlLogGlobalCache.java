package com.ai.obc.log.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author jianyang E-mail:jjzxjgy@126.com
 * @version create time：5/7/19 14:33
 */
public class SqlLogGlobalCache {

    /**
     * 是否监控所有的sql记录
     */
    public static Boolean MONITER_ALL = Boolean.FALSE;

    /**
     * 需要监控的key，主要为sessionID
     */
    public final static LoadingCache<String, Boolean> SQL_LOG_MONITER_CACHE = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(2, TimeUnit.HOURS)
            .build(
                    new CacheLoader<String, Boolean>() {
                        @Override
                        public Boolean load(String key) throws Exception {
                            return Boolean.FALSE;
                        }
                    });

    /**
     * SQL LOG CAHE
     */
    private final static LoadingCache<String, LinkedBlockingQueue<String>> SQL_LOG_CACHE = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, LinkedBlockingQueue<String>>() {
                        @Override
                        public LinkedBlockingQueue<String> load(String key) throws Exception {
                            return new LinkedBlockingQueue<String>(10240);
                        }
                    });


    /**
     * 记录sql日志
     * @param key
     * @param sql
     */
    public static void addSqlLog(String key, String sql) {
        try {
            LinkedBlockingQueue<String> queue = SQL_LOG_CACHE.get(key);
            if (null == queue) {
                SQL_LOG_CACHE.put(key, new LinkedBlockingQueue<String>(1024));
            }
            if (1024 > queue.size()) {
                queue.offer(sql + "<br/><br/>");
            } else {
                //do nothings
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 抓取sql日志
     * @param key
     * @return
     */
    public static List<String> listSqlLog(String key) {
        List<String> list = new LinkedList<String>();
        try {
            SQL_LOG_CACHE.get(key).drainTo(list, 128);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return list;
    }


}
