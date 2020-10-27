package finley.gmair.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: Bright Chan
 * @date: 2020/10/27 9:25
 * @description: OrderNewPool
 */
public class OrderNewPool {
    private static ExecutorService OrderNewPool = new ThreadPoolExecutor(2, 3,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    public static ExecutorService getOrderNewPool() {
        return OrderNewPool;
    }
}
