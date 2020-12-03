package finley.gmair.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lycheeshell
 * @date 2020/12/3 15:10
 */
public class CorePool {

    private static final ExecutorService LOG_POOL = new ThreadPoolExecutor(3, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    private static final ExecutorService COM_POOL = new ThreadPoolExecutor(3, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    private static final ExecutorService HANDLE_POOL = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    private static final ExecutorService SURPLUS_POOL = new ThreadPoolExecutor(4, 8, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    public static ExecutorService getLogPool() {
        return LOG_POOL;
    }

    public static ExecutorService getComPool() {
        return COM_POOL;
    }

    public static ExecutorService getHandlePool() {
        return HANDLE_POOL;
    }

    public static ExecutorService getSurplusPool() {
        return SURPLUS_POOL;
    }
}

