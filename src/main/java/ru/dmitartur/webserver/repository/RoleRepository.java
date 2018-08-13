package ru.dmitartur.webserver.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.dmitartur.webserver.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
