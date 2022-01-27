package finley.gmair.model.chatlog;

import lombok.Data;

import java.util.List;

@Data
public class UserSession {
    int userId;
    int sessionId;
    int waiterId;
    String productId;
    String analysis;
    List<Message> messageList;

}
