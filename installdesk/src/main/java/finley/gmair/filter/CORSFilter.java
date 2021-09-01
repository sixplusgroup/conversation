package finley.gmair.filter;



import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;


// 定义filterName 和过滤的url
@WebFilter(filterName="CORSFilter",urlPatterns="/*")
@Component
public class CORSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String orignalHeader = request.getHeader("Origin");

        response.addHeader("Access-Control-Allow-Origin", orignalHeader);
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.addHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {

            filterChain.doFilter(request, response);
        }
    }


    @Override
    public void destroy() {

    }
}
