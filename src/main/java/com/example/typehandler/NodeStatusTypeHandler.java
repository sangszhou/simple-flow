package com.example.typehandler;

import com.example.domain.NodeStatusEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class NodeStatusTypeHandler extends BaseTypeHandler<NodeStatusEnum>  {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, NodeStatusEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getName());
    }

    @Override
    public NodeStatusEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return NodeStatusEnum.valueOf(rs.getString(columnName));
    }

    @Override
    public NodeStatusEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return NodeStatusEnum.valueOf(rs.getString(columnIndex));
    }

    @Override
    public NodeStatusEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return NodeStatusEnum.valueOf(cs.getString(columnIndex));
    }
}
