package ru.dmitartur.webserver.service.abstraction;



import ru.dmitartur.webserver.model.User;

import java.util.List;


public interface UserService {
    User get(String login);
    List<User> getAll();
    void add(User t);
    void update(User t);
    void delete(long id);
}
