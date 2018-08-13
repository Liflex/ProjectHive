package ru.dmitartur.webserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dmitartur.webserver.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
