package finley.gmair.handler;

import finley.gmair.model.express.ExpressStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpressStatusHandler extends BaseTypeHandler<ExpressStatus> {

    private Class<ExpressStatus> status;

    private final ExpressStatus[] enums;

    public ExpressStatusHandler(Class<ExpressStatus> status) {
        if (status == null) {
            throw new IllegalArgumentException("Status argument cannot be null");
        }
        this.status = status;
        this.enums = status.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(new StringBuffer(status.getSimpleName()).append(" does not represent an enum type.").toString());
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ExpressStatus status, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, status.getCode());
    }

    @Override
    public ExpressStatus getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(i);
        }
    }

    @Override
    public ExpressStatus getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public ExpressStatus getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    private ExpressStatus locateEnumStatus(int code) {
        for (ExpressStatus status : enums) {
            if (status.getCode() == (Integer.valueOf(code))) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown enum type：" + code + ", please check " + status.getSimpleName());
    }
}
