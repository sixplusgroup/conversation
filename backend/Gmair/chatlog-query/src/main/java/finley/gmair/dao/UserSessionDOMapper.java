package finley.gmair.dao;

import finley.gmair.model.chatlog.UserSession;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserSessionDOMapper {
    @Insert("insert into usr_session_jd(original_session_id, usr_id, waiter_id, product_id,timestamp)\n" +
            "        VALUES (#{session.originalSessionId}, #{session.userId}, #{session.waiterId}, #{session.productId}, #{session.timestamp})")
//    @Options(useGeneratedKeys = true, keyProperty = "session_id")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "session.id", before = false, resultType = Integer.class)
    void insertUserSession(@Param("session") UserSession session);
}
