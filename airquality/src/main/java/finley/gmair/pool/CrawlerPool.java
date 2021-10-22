package finley.gmair.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: CrawlerPool
 * @Description: TODO
 * @Author fan
 * @Date 2021/8/18 4:01 PM
 */
public class CrawlerPool {
    private static ExecutorService crawlerPool = new ThreadPoolExecutor(2, 4, 0L, TimeUnit.MICROSECONDS, new LinkedBlockingDeque<>());

    public static ExecutorService getCrawlerPool() {
        return crawlerPool;
    }
}
