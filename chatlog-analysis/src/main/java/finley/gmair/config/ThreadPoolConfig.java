package finley.gmair.config;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;


@Configuration
@Setter
@ConfigurationProperties(
        prefix = "thread-pool"
)
public class ThreadPoolConfig {
    //线程池维护线程的最少数量
    private Integer corePoolSize;
    //允许的空闲时间
    private Integer keepAliveSeconds;
    //线程池维护线程的最大数量
    private Integer maxPoolSize;
    //缓存队列
    private Integer queueCapacity;

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        ThreadPoolExecutor.CallerRunsPolicy callerRunsPolicy = new ThreadPoolExecutor.CallerRunsPolicy();
        //对拒绝task的处理策略
        executor.setRejectedExecutionHandler(callerRunsPolicy);
        executor.initialize();
        return executor;

    }
}
