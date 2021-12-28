package finley.gmair.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-27 17:10
 * @description ：
 */

//todo：线程池优化
public class OrderCentrePool {
    private static ExecutorService OrderNewPool = new ThreadPoolExecutor(2 * Runtime.getRuntime().availableProcessors(),
            2 * Runtime.getRuntime().availableProcessors(),
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    public static ExecutorService getOrderCentrePool() {
        return OrderNewPool;
    }

    private OrderCentrePool() { }
}
