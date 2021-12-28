package finley.gmair.event;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 16:18
 * @description ：eventBus顶层接口
 */

public interface IEventBus {
    /**
     * 发布事件
     *
     * @param event 事件实体
     */
    void post(IEvent event);

    /**
     * 添加消费者
     *
     * @param event 消费者对象
     */
    void addConsumer(IEventConsumer event);

    /**
     * 移除消费者
     *
     * @param event 消费者对象
     */
    void removeConsumer(IEventConsumer event);
}
