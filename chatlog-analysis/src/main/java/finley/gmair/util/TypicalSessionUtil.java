package finley.gmair.util;

import finley.gmair.dto.chatlog.KafkaSession;
import org.springframework.stereotype.Component;

@Component
public class TypicalSessionUtil {

    private static final double CUSTOMER_UNSATISFIED_AVERAGE = -2;

    private static final double CUSTOMER_SATISFIED_AVERAGE = 5;

    private static final int CUSTOMER_EXTREME_NEGATIVE_COUNT = 5;

    private static final int WAITER_NEGATIVE_COUNT = 3;

    public static boolean isTypicalSession(KafkaSession session) {
        boolean customerUnsatisfied = session.getCustomerAverageScore() <= CUSTOMER_UNSATISFIED_AVERAGE ||
                session.getCustomerExtremeNegativeCount() > CUSTOMER_EXTREME_NEGATIVE_COUNT;
        boolean customerSatisfied = !customerUnsatisfied &&
                session.getCustomerAverageScore() > CUSTOMER_SATISFIED_AVERAGE;
        boolean waiterImpatient = session.getWaiterNegativeCount() >= WAITER_NEGATIVE_COUNT;

        return customerSatisfied||customerUnsatisfied||waiterImpatient;
    }

}
