package ru.dmitartur.webserver.dao.auth;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SimpleBeanRowMapper<T> extends BeanPropertyRowMapper<T> {

    private Map<String, String> remappedFields;
    private Map<String, Function<Object, Object>> translators;

    public SimpleBeanRowMapper(Class<T> clazz) {
        super(clazz);
        setPrimitivesDefaultedForNullValue(true);
    }

    public synchronized void remapField(String dbName, String beanName) {
        Map<String, String> newFields = new HashMap<>();
        if (remappedFields != null) {
            newFields.putAll(remappedFields);
        }
        newFields.put(beanName, dbName.toLowerCase());
        remappedFields = newFields;
        initialize(this.getMappedClass());
    }

    public synchronized void translateField(String field, Function<Object, Object> translator) {
        Map<String, Function<Object, Object>> newTranslators = new HashMap<>();
        if (translators != null) {
            newTranslators.putAll(translators);
        }
        newTranslators.put(field, translator);
        translators = newTranslators;
    }

    @Override
    protected String underscoreName(String name) {
        if (remappedFields != null) {
            String remapped = remappedFields.get(name);
            if (remapped != null) {
                return remapped;
            }
        }

        StringBuilder r = new StringBuilder();

        boolean prevLower = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c) && prevLower) {
                r.append('_');
                prevLower = false;
            } else {
                prevLower = true;
            }
            r.append(Character.toLowerCase(c));
        }

        return r.toString();
    }

    @Override
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        Function<Object, Object> translator = translators == null ? null : (translators.get(pd.getName()));
        if (translator == null) {
            return super.getColumnValue(rs, index, pd);
        } else {
            Object value = JdbcUtils.getResultSetValue(rs, index, Object.class);
            return translator.apply(value);
        }
    }
}
