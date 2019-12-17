package finley.gmair.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: MachinePool
 * @Description: TODO
 * @Author fan
 * @Date 2019/12/17 7:56 PM
 */
public class MachinePool {
    private static ExecutorService MachinePool = new ThreadPoolExecutor(2, 3, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public static ExecutorService getMachinePool() {
        return MachinePool;
    }
}
