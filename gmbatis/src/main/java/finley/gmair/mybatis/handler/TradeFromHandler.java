package finley.gmair.mybatis.handler;

import finley.gmair.model.ordernew.TradeFrom;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TradeFromHandler extends BaseTypeHandler<TradeFrom> {
    private Class<TradeFrom> from;

    private final TradeFrom[] enums;

    public TradeFromHandler(Class<TradeFrom> from) {
        if (from == null) {
            throw new IllegalArgumentException("Status argument cannot be null");
        }
        this.from = from;
        this.enums = from.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(from.getSimpleName() + " does not represent an enum type.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, TradeFrom from, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, from.getValue());
    }

    @Override
    public TradeFrom getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnum(i);
        }
    }

    @Override
    public TradeFrom getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnum(index);
        }
    }

    @Override
    public TradeFrom getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnum(index);
        }
    }

    private TradeFrom locateEnum(int code) {
        for (TradeFrom from : enums) {
            if (from.getValue() == code) {
                return from;
            }
        }
        throw new IllegalArgumentException("Unknown enum type：" + code + ",please check " + from.getSimpleName());
    }
}
