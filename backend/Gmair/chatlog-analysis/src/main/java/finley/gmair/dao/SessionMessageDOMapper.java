package finley.gmair.dao;

import finley.gmair.model.chatlog.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SessionMessageDOMapper {
    int batchStoreMessagesAnalysisRes(@Param("messages") List<Message> messageList);

}
