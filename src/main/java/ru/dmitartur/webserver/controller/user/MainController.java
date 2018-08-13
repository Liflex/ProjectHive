package ru.dmitartur.webserver.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/user")
    public String login(Model model) {
        return "user";
    }
}
