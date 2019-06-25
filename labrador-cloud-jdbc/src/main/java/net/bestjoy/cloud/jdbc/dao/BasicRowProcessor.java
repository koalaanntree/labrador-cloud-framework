package net.bestjoy.cloud.jdbc.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * BasicRowProcessor
 *
 * @author lidj
 * @date 2018-01-29
 */
public class BasicRowProcessor {
    private static final BeanProcessor defaultConvert = new BeanProcessor();

    private static final BasicRowProcessor instance = new BasicRowProcessor();

    private final BeanProcessor convert;

    public BasicRowProcessor() {
        this(defaultConvert);
    }

    public BasicRowProcessor(BeanProcessor convert) {
        super();
        this.convert = convert;
    }

    public Object[] toArray(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        Object[] result = new Object[cols];

        for (int i = 0; i < cols; i++) {
            result[i] = rs.getObject(i + 1);
        }

        return result;
    }

    public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
        return this.convert.toBean(rs, type);
    }

    public <T> List<T> toBeanList(ResultSet rs, Class<T> type)
            throws SQLException {
        return this.convert.toBeanList(rs, type);
    }


    public Map<String, Object> toMap(ResultSet rs) throws SQLException {
        Map<String, Object> result = new CaseInsensitiveHashMap();
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        for (int i = 1; i <= cols; i++) {
            result.put(rsmd.getColumnName(i), rs.getObject(i));
        }

        return result;
    }

    private static class CaseInsensitiveHashMap extends HashMap<String, Object> {
        private final Map<String, String> lowerCaseMap = new HashMap<String, String>();

        private static final long serialVersionUID = -2848100435296897392L;

        @Override
        public boolean containsKey(Object key) {
            Object realKey = lowerCaseMap.get(key.toString().toLowerCase(
                    Locale.ENGLISH));
            return super.containsKey(realKey);
        }

        @Override
        public Object get(Object key) {
            Object realKey = lowerCaseMap.get(key.toString().toLowerCase(
                    Locale.ENGLISH));
            return super.get(realKey);
        }

        @Override
        public Object put(String key, Object value) {

            Object oldKey = lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH),
                    key);
            Object oldValue = super.remove(oldKey);
            super.put(key, value);
            return oldValue;
        }

        @Override
        public void putAll(Map<? extends String, ?> m) {
            for (Entry<? extends String, ?> entry : m.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                this.put(key, value);
            }
        }

        @Override
        public Object remove(Object key) {
            Object realKey = lowerCaseMap.remove(key.toString().toLowerCase(
                    Locale.ENGLISH));
            return super.remove(realKey);
        }
    }
}
