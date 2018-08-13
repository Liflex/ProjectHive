package ru.dmitartur.webserver.config.auth;

import org.springframework.web.filter.OncePerRequestFilter;
import ru.systemres.vsrf.stat.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

public class ForceLogoutFilter extends OncePerRequestFilter {

    private UserService userService;

    public ForceLogoutFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Principal principal = request.getUserPrincipal();
        if (principal != null && userService.shouldLogout(principal.getName())) {
            response.sendRedirect(request.getContextPath() + "/logout");
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
