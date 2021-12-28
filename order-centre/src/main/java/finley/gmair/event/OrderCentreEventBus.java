package finley.gmair.event;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 16:21
 * @description ：订单事件总线
 */

@Component
public class OrderCentreEventBus extends AbstractSpringEventBus {

    private final AsyncEventBus asyncEventBus = new AsyncEventBus(new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors(),
            0L, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>()));


    @Override
    public void post(IEvent event) {
        asyncEventBus.post(event);
    }

    @Override
    public void addConsumer(IEventConsumer event) {
        asyncEventBus.register(event);
    }

    @Override
    public void removeConsumer(IEventConsumer event) {
        asyncEventBus.unregister(event);
    }
}
