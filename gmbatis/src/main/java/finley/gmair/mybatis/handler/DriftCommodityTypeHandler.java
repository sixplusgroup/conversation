package finley.gmair.mybatis.handler;

import finley.gmair.model.goods.DriftCommodityType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DriftCommodityTypeHandler extends BaseTypeHandler<DriftCommodityType> {
    private Class<DriftCommodityType> type;

    private final DriftCommodityType[] enums;

    public DriftCommodityTypeHandler(Class<DriftCommodityType> type) {
        if (type == null) {
            throw new IllegalArgumentException("assign argument cannot be null");
        }
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(new StringBuffer(type.getSimpleName()).append(" does not represent an enum type.").toString());
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, DriftCommodityType commodityType, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, commodityType.getValue());
    }

    @Override
    public DriftCommodityType getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        int index = resultSet.getInt(columnName);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public DriftCommodityType getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        int index = resultSet.getInt(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public DriftCommodityType getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        int index = callableStatement.getInt(columnIndex);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    /*
     * 枚举类型转换，由于构造函数获取了枚举的子类enums，让遍历更加高效快捷
     * @param code 数据库中存储的自定义code属性
     * @return code对应的枚举类
     */
    private DriftCommodityType locateEnumStatus(int code) {
        for (DriftCommodityType type : enums) {
            if (type.getValue() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum type：" + code + ",please check " + type.getSimpleName());
    }
}
