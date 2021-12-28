package finley.gmair.event;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 16:19
 * @description ：eventBus接入Spring,实现自动扫描事件消费者
 */

public abstract class AbstractSpringEventBus implements IEventBus, ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContext.getBeansOfType(IEventConsumer.class).forEach((k, v) -> {
            this.addConsumer(v);
        });
    }
}
