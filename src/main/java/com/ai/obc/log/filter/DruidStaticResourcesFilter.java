package com.ai.obc.log.filter;

import com.alibaba.druid.support.http.AbstractWebStatImpl;
import com.alibaba.druid.util.Utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;



public class DruidStaticResourcesFilter extends AbstractWebStatImpl implements Filter {

    private static final String URL_REWRITE_PARAM = "URL_REWRITE_PARAM";

    private static String URL_REWRITE_PARAM_VALUE = "";

    private static final String STATIC_RESOURCES_FILE_PATH = "support/http/resources";

    public void init(FilterConfig filterConfig) throws ServletException {
        URL_REWRITE_PARAM_VALUE = filterConfig.getInitParameter(URL_REWRITE_PARAM);
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String rewriteBasePath = (null == URL_REWRITE_PARAM_VALUE || URL_REWRITE_PARAM_VALUE.trim().length() == 0) ? "" : URL_REWRITE_PARAM_VALUE.replaceAll("\\*", "");
        if (!rewriteBasePath.endsWith("/")) {
            rewriteBasePath = rewriteBasePath + "/";
        }

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String context = httpServletRequest.getContextPath();
        String rewritePath = context + rewriteBasePath + "header.html";

        String uri = httpServletRequest.getRequestURI();

        if (uri.contains(rewritePath)) {
            response.setContentType("text/html; charset=utf-8");
            String text = Utils.readFromResource(STATIC_RESOURCES_FILE_PATH + "/headerExt.html");
            response.getWriter().print(text);
        } else {
            chain.doFilter(request, response);
        }
    }
 
    public void destroy() {
        //System.out.println("Xss filter destroyed!");
    }
	
}
