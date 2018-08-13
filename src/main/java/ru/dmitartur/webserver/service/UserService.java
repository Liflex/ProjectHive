package ru.dmitartur.webserver.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.dmitartur.webserver.model.auth.AuthRole;
import ru.dmitartur.webserver.model.auth.AuthUser;
import ru.dmitartur.webserver.model.auth.AuthUserWithRoles;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<AuthUserWithRoles> getUsers();

    List<AuthRole> getRoles();

    void saveUser(AuthUser user, List<Long> roles);

    void deleteUser(long userId);

    boolean shouldLogout(String login);

    void userLogoutSuccess(String login);

    void userAuthSuccess(String login);

}
