package finley.gmair.mybatis.handler;

import finley.gmair.model.goods.CommodityType;
import finley.gmair.model.installation.AssignStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommodityTypeHandler extends BaseTypeHandler<CommodityType> {
    private Class<CommodityType> status;

    private final CommodityType[] enums;

    public CommodityTypeHandler(Class<CommodityType> status) {
        if (status == null) {
            throw new IllegalArgumentException("assign argument cannot be null");
        }
        this.status = status;
        this.enums = status.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(new StringBuffer(status.getSimpleName()).append(" does not represent an enum type.").toString());
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, CommodityType commodityType, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, commodityType.getValue());
    }

    @Override
    public CommodityType getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        int index = resultSet.getInt(columnName);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public CommodityType getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        int index = resultSet.getInt(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public CommodityType getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
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
    private CommodityType locateEnumStatus(int code) {
        for (CommodityType status : enums) {
            if (status.getValue() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown enum type：" + code + ",please check " + status.getSimpleName());
    }
}
