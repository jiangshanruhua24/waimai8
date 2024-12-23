package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否登录
 */
@WebFilter(filterName = "loginCheck",urlPatterns = "/*")
@Slf4j
public class LoginCheck implements Filter {
    public static final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String URI = request.getRequestURI();
        String[] urls =  new String[]{
                "/employee/login",
                "/employee/register",
                "/backend/**",
                "/front/**"
        };
        log.info("拦截到{}",request.getRequestURI());

        //3.
        if(check(urls, URI)){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        //4.判断登录状态，如果已登录 放行
        if(request.getSession().getAttribute("employee") != null){
            filterChain.doFilter(request, response);
            return;
        }

        //5.如果未登录 返回登录结果
        response.getWriter().write(JSON.toJSONString(R.error("未登录")));
        return;
    }

    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            if (matcher.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }
}
