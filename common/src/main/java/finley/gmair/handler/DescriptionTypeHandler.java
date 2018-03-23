package finley.gmair.handler;

import finley.gmair.model.wechat.DescriptionType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DescriptionTypeHandler extends BaseTypeHandler<DescriptionType>{
    private Class<DescriptionType> type;

    private final DescriptionType[] enums;

    private Logger logger = LoggerFactory.getLogger(DescriptionTypeHandler.class);

    public DescriptionTypeHandler(Class<DescriptionType> type) {
        if (type == null) {
            throw new IllegalArgumentException("参数status不能为空");
        }
        this.type = type;
        enums = type.getEnumConstants();
        if (enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + "不是一个枚举类型");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DescriptionType type, JdbcType jdbcType)
            throws SQLException {
        ps.setInt(i, type.getCode());
    }

    @Override
    public DescriptionType getNullableResult(ResultSet rs, String c) throws SQLException {
        int i = rs.getInt(c);
        if (rs.wasNull()) {
            return null;
        } else {
            return locateEnumType(i);
        }
    }

    @Override
    public DescriptionType getNullableResult(ResultSet rs, int i) throws SQLException {
        int index = rs.getInt(i);
        if (rs.wasNull()) {
            return null;
        } else {
            return locateEnumType(index);
        }
    }

    @Override
    public DescriptionType getNullableResult(CallableStatement cs, int i) throws SQLException {
        int index = cs.getInt(i);
        if (cs.wasNull()) {
            return null;
        } else {
            return locateEnumType(index);
        }
    }

    private DescriptionType locateEnumType(int code) {
        for (DescriptionType type : enums) {
            if (type.getCode() == (Integer.valueOf(code))) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的枚举类型: " + code + ", 请核对" + this.type.getSimpleName());
    }
}