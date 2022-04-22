package finley.gmair.dao;

import finley.gmair.dto.chatlog.KafkaSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserSessionDOMapper {
    void storeSessionAnalysisRes(@Param("session") KafkaSession session);
}
