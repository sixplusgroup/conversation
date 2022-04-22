package finley.gmair.model.chatlog;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserSession {
    int userId;
    Integer id;
    int waiterId;
    String productId;
    String originalSessionId;
    List<Message> messageList;
    long timestamp;
    // 用户情绪评分平均值
    double customerAverageScore;
    // 用户情绪评分低于-4的消息条数
    int customerExtremeNegativeCount;
    // 客服情绪标签为negative的消息条数
    int waiterNegativeCount;


}
