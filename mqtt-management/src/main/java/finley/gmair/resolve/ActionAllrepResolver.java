package finley.gmair.resolve;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.datastructrue.LimitQueue;
import finley.gmair.model.machine.v3.MachineStatusV3;
import finley.gmair.pool.CorePool;
import finley.gmair.repo.MachineStatusV3Repository;
import finley.gmair.service.RedisService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ALLREP行为的处理器
 *
 * @author lycheeshell
 * @date 2020/12/18 17:16
 */
@PropertySource({"classpath:mqtt.properties"})
@Component
public class ActionAllrepResolver implements ActionResolver, InitializingBean {

    @Value("${replica}")
    private boolean isReplica;

    @Resource
    private RedisService redisService;

    @Resource
    private MachineStatusV3Repository repository;

    @Override
    public void afterPropertiesSet() {
        ActionResolverFactory.register("ALLREP", this);
    }

    /**
     * 处理行为
     *
     * @param topic mqtt主题topic，topic的第一个"/"已经删除
     * @param json  mqtt消息内容payload的json
     */
    @Override
    public void resolve(String topic, JSONObject json) {
        String[] topicArray = topic.split("/");
        String machineId = topicArray[2];
        if (json.containsKey("power") && json.getIntValue("power") == 0) {
            json.replace("speed", 0);
        }
        //写入内存缓存的数据使用common模块中的结构
        finley.gmair.model.machine.v3.MachineStatusV3 status = new finley.gmair.model.machine.v3.MachineStatusV3(machineId, json);
        LimitQueue<MachineStatusV3> queue;
        if (redisService.exists(machineId)) {
            queue = (LimitQueue) redisService.get(machineId);
            queue.offer(status);
        } else {
            queue = new LimitQueue<>(120);
            queue.offer(status);
        }
        redisService.set(machineId, queue, (long) 120);
        CorePool.getComPool().execute(() -> {
            if (!isReplica) {
                //写入mongodb的使用mongo-common中的结构
                finley.gmair.model.machine.MachineStatusV3 mongo = new finley.gmair.model.machine.MachineStatusV3(machineId, json);
                repository.save(mongo);
            }
        });
    }

}
