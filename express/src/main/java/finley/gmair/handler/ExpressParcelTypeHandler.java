package finley.gmair.handler;

import finley.gmair.model.express.ParcelType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpressParcelTypeHandler extends BaseTypeHandler<ParcelType> {

    private Class<ParcelType> type;

    private final ParcelType[] enums;

    public ExpressParcelTypeHandler(Class<ParcelType> type) {
        if (type == null) {
            throw new IllegalArgumentException("Status argument cannot be null");
        }
        this.type = type;
        this.enums = type.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(new StringBuffer(type.getSimpleName()).append(" does not represent an enum type.").toString());
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ParcelType type, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, type.getValue());
    }

    @Override
    public ParcelType getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumType(i);
        }
    }

    @Override
    public ParcelType getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumType(index);
        }
    }

    @Override
    public ParcelType getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnumType(index);
        }
    }

    private ParcelType locateEnumType(int code) {
        for (ParcelType type : enums) {
            if (type.getValue() == (Integer.valueOf(code))) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum type：" + code + ", please check " + type.getSimpleName());
    }
}
