package ru.dmitartur.webserver.dao.auth;

import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.dmitartur.webserver.model.auth.AuthRole;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.internal.objects.NativeDebug.map;

@Repository
public class AuthRoleDAOImpl extends AbstractDAO implements AuthRoleDAO {

    private static final SimpleBeanRowMapper<AuthRole> ROW_MAPPER = new SimpleBeanRowMapper<>(AuthRole.class);

    public AuthRoleDAOImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<AuthRole> getByUserId(long userId) {
        return jdbcTemplate.query("SELECT R.* FROM ST_AUTH_ROLES R, ST_AUTH_USERS_ROLES UR " +
                        "WHERE UR.USER_ID = :userId AND UR.ROLE_ID = R.ID ORDER BY R.ID",
                map("userId", userId), ROW_MAPPER);
    }

    @Override
    public List<AuthRole> getAll() {
        return jdbcTemplate.query("SELECT * FROM ST_AUTH_ROLES ORDER BY ID", ROW_MAPPER);
    }

    @Override
    public Map<Long, List<Long>> getUsersRolesMap() {
        Map<Long, List<Long>> result = new HashMap<>();
        for (Map<String, Object> row : jdbcTemplate.queryForList("SELECT USER_ID, ROLE_ID " +
                "FROM ST_AUTH_USERS_ROLES ORDER BY USER_ID, ROLE_ID", new EmptySqlParameterSource())) {
            Long userId = ((BigDecimal) row.get("USER_ID")).longValue();
            Long roleId = ((BigDecimal) row.get("ROLE_ID")).longValue();
            List<Long> rolesList = result.get(userId);
            if (rolesList == null) {
                rolesList = new ArrayList<>();
                result.put(userId, rolesList);
            }
            rolesList.add(roleId);
        }
        return result;
    }

    @Override
    public void updateRoles(long userId, List<Long> roles) {
        jdbcTemplate.update("DELETE FROM ST_AUTH_USERS_ROLES WHERE USER_ID = :userId",
                map("userId", userId));
        jdbcTemplate.batchUpdate("INSERT INTO ST_AUTH_USERS_ROLES (USER_ID, ROLE_ID) VALUES (:userId, :roleId)",
                (HashMap[]) roles.stream().map(x -> map("userId", userId, "roleId", x)).toArray(HashMap[]::new));
    }
}
