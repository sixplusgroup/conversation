package finley.gmair.pool;

import java.util.concurrent.*;

public class TimingPool {
    private static ExecutorService timingPool = new ThreadPoolExecutor(3, 5, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());



    public static ExecutorService getTimingExecutor() {
        return timingPool;
    }

}
