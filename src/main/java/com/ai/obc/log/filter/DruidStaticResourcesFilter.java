package com.ai.obc.log.filter;

import com.alibaba.druid.support.http.AbstractWebStatImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;



public class DruidStaticResourcesFilter extends AbstractWebStatImpl implements Filter {

    private static final String URL_REWRITE_PARAM = "URL_REWRITE_PARAM";

    private static String URL_REWRITE_PARAM_VALUE = "";

    public void init(FilterConfig filterConfig) throws ServletException {
        URL_REWRITE_PARAM_VALUE = filterConfig.getInitParameter(URL_REWRITE_PARAM);
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String rewriteBasePath = (null == URL_REWRITE_PARAM_VALUE || URL_REWRITE_PARAM_VALUE.trim().length() == 0) ? "" : URL_REWRITE_PARAM_VALUE.replaceAll("\\*", "");
        if (!rewriteBasePath.endsWith("/")) {
            rewriteBasePath = rewriteBasePath + "/";
        }

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String context = httpServletRequest.getContextPath();
        String rewritePath = context + rewriteBasePath + "header.html";

        String uri = httpServletRequest.getRequestURI();

        if (rewritePath.equals(uri)) {
            httpServletResponse.sendRedirect( context + rewriteBasePath + "headerExt.html");
        } else {
            chain.doFilter(request, response);
        }
    }
 
    public void destroy() {
        //System.out.println("Xss filter destroyed!");
    }
	
}
