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
     * 保存机器型号
     *
     * @param name        型号标识名称的英文字符串
     * @param description 机器型号的描述说明
     * @return 新增条数
     * @throws Exception 异常
     */
    int saveModel(String name, String description) throws Exception;

    /**
     * 根据型号id查询不包含其行为的型号信息
     *
     * @param modelId 型号id
     * @return 型号
     */
    Model queryOneWithoutAction(String modelId);

    /**
     * 查询不包含其行为的型号信息
     *
     * @param name 型号标识名称的英文字符串
     * @return 型号列表
     */
    List<Model> queryModelsWithoutAction(String name);

    /**
     * 新增型号与行为的对应关系
     *
     * @param modelId  型号id
     * @param actionId 行为id
     * @return 新增条数
     */
    int insertModelActionRelation(String modelId, String actionId);

    /**
     * 查询所有型号，包含其行为
     *
     * @return 型号列表
     */
    List<Model> queryModelsWithAction();

}
