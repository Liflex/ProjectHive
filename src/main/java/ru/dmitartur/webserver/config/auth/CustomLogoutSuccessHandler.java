package ru.dmitartur.webserver.config.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication)
            throws IOException, ServletException {
        if (request.getQueryString() != null && request.getQueryString().contains("force")) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
