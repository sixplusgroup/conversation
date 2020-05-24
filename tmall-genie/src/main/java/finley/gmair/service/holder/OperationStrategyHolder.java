package finley.gmair.service.holder;

import finley.gmair.service.strategy.OperationStrategy;
import finley.gmair.util.tmall.TmallDeviceType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 存放设备及其对应的操作策略
 */
@Component
public class OperationStrategyHolder {

    public static Map<TmallDeviceType, OperationStrategy> operationStrategyMap = new HashMap<>();

    public OperationStrategy getByDeviceType(TmallDeviceType operation) {
        return operationStrategyMap.get(operation);
    }

}
