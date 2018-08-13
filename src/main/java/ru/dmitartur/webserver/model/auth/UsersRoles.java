package ru.dmitartur.webserver.model.auth;

import java.util.List;

public class UsersRoles {

    private List<AuthUserWithRoles> users;
    private List<AuthRole> roles;
    private long currentUserId;

    public UsersRoles(List<AuthUserWithRoles> users, List<AuthRole> roles, long currentUserId) {
        this.users = users;
        this.roles = roles;
        this.currentUserId = currentUserId;
    }

    public List<AuthUserWithRoles> getUsers() {
        return users;
    }

    public void setUsers(List<AuthUserWithRoles> users) {
        this.users = users;
    }

    public List<AuthRole> getRoles() {
        return roles;
    }

    public void setRoles(List<AuthRole> roles) {
        this.roles = roles;
    }

    public long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(long currentUserId) {
        this.currentUserId = currentUserId;
    }

    @Override
    public String toString() {
        return "UsersRoles{" +
                "users=" + users +
                ", roles=" + roles +
                ", currentUserId=" + currentUserId +
                '}';
    }
}
