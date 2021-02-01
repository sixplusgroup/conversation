package finley.gmair.service;

import finley.gmair.exception.MqttBusinessException;
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
     * 保存机器型号
     *
     * @param name        型号标识名称的英文字符串
     * @param description 机器型号的描述说明
     * @return 影响条数
     * @throws MqttBusinessException 异常
     */
    int saveModel(String name, String description) throws MqttBusinessException;

    /**
     * 根据型号id查询不包含其行为的型号信息
     *
     * @param modelId 型号id
     * @return 型号
     */
    Model queryOneWithoutAction(String modelId);

    /**
     * 模糊查询，查询不包含其行为的型号信息
     *
     * @param name 型号标识名称的英文字符串
     * @return 型号列表
     */
    List<Model> queryModelsWithoutActionByName(String name);

    /**
     * 新增型号与行为的对应关系
     *
     * @param modelId  型号id
     * @param actionId 行为id
     * @return 新增条数
     * @throws MqttBusinessException 异常
     */
    int insertModelActionRelation(String modelId, String actionId) throws MqttBusinessException;

    /**
     * 查询所有型号，包含其行为
     *
     * @return 型号列表
     */
    List<Model> queryModelsWithAction();

    /**
     * 删除型号
     *
     * @param modelId 型号id
     * @return 删除行数
     * @throws MqttBusinessException 异常
     */
    int deleteModel(String modelId) throws MqttBusinessException;
}
