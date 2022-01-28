package finley.gmair.dao;

import finley.gmair.model.chatlog.UserSession;
import org.apache.ibatis.annotations.Param;

public interface UserSessionDOMapper {
    Integer insertUserSession(@Param("sid") String originalSessionId, @Param("uid") int userId,
                              @Param("wid") int waiterId, @Param("pid") String productId);
}
