package finley.gmair.service.holder;

import finley.gmair.service.strategy.OperationStrategy;
import finley.gmair.util.tmall.TmallDeviceTypeEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 存放设备及其对应的操作策略
 */
@Component
public class OperationStrategyHolder {

    public static Map<TmallDeviceTypeEnum, OperationStrategy> operationStrategyMap = new HashMap<>();

    public OperationStrategy getByDeviceType(TmallDeviceTypeEnum operation) {
        return operationStrategyMap.get(operation);
    }

}
