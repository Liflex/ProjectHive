package ru.dmitartur.webserver.dao.auth;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AbstractDAO {

    protected final static Logger LOGGER = LoggerFactory.getLogger(AbstractDAO.class);

    protected final NamedParameterJdbcTemplate jdbcTemplate;

    public AbstractDAO(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        JdbcOperations operations = jdbcTemplate.getJdbcOperations();
        if (operations instanceof JdbcTemplate) {
            ((JdbcTemplate) operations).setFetchSize(1000);
        }
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public static Map<String, Object> map(Object... keysAndValues) {
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            result.put((String) keysAndValues[i], keysAndValues[i + 1]);
        }
        return result;
    }

    /**
     * Заворачивает запрос в offset, limit.
     *
     * @param mapper
     * @param fields - перечисление полей, которые надо выбрать вида e.DT, e.NAME, e.EVENT_TYPE
     * @param tables - перечисление таблиц по которым идет выборка данных вида CHECK_EVENT e left join STOREYS z on (c.STOREY = z.STOREY)
     * @param where  - условия для фильтрации с учетом поиска вида where x=1 and y=2
     * @param params - параметры запроса
     * @param offset - сколько записей с начала пропустить
     * @param limit  - количество записей, которое должено попасть в выборку
     * @param order  - сортировка данных вида order by x desc
     * @return
     */
    protected <T> List<T> selectPage(RowMapper<T> mapper, String fields, String tables,
                                     String where, Map<String, Object> params, int offset, int limit, String order) {

        if (where == null) {
            where = "";
        }
        if (order == null) {
            order = "";
        }

        String dataSelect = "select " + fields + " from\n";

        return selectPage(mapper, dataSelect + tables + " " + where + " " + order, params, offset, limit);
    }

    protected <T> List<T> selectPage(RowMapper<T> mapper, String sql, Map<String, Object> params, int offset, int limit) {
        StringBuilder query = new StringBuilder();
        params = new HashMap<>(params);
        prepareSelectPageSql(query, params, sql, offset, limit);

        return jdbcTemplate.query(query.toString(), params, mapper);
    }

    protected <T> T selectPage(ResultSetExtractor<T> extractor, String sql, Map<String, Object> params, int offset, int limit) {
        return selectPage(jdbcTemplate, extractor, sql, params, offset, limit);
    }

    protected <T> T selectPage(NamedParameterJdbcTemplate jdbcTemplate,
                               ResultSetExtractor<T> extractor, String sql, Map<String, Object> params, int offset, int limit) {
        StringBuilder query = new StringBuilder();
        params = new HashMap<>(params);
        prepareSelectPageSql(query, params, sql, offset, limit);

        return jdbcTemplate.query(query.toString(), params, extractor);
    }

    private static void prepareSelectPageSql(StringBuilder sb, Map<String, Object> params, String sql, int offset, int limit) {
        String orderLimitWrapperStart;
        String orderLimitWrapperEnd;
        if (limit > 0) {
            orderLimitWrapperStart = "select * from (select t.*, rownum-1 as rn from (";
            orderLimitWrapperEnd = ") t) t where rn >= :offset and rownum <= :limit order by rn";
            params.put("offset", offset);
            params.put("limit", limit);
        } else {
            orderLimitWrapperStart = "";
            orderLimitWrapperEnd = "";
        }

        sb.append(orderLimitWrapperStart).append(sql).append(orderLimitWrapperEnd);
    }

    protected <T> T selectOne(String sql, Map<String, Object> params, RowMapper<T> mapper) {
        List<T> items = jdbcTemplate.query(sql, params, mapper);
        return items.isEmpty() ? null : items.get(0);
    }


    public static String nextval(String seq) {
        return seq + ".nextval";
    }

    /**
     * Метод для корректного чтения значений NULL из БД
     *
     * @param rs        - ResultSet
     * @param fieldName - имя поля в таблице
     * @param className - к какому типу нужно преобразовать значение поля
     * @return
     * @throws SQLException
     */
    protected static <T> T getField(ResultSet rs, String fieldName, Class<T> className) throws SQLException {
        T obj = rs.getObject(fieldName.trim(), className);
        if (rs.wasNull()) {
            obj = null;
        }
        return obj;
    }

    protected static Map<String, Object> describeBean(Object object) {
        try {
            return PropertyUtils.describe(object);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOGGER.error("failed to describe bean", e);
            return null;
        }
    }


    protected static class MapCountResultSetExtractor<T> implements ResultSetExtractor<Map<T, Integer>> {
        private final String idField;
        private final String countField;
        private final Class<T> idClass;

        public MapCountResultSetExtractor(String idField, String countField, Class<T> idClass) {
            this.idField = idField;
            this.countField = countField;
            this.idClass = idClass;
        }

        @Override
        public Map<T, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<T, Integer> result = new LinkedHashMap<>();

            while (rs.next()) {
                int count = rs.getInt(countField);
                result.compute(rs.getObject(idField, idClass), (k, v) -> (v == null ? 0 : v) + count);
            }

            return result;
        }
    }

}
