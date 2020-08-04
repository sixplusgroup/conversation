package finley.gmair.service.holder;

import finley.gmair.service.strategy.OperationStrategy;
import finley.gmair.util.tmall.TmallDeviceTypeEnum;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HandlerOperationProcessor implements ApplicationContextAware {

    /**
     * 获取所有的策略实例 加入OperationStrategyHolder的属性中
     *
     * @param applicationContext 应用上下文
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        Map<String, Object> orderStrategyMap = applicationContext.getBeansWithAnnotation(HandlerOperationType.class);
        orderStrategyMap.forEach((k, v) -> {
            Class<?> orderStrategyClass = v.getClass();
//            Class<OperationStrategy> orderStrategyClass = (Class<OperationStrategy>) v.getClass();
            TmallDeviceTypeEnum type = orderStrategyClass.getAnnotation(HandlerOperationType.class).value();
            // 将type作为key,策略实例作为value,加入map中
            OperationStrategyHolder.operationStrategyMap.put(type, (OperationStrategy) v);
        });
    }

}
