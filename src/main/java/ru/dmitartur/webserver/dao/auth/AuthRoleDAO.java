package ru.dmitartur.webserver.dao.auth;

import ru.dmitartur.webserver.model.auth.AuthRole;

import java.util.List;
import java.util.Map;

public interface AuthRoleDAO {

    List<AuthRole> getByUserId(long userId);

    List<AuthRole> getAll();

    Map<Long, List<Long>> getUsersRolesMap();

    void updateRoles(long userId, List<Long> roles);

}
