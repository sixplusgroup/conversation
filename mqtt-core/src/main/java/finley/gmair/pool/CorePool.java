package finley.gmair.pool;

/**
 * @ClassName: CorePool
 * @Description: TODO
 * @Author fan
 * @Date 2019/5/11 5:18 PM
 */

import java.util.concurrent.*;

public class CorePool {
    private static ExecutorService logPool = new ThreadPoolExecutor(3, 5, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    private static ExecutorService comPool = new ThreadPoolExecutor(3, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    private static ExecutorService handlePool = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    private static ExecutorService surplusPool = new ThreadPoolExecutor(4, 8, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    public static ExecutorService getLogPool() {
        return logPool;
    }

    public static ExecutorService getComPool() {
        return comPool;
    }

    public static ExecutorService getHandlePool() {
        return handlePool;
    }

    public static ExecutorService getSurplusPool() {
        return surplusPool;
    }
}

