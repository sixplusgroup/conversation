package finley.gmair.mybatis.handler;

import finley.gmair.model.installation.MemberRole;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberRoleHandler extends BaseTypeHandler<MemberRole> {
    private Class<MemberRole> role;

    private final MemberRole[] enums;

    public MemberRoleHandler(Class<MemberRole> role) {
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
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, MemberRole role, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, role.getValue());
    }

    @Override
    public MemberRole getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        //根据数据库存储类型决定获取类型，Assign数据库中存放int类型
        int index = resultSet.getInt(columnName);
        if (resultSet.wasNull()) {
            return null;
        } else {
            //根据数据库中的code值，定位EnumStatus子类
            return locateEnumRole(index);
        }
    }

    @Override
    public MemberRole getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        //根据数据库存储类型决定获取类型,Assign数据库中存放int类型
        int index = resultSet.getInt(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        } else {
            //根据数据库中的code值,定位EnumStatus子类
            return locateEnumRole(index);
        }
    }

    @Override
    public MemberRole getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        int index = callableStatement.getInt(columnIndex);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnumRole(index);
        }
    }

    /*
     * 枚举类型转换，由于构造函数获取了枚举的子类enums，让遍历更加高效快捷
     * @param code 数据库中存储的自定义code属性
     * @return code对应的枚举类
     */
    private MemberRole locateEnumRole(int code) {
        for (MemberRole role : enums) {
            if (role.getValue() == (Integer.valueOf(code))) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown enum type：" + code + ",please check " + role.getSimpleName());
    }
}
