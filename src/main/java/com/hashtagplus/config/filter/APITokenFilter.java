package com.hashtagplus.config.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hashtagplus.model.HtplUser;
import com.hashtagplus.model.HtplUserDetails;
import com.hashtagplus.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

public class APITokenFilter extends GenericFilterBean {

    private UserService userService;


    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        if(userService==null){
            ServletContext servletContext = req.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            userService = webApplicationContext.getBean(UserService.class);
        }

        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        final String authHeader = request.getHeader("authorization");


        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);

            chain.doFilter(req, res);
        } else {
            HtplUserDetails user = null;

            HttpSession session = request.getSession(false);
            if (session != null) {
                SecurityContextImpl sci = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
                if (sci != null) {
                    HtplUser cud = (HtplUser) sci.getAuthentication().getPrincipal();
                    user = userService.getUserById(cud.getId());
                }
            }

            if (user == null && (authHeader == null || !authHeader.startsWith("Bearer "))) {
                throw new ServletException("Missing or invalid Authorization header");
            }

            if (user == null) {
                final String token = authHeader.substring(7);
                user = userService.getUserByToken(token);
                if (user == null)
                    throw new ServletException("Invalid token");
            }
            else {

            }

            HtplUser htplUser = new HtplUser(user.getUsername(), user.getPassword(), user.id, user.getAuthorities());
            req.setAttribute("user_from_token", htplUser);

            chain.doFilter(req, res);
        }
    }
}