package ru.dmitartur.webserver.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.dmitartur.webserver.service.abstraction.UserService;
import ru.dmitartur.webserver.validator.AdminValidatorAdd;

@Controller
@RequestMapping(value="/admin")
public class AddController {

    private final UserService userService;

    private final AdminValidatorAdd adminValidatorAdd;

    @Autowired
    public AddController(UserService userService, AdminValidatorAdd adminValidatorAdd) {
        this.userService = userService;
        this.adminValidatorAdd = adminValidatorAdd;
    }


    @PostMapping(value = "/add")
    public ModelAndView addNewUser(@ModelAttribute("user")User user, BindingResult bindingResult, Model model) {
        adminValidatorAdd.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return new ModelAndView("registration", "user", user);
        }
        userService.add(user);
        model.addAttribute("message", "Add new user is successfully.");
        model.addAttribute("Users", userService.getAll());
        return new ModelAndView("list", "user", new User());
    }

}
