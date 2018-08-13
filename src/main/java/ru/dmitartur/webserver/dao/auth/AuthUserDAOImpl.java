package ru.dmitartur.webserver.dao.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.dmitartur.webserver.model.auth.AuthUser;

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

@Repository
public class AuthUserDAOImpl extends AbstractDAO implements AuthUserDAO {

    private static final SimpleBeanRowMapper<AuthUser> ROW_MAPPER = new SimpleBeanRowMapper<>(AuthUser.class);

    public AuthUserDAOImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public AuthUser getByLogin(String login) {
        List<AuthUser> users = jdbcTemplate.query(
                "SELECT * FROM ST_AUTH_USERS where LOGIN = :login",
                map("login", login), ROW_MAPPER);
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<AuthUser> getAll() {
        return jdbcTemplate.query("SELECT * FROM ST_AUTH_USERS WHERE ACTIVE = 1 ORDER BY LOGIN", ROW_MAPPER);
    }

    @Override
    public AuthUser getById(long id) {
        List<AuthUser> users = jdbcTemplate.query(
                "SELECT * FROM ST_AUTH_USERS where ID = :id", map("id", id), ROW_MAPPER);
        if (!users.isEmpty()) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void create(AuthUser user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                "INSERT INTO ST_AUTH_USERS (ID, LOGIN, FULL_NAME, \"COMMENT\", PASSWORD, ACTIVE) values ("
                        + nextval("ST_AUTH_USERS_SEQ") + ", :login, :fullName, :comment, :password, 1)",
                new MapSqlParameterSource(describeBean(user)), keyHolder, new String[]{"id"});
        user.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(AuthUser user) {
        jdbcTemplate.update("UPDATE ST_AUTH_USERS SET FULL_NAME = :fullName, \"COMMENT\" = :comment WHERE ID = :id",
                map("fullName", user.getFullName(), "comment", user.getComment(), "id", user.getId()));
        if (StringUtils.isNotBlank(user.getPassword())) {
            jdbcTemplate.update("UPDATE ST_AUTH_USERS SET PASSWORD = :password WHERE ID = :id",
                    map("password", user.getPassword(), "id", user.getId()));
        }
    }

    @Override
    public void delete(AuthUser user) {
        String newLogin = StringUtils.abbreviate("_DEL_" + user.getLogin() + "_"
                + UUID.randomUUID().toString(), 2000);
        jdbcTemplate.update("UPDATE ST_AUTH_USERS SET LOGIN = :login, ACTIVE = 0 WHERE ID = :id",
                map("login", newLogin, "id", user.getId()));
    }
}
