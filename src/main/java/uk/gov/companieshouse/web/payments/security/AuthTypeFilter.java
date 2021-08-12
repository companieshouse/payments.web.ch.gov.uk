package uk.gov.companieshouse.web.payments.security;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class AuthTypeFilter implements Filter {


    public AuthTypeFilter() {
    }

    public void destroy() {
    }

    public void init(FilterConfig filterConfig) {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("IN FILTER!!!!!");
//        System.out.println(request);
//        System.out.println(
//                ((HttpServletRequest) request).getServletPath() + StringUtils.defaultString(((HttpServletRequest)request).getPathInfo())
//        );

        //request.getRequestDispatcher(((HttpServletRequest) request).getServletPath() + StringUtils.defaultString(((HttpServletRequest)request).getPathInfo()) ).forward(request, response);
//        request.setAttribute("executeRemainingFilters", false);
        chain.doFilter(request, response);
    }
}
