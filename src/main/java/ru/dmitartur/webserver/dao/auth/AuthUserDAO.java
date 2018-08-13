package ru.dmitartur.webserver.dao.auth;


import ru.dmitartur.webserver.model.auth.AuthUser;

import java.util.List;

public interface AuthUserDAO {

    AuthUser getByLogin(String login);

    AuthUser getById(long id);

    List<AuthUser> getAll();

    void update(AuthUser user);

    void create(AuthUser user);

    void delete(AuthUser user);

}
