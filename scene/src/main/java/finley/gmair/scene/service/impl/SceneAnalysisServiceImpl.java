package finley.gmair.scene.service.impl;

import com.google.common.collect.Maps;
import finley.gmair.scene.dao.repository.SceneOperationRepository;
import finley.gmair.scene.entity.SceneOperationCommand;
import finley.gmair.scene.entity.SceneOperationDO;
import finley.gmair.scene.service.SceneAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author : Lyy
 * @create : 2021-01-14 15:32
 * @description 场景数据分析接口
 **/
@Slf4j
@Service
public class SceneAnalysisServiceImpl implements SceneAnalysisService {

    @Resource
    SceneOperationRepository sceneOperationRepository;

    // 所有用户的场景中component分布情况
    @Override
    public void commandComponentDistribution() {
        List<SceneOperationDO> sceneOperationDOS = sceneOperationRepository.findAllByDeletedFalse();
        Map<String, Integer> componentMap = Maps.newHashMap();
        sceneOperationDOS.forEach(sceneOperationDO -> {
            List<SceneOperationCommand> commands = sceneOperationDO.getCommands();
            commands.forEach(command -> {
                componentMap.put(command.getCommandComponent(),
                        componentMap.getOrDefault(command.getCommandComponent(), 0) + 1);
            });
        });
        componentMap.forEach((k, v) -> {
            log.info("component {} appears {} times", k, v);

        });
    }

    @Override
    public void commandComponentDistribution(String consumerId) {
        List<SceneOperationDO> sceneOperationDOS = sceneOperationRepository
                .findAllByConsumerIdAndDeletedFalse(consumerId);
        Map<String, Integer> componentMap = Maps.newHashMap();
        sceneOperationDOS.forEach(sceneOperationDO -> {
            List<SceneOperationCommand> commands = sceneOperationDO.getCommands();
            commands.forEach(command -> {
                componentMap.put(command.getCommandComponent(),
                        componentMap.getOrDefault(command.getCommandComponent(), 0) + 1);
            });
        });
        componentMap.forEach((k, v) -> {
            log.info("component {} appears {} times", k, v);
        });
    }
}
