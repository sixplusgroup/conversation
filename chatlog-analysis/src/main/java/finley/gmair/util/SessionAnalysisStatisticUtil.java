package finley.gmair.util;

import finley.gmair.dto.chatlog.KafkaSession;
import finley.gmair.enums.chatlog.SentimentLabel;
import finley.gmair.model.chatlog.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SessionAnalysisStatisticUtil {

    private final double EXTREME_NEGATIVE_SCORE_THRESHOLD = -3.5;

    public void refreshSessionStatistics(KafkaSession session, List<Message> messages) {
        int waiterNegativeCount = session.getWaiterNegativeCount();
        int customerNegativeCount = session.getCustomerExtremeNegativeCount();
        double customerScoreSum = session.getSumOfCustomerScore();
        for (Message message : messages) {
            if (message.isFromWaiter()) {
                if (message.getLabel().equals(SentimentLabel.NEGATIVE)) {
                    waiterNegativeCount += 1;
                }
            } else {
                if (message.getScore() < EXTREME_NEGATIVE_SCORE_THRESHOLD) {
                    customerNegativeCount += 1;
                }
                customerScoreSum += message.getScore();
            }
        }
        double average = customerScoreSum / messages.size();
        session.setCustomerAverageScore(average)
                .setCustomerExtremeNegativeCount(customerNegativeCount)
                .setWaiterNegativeCount(waiterNegativeCount);
        System.out.println("------------------------!!!!!!!!!!!!!!!!!!!!!!-----------------------------");
        System.out.println(session);
    }

}
