package finley.gmair.dao;

import finley.gmair.model.chatlog.Message;
import finley.gmair.model.chatlog.UserSession;
import org.apache.ibatis.annotations.*;

import java.util.Map;

@Mapper
public interface SessionMessageDOMapper {

    @Insert("insert into session_message_jd(session_id, content, is_from_waiter, timestamp) values\n" +
            "        (#{message.sessionId}, #{message.content}, #{message.isFromWaiter}, #{message.timestamp})")
//    @Options(useGeneratedKeys = true, keyProperty = "session_id")
    @SelectKey(statement = "select last_insert_id()", keyProperty = "message.id", before = false, resultType = Integer.class)
    void insertSessionMessage(@Param("message") Message message);
}
