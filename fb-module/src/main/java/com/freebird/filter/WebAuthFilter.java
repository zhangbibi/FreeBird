package com.freebird.filter;

import com.freebird.wx.common.wx.WebAuthAccessToken;
import com.freebird.wx.common.wx.WeixinUtilFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 网页授权页面的过滤器  自动拼接微信的url
 *
 * @author zhangyaping001
 */
public class WebAuthFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(WebAuthFilter.class);


    private WebAuthFilterConfig config;

    public void destroy() {
        logger.info("destroy...");
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String uri = httpRequest.getRequestURI();
        if (config.check(uri)) {
            authToWeb(chain, httpRequest, httpResponse);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void authToWeb(FilterChain chain, HttpServletRequest httpRequest,
                           HttpServletResponse httpResponse) throws IOException,
            ServletException {
        String originalUrl = httpRequest.getRequestURL().toString();
        String queryStr = httpRequest.getQueryString();
        String code = httpRequest.getParameter("code");
        if (StringUtils.isEmpty(code)) {
            String url = originalUrl + "?" + (StringUtils.isEmpty(queryStr) ? "codeFlag=1" : (queryStr + "&codeFlag=1"));
            String authUrl = config.createWxAuthUrl(url, WeixinUtilFactory.getInstance());
            logger.info("WebAuthFilter.authToWeb no code. authUrl=" + authUrl);
            httpResponse.sendRedirect(authUrl);
        } else {
            // 授权跳转之后
            WebAuthAccessToken webAuthAccessToken = WeixinUtilFactory.getInstance().getWebAccessTokenByCode(code);
            logger.info("WebAuthFilter.authToWeb code = " + code + ". webAuthAccessToken = " + webAuthAccessToken);
//            String authUrl = httpRequest.getRequestURL().append(config.queryStrWithout(httpRequest, "code", "codeFlag")).toString();
//            httpResponse.sendRedirect(authUrl);
            chain.doFilter(httpRequest, httpResponse);
        }
    }

    public void init(FilterConfig arg0) throws ServletException {
        logger.info("WebAuthFilter init...");
        config = new WebAuthFilterConfig();
        config.init();
    }
}
