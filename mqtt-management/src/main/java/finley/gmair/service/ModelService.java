package finley.gmair.service;

import finley.gmair.model.mqttManagement.Model;

import java.util.List;

/**
 * 机器型号服务
 *
 * @author lycheeshell
 * @date 2020/12/9 01:25
 */
public interface ModelService {

    /**
     * 查询所有型号，包含其行为
     *
     * @return 型号列表
     */
    List<Model> queryModelsWithAction();

}
