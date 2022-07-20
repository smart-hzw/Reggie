package com.hzw.filter;

import com.alibaba.fastjson.JSON;
import com.hzw.common.BaseContext;
import com.hzw.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static  final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpServletResponse response1 = (HttpServletResponse) response;
        //1、获取本次请求的URI
        String uri = request1.getRequestURI();
        log.info("拦截到请求：{}",uri);

        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs"
        };

        //2、判断本次请求是否需要处理
        boolean check = check(urls, uri);

        //3、如果不需要处理，则直接放行
        if (check){
            log.info("本次请求不需要处理：{}",uri);
            chain.doFilter(request1,response1);
            return;
        }

        //4-1、判断网页端登录状态，如果已登录，则直接放行
        if (request1.getSession().getAttribute("employee")!=null){
            log.info("用户 {} 已登录",request1.getSession().getAttribute("employee"));
            Long empId = (Long) request1.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            chain.doFilter(request1,response1);
            return;
        }

        //4-12、判断移动端登录状态，如果已登录，则直接放行
        if (request1.getSession().getAttribute("user")!=null){
            log.info("用户 {} 已登录",request1.getSession().getAttribute("user"));
            Long userId = (Long) request1.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            chain.doFilter(request1,response1);
            return;
        }

        //5、如果未登录则返回未登录结果.通过输出流方式向客户端响应数据
        log.info("用户未登录");
        response1.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次请求是否放行
     * @param urls
     * @param requestUri
     * @return
     */
    public boolean check(String[] urls,String requestUri){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestUri);
            if (match) return true;
        }
        return false;
    }
}
