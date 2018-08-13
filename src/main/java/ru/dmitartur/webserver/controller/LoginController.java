package ru.dmitartur.webserver.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class  LoginController {

    @GetMapping ("/")
    public String login(Model model, String error, String logout) {
//        System.out.println("ОБРАБОТКА");
//        if (error != null) {
//            model.addAttribute("error", "Username or password is incorrect.");
//        }

        return "login";
    }

    @GetMapping(value = "/logout")
    public String logout(final HttpServletRequest request, final HttpServletResponse response, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        model.addAttribute("message", "Logged out successfully.");

        return "login";
    }
}
