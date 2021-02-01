package finley.gmair.controller;

import finley.gmair.service.MqttService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * mqtt主题订阅相关交互接口
 *
 * @author lycheeshell
 * @date 2021/2/1 01:31
 */
@CrossOrigin
@RestController
@RequestMapping("/management/mqtt")
public class MqttController {

    @Autowired
    private MqttService mqttService;

    /**
     * 模糊查询，查询属性信息
     *
     * @param name 属性标识名称英文字符串
     * @return 属性列表
     */
    @GetMapping(value = "/queryAttributesByName")
    public ResultData queryAttributesByName(String name) {
        return mqttService.queryAttributesByName(name);
    }

    /**
     * 保存属性
     *
     * @param name        属性标示名称英文字符串
     * @param description 属性描述
     * @param required    该属性是否必须有
     * @return 新增条数
     */
    @PostMapping(value = "/saveAttribute")
    public ResultData saveAttribute(String name, String description, Boolean required) {
        return mqttService.saveAttribute(name, description, required);
    }

    /**
     * 删除属性
     *
     * @param attributeId 属性id
     * @return 删除行数
     */
    @PostMapping(value = "/deleteAttribute")
    public ResultData deleteAttribute(String attributeId) {
        return mqttService.deleteAttribute(attributeId);
    }

    /**
     * 模糊查询，查询不包含其属性的行为信息
     *
     * @param name 行为标识名称的英文字符串
     * @return 行为列表
     */
    @GetMapping(value = "/queryActionsByName")
    public ResultData queryActionsByName(String name) {
        return mqttService.queryActionsByName(name);
    }

    /**
     * 保存行为
     *
     * @param name        行为标识名称的英文字符串
     * @param description 行为描述说明
     * @return 数据库变化条数
     */
    @PostMapping(value = "/saveAction")
    public ResultData saveAction(String name, String description) {
        return mqttService.saveAction(name, description);
    }

    /**
     * 删除行为
     *
     * @param actionId 行为id
     * @return 删除行数
     */
    @PostMapping(value = "/deleteAction")
    public ResultData deleteAction(String actionId) {
        return mqttService.deleteAction(actionId);
    }

    /**
     * 根据行为id查询该行为所有的属性
     *
     * @param actionId 行为id
     * @return 属性列表
     */
    @GetMapping(value = "/queryAttributesByAction")
    public ResultData queryAttributesByAction(String actionId) {
        return mqttService.queryAttributesByAction(actionId);
    }

    /**
     * 新增行为与属性的对应关系
     *
     * @param actionId    行为id
     * @param attributeId 属性id
     * @return 新增条数
     */
    @PostMapping(value = "/addActionAttributeRelation")
    public ResultData addActionAttributeRelation(String actionId, String attributeId) {
        return mqttService.addActionAttributeRelation(actionId, attributeId);
    }

    /**
     * 模糊查询，查询不包含其行为的型号信息
     *
     * @param name 型号标识名称的英文字符串
     * @return 型号列表
     */
    @GetMapping(value = "/queryModelsByName")
    public ResultData queryModelsByName(String name) {
        return mqttService.queryModelsByName(name);
    }

    /**
     * 保存机器型号
     *
     * @param name        型号标识名称的英文字符串
     * @param description 机器型号的描述说明
     * @return 数据库变化条数
     */
    @PostMapping(value = "/saveModel")
    public ResultData saveModel(String name, String description) {
        return mqttService.saveModel(name, description);
    }

    /**
     * 根据型号id查询该型号所有的行为
     *
     * @param modelId 型号id
     * @return 行为列表
     */
    @GetMapping(value = "/queryActionsByModel")
    public ResultData queryActionsByModel(String modelId) {
        return mqttService.queryActionsByModel(modelId);
    }

    /**
     * 新增型号与行为的对应关系
     *
     * @param modelId  型号id
     * @param actionId 行为id
     * @return 新增条数
     */
    @PostMapping(value = "/addModelActionRelation")
    public ResultData addModelActionRelation(String modelId, String actionId) {
        return mqttService.addModelActionRelation(modelId, actionId);
    }

    /**
     * 模糊查询，查询主题列表
     *
     * @param topicDetail 主题格式
     * @return 主题列表
     */
    @GetMapping(value = "/getTopicByDetail")
    public ResultData getTopicByDetail(String topicDetail) {
        return mqttService.getTopicByDetail(topicDetail);
    }

    /**
     * 新增主题
     *
     * @param topicDetail      主题格式
     * @param topicDescription 主题描述
     * @return 新增行数
     */
    @PostMapping(value = "/createTopic")
    public ResultData createTopic(String topicDetail, String topicDescription) {
        return mqttService.createTopic(topicDetail, topicDescription);
    }

    /**
     * 删除主题
     *
     * @param topicId 主题id
     * @return 删除行数
     */
    @PostMapping(value = "/deleteTopic")
    public ResultData deleteTopic(String topicId) {
        return mqttService.deleteTopic(topicId);
    }

}
