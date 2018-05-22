package finley.gmair.pool;

import java.util.concurrent.*;

public class CorePool {
    private static ExecutorService logPool = new ThreadPoolExecutor(3, 5,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    private static ExecutorService comPool = new ThreadPoolExecutor(3, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    public static ExecutorService getLogExecutor() {
        return logPool;
    }

    public static ExecutorService getComExecutor() {
        return comPool;
    }
}
