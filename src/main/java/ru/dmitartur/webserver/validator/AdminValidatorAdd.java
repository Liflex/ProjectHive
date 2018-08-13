package ru.dmitartur.webserver.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.dmitartur.webserver.model.User;
import ru.dmitartur.webserver.service.abstraction.UserService;

@Component
public class AdminValidatorAdd implements Validator {

    private final UserService userService;


    @Autowired
    public AdminValidatorAdd(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        ValidationUtils.rejectIfEmpty(errors, "username", "Required", "This field is required.");
        if (userService.get(user.getUsername()) != null) {
            errors.rejectValue("username","Duplicate.userForm.username", "Such username already exists.");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Required", "This field is required.");
    }
}
