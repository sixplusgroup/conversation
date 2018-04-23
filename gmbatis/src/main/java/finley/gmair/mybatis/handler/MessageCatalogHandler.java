package finley.gmair.mybatis.handler;

import finley.gmair.model.message.MessageCatalog;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageCatalogHandler extends BaseTypeHandler<MessageCatalog> {

    private Class<MessageCatalog> catalog;

    private final MessageCatalog[] enums;

    public MessageCatalogHandler(Class<MessageCatalog> catalog) {
        if (catalog == null) {
            throw new IllegalArgumentException("Catalog argument cannot be null");
        }
        this.catalog = catalog;
        this.enums = catalog.getEnumConstants();
        if (this.enums == null) {
            throw new IllegalArgumentException(new StringBuffer(catalog.getSimpleName()).append(" does not represent an enum type.").toString());
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, MessageCatalog catalog, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, catalog.getCode());
    }

    @Override
    public MessageCatalog getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int i = resultSet.getInt(s);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(i);
        }
    }

    @Override
    public MessageCatalog getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int index = resultSet.getInt(i);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    @Override
    public MessageCatalog getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int index = callableStatement.getInt(i);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return locateEnumStatus(index);
        }
    }

    private MessageCatalog locateEnumStatus(int code) {
        for (MessageCatalog status : enums) {
            if (status.getCode() == (Integer.valueOf(code))) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown enum type：" + code + ",please check " + catalog.getSimpleName());
    }
}
