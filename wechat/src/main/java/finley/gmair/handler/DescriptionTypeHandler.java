package finley.gmair.handler;

import finley.gmair.model.wechat.DescriptionType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DescriptionTypeHandler extends BaseTypeHandler<DescriptionType> {
    private Class<DescriptionType> type;

    private final DescriptionType[] enums;

    public DescriptionTypeHandler(Class<DescriptionType> type) {
        if (type == null) {
            throw new IllegalArgumentException("descriptionType argument cannot be null");
        }
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(new StringBuffer(type.getSimpleName()).append(" does not represent an enum type.").toString());
        }
    }
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, DescriptionType type, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, type.getCode());
    }

    @Override
    public DescriptionType getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        //根据数据库存储类型决定获取类型，Assign数据库中存放int类型
        int index = resultSet.getInt(columnName);
        if (resultSet.wasNull()) {
            return null;
        } else {
            //根据数据库中的code值，定位EnumStatus子类
            return locateEnumType(index);
        }
    }

    @Override
    public DescriptionType getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        //根据数据库存储类型决定获取类型,Assign数据库中存放int类型
        int index = resultSet.getInt(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        } else {
            //根据数据库中的code值,定位EnumStatus子类
            return locateEnumType(index);
        }
    }

    @Override
    public DescriptionType getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        int index = callableStatement.getInt(columnIndex);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnumType(index);
        }
    }

    /*
     * 枚举类型转换，由于构造函数获取了枚举的子类enums，让遍历更加高效快捷
     * @param code 数据库中存储的自定义code属性
     * @return code对应的枚举类
     */
    private DescriptionType locateEnumType(int code) {
        for (DescriptionType type : enums) {
            if (type.getCode() == (Integer.valueOf(code))) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum type：" + code + ",please check " + type.getSimpleName());
    }

}