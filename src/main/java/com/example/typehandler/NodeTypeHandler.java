package com.example.typehandler;

import com.example.domain.NodeStatusEnum;
import com.example.domain.NodeTypeEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NodeTypeHandler extends BaseTypeHandler<NodeTypeEnum> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, NodeTypeEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.name());
    }

    @Override
    public NodeTypeEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return NodeTypeEnum.valueOf(rs.getString(columnName));
    }

    @Override
    public NodeTypeEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return NodeTypeEnum.valueOf(rs.getString(columnIndex));
    }

    @Override
    public NodeTypeEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return NodeTypeEnum.valueOf(cs.getString(columnIndex));
    }
}
