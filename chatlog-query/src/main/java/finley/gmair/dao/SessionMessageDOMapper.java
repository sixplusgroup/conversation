package finley.gmair.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface SessionMessageDOMapper {
    Integer insertSessionMessage(@Param("sid") int sessionId, @Param("content") String content,
                             @Param("isFromWaiter") boolean isFromWaiter, @Param("timestamp") long timestamp);
}
