package ru.dmitartur.webserver.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.dmitartur.webserver.model.User;
import ru.dmitartur.webserver.service.abstraction.UserService;

@Controller
@RequestMapping(value="/admin")
public class UpdateController {

    public final UserService userService;

    @Autowired
    public UpdateController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/update")
    protected  String updateUser(@ModelAttribute("user")User user) {
        try {
            userService.update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/list";
    }
}
