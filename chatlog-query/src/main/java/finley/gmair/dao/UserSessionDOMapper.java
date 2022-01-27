package finley.gmair.dao;

import finley.gmair.model.chatlog.UserSession;
import org.apache.ibatis.annotations.Param;

public interface UserSessionDOMapper {
    void insertUserSession(@Param("uid") int userId, @Param("wid") int waiterId,
                           @Param("sid") int sessionId, @Param("pid") String productId);
}
