package finley.gmair.pool;

import java.util.concurrent.*;

/**
 * @ClassName: InstallPool
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/15 5:15 PM
 */
public class InstallPool {
    private static ExecutorService logPool = new ThreadPoolExecutor(3, 5, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public static ExecutorService getLogExecutor() {
        return logPool;
    }

}
