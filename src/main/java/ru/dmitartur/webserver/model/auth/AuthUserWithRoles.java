package ru.dmitartur.webserver.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.BeanUtils;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUserWithRoles extends AuthUser {

    private List<Long> roles;

    public AuthUserWithRoles() {
    }

    public AuthUserWithRoles(AuthUser user, List<Long> roles) {
        try {
            BeanUtils.copyProperties(this, user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.roles = roles;
    }

    public List<Long> getRoles() {
        return roles;
    }

    public void setRoles(List<Long> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "AuthUserWithRoles{" +
                "roles=" + roles +
                "} " + super.toString();
    }
}
