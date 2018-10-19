package finley.gmair.mybatis.handler;

import finley.gmair.model.bill.BillStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillStatusHandler extends BaseTypeHandler<BillStatus> {
    private Class<BillStatus> status;

    private final BillStatus[] enums;

    public BillStatusHandler(Class<BillStatus> status) {
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
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, BillStatus status, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, status.getValue());
    }

    @Override
    public BillStatus getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        //根据数据库存储类型决定获取类型，Assign数据库中存放int类型
        int index = resultSet.getInt(columnName);
        if (resultSet.wasNull()) {
            return null;
        } else {
            //根据数据库中的code值，定位EnumStatus子类
            return locateEnumStatus(index);
        }
    }

    @Override
    public BillStatus getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        //根据数据库存储类型决定获取类型,Assign数据库中存放int类型
        int index = resultSet.getInt(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        } else {
            //根据数据库中的code值,定位EnumStatus子类
            return locateEnumStatus(index);
        }
    }

    @Override
    public BillStatus getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
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
    private BillStatus locateEnumStatus(int code) {
        for (BillStatus status : enums) {
            if (status.getValue() == (Integer.valueOf(code))) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown enum type：" + code + ",please check " + status.getSimpleName());
    }
}
