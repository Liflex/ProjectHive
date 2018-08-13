package ru.dmitartur.webserver.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.dmitartur.webserver.model.User;
import ru.dmitartur.webserver.service.abstraction.UserService;

@Component
public class UserValidatorRegistration implements Validator {

    private final UserService userService;


    @Autowired
    public UserValidatorRegistration(UserService userService) {
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
        if (user.getUsername().length() < 4 || user.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username", "Username must be between 4 and 32 characters.");
        }

        if (userService.get(user.getUsername()) != null) {
            errors.rejectValue("username","Duplicate.userForm.username", "Such username already exists.");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Required", "This field is required.");
        if (user.getPassword().length() < 6 || user.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password", "Password must be over 6 characters.");
        }
    }
}
