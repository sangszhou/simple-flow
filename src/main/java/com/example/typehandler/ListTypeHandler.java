package com.example.typehandler;

import com.example.util.JsonHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * description: ListTypeHandler
 *
 */
@Slf4j
public class ListTypeHandler extends BaseTypeHandler<List<Object>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Object> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, serialize(parameter));
    }

    @Override
    public List<Object> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return deserialize(rs.getString(columnName));
    }

    @Override
    public List<Object> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return deserialize(rs.getString(columnIndex));
    }

    @Override
    public List<Object> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return deserialize(cs.getString(columnIndex));
    }

    private static String serialize(List<Object> parameter) {
        String serializedMap = null;
        try {
            serializedMap = JsonHelper.getMapper().writeValueAsString(parameter);
        } catch (Exception e) {
            log.error("序列化失败", e);
        }
        return serializedMap;
    }

    private static List<Object> deserialize(String data) {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        try {
            return JsonHelper.getMapper().readValue(data, new TypeReference<List<Object>>() {
            });
        } catch (Exception e) {
            log.error("反序列化失败", e);
        }
        return null;
    }

}
