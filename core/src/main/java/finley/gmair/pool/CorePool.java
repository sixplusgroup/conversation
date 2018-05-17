package finley.gmair.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CorePool {
    private static ExecutorService logPool = new ThreadPoolExecutor(3, 5,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public static ExecutorService getLogExecutor() {
        return logPool;
    }
}
