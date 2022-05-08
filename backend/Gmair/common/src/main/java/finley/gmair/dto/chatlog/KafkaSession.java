package finley.gmair.dto.chatlog;

import finley.gmair.model.chatlog.Message;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class KafkaSession implements Serializable {
    int sessionId;
    List<Message> messages;
    int messageNum;
    // 用户情绪评分平均值
    double customerAverageScore;
    // 用户情绪评分和，暂存redis有记录的部分数据，传递给bert计算总值
    double sumOfCustomerScore;
    // 用户情绪评分低于极端阈值的消息条数
    int customerExtremeNegativeCount;
    // 客服情绪标签为negative的消息条数
    int waiterNegativeCount;
}
