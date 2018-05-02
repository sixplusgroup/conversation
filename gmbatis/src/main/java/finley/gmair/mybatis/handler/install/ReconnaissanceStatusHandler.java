package finley.gmair.mybatis.handler.install;

import finley.gmair.model.installation.ReconnaissanceStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReconnaissanceStatusHandler extends BaseTypeHandler<ReconnaissanceStatus> {
    private Class<ReconnaissanceStatus> status;

    private final ReconnaissanceStatus[] enums;


    public ReconnaissanceStatusHandler(Class<ReconnaissanceStatus> status) {
        if (status == null) {
            throw new IllegalArgumentException("Status argument cannot be null");
        }
        this.status = status;
        this.enums = status.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(new StringBuffer(status.getSimpleName()).append(" does not represent an enum type").toString());
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ReconnaissanceStatus reconnaissanceStatus, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, reconnaissanceStatus.getValue());
    }

    @Override
    public ReconnaissanceStatus getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(i);
        }
    }

    @Override
    public ReconnaissanceStatus getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public ReconnaissanceStatus getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    private ReconnaissanceStatus locateEnumStatus(int code) {
        for (ReconnaissanceStatus status : enums) {
            if (status.getValue() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown enum type：" + code + ",please check " + status.getSimpleName());
    }
}
