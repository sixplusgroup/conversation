package finley.gmair.mybatis.handler;

import finley.gmair.model.admin.AdminRole;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AdminRoleHandler extends BaseTypeHandler<AdminRole> {
    private Class<AdminRole> role;

    private final AdminRole[] enums;

    public AdminRoleHandler(Class<AdminRole> role) {
        if (role == null) {
            throw new IllegalArgumentException("role argument cannot be null");
        }
        this.role = role;
        this.enums = role.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(new StringBuffer(role.getSimpleName()).append(" does not represent an enum type.").toString());
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, AdminRole adminRole, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, adminRole.getValue());
    }

    @Override
    public AdminRole getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(i);
        }
    }

    @Override
    public AdminRole getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public AdminRole getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    private AdminRole locateEnumStatus(int code) {
        for (AdminRole role : enums) {
            if (role.getValue() == (Integer.valueOf(code))) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown enum type：" + code + ",please check " + role.getSimpleName());
    }
}
