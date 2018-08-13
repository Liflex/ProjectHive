package ru.dmitartur.webserver.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dmitartur.webserver.dao.auth.AuthRoleDAO;
import ru.dmitartur.webserver.dao.auth.AuthUserDAO;
import ru.dmitartur.webserver.model.auth.AuthRole;
import ru.dmitartur.webserver.model.auth.AuthUser;
import ru.dmitartur.webserver.model.auth.AuthUserWithRoles;
import ru.dmitartur.webserver.model.auth.UserDetailsImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AuthUserDAO authUserDao;

    @Autowired
    private AuthRoleDAO authRoleDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Set<String> usersForLogout = new HashSet<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserDao.getByLogin(username);
        if (authUser != null) {
            List<AuthRole> roles = authRoleDao.getByUserId(authUser.getId());
            return new UserDetailsImpl(authUser, roles);
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }

    @Override
    public List<AuthUserWithRoles> getUsers() {
        Map<Long, List<Long>> usersRolesMap = authRoleDao.getUsersRolesMap();
        return authUserDao.getAll()
                .stream()
                .map(x -> {
                    x.setPassword(null);
                    return new AuthUserWithRoles(x, usersRolesMap.get(x.getId()));
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthRole> getRoles() {
        return authRoleDao.getAll();
    }

    @Override
    @Transactional
    public void saveUser(AuthUser user, List<Long> roles) {
        if (StringUtils.isBlank(user.getLogin()) || StringUtils.isBlank(user.getFullName())) {
            throw new RuntimeException("Validate failed before save for " + user);
        }
        boolean forceLogout = false;
        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            forceLogout = true;
        } else {
            user.setPassword(null);
        }
        if (user.getId() == 0) {
            authUserDao.create(user);
            authRoleDao.updateRoles(user.getId(), roles);
        } else {
            AuthUser existingUser = authUserDao.getById(user.getId());
            authUserDao.update(user);
            List<Long> existingRoles = authRoleDao.getByUserId(user.getId()).stream().map(x -> x.getId()).collect(Collectors.toList());
            // для пользователя новые роли заменяют старые
            // для главного администратора можно только добавлять новые роли
            if (("admin".equals(existingUser.getLogin()) && roles.containsAll(existingRoles) && roles.size() > existingRoles.size())
                    || (!"admin".equals(existingUser.getLogin()) && !CollectionUtils.isEqualCollection(roles, existingRoles))) {
                authRoleDao.updateRoles(user.getId(), roles);
                forceLogout = true;
            }
        }
        if (forceLogout) {
            usersForLogout.add(user.getLogin());
        }

    }

    @Override
    public void deleteUser(long userId) {
        AuthUser user = authUserDao.getById(userId);
        authUserDao.delete(user);
        usersForLogout.add(user.getLogin());
    }

    @Override
    public boolean shouldLogout(String login) {
        return usersForLogout.contains(login);
    }

    @Override
    public void userLogoutSuccess(String login) {
        usersForLogout.remove(login);
    }

    @Override
    public void userAuthSuccess(String login) {
        usersForLogout.remove(login);
    }
}
