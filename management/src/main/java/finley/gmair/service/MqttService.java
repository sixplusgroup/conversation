package finley.gmair.service;

import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * mqtt-management的服务
 *
 * @author lycheeshell
 * @date 2021/1/31 23:57
 */
@FeignClient(value = "corev3-agent")
public interface MqttService {

    /**
     * 模糊查询，查询属性信息
     *
     * @param name 属性标识名称英文字符串
     * @return 属性列表
     */
    @GetMapping("/mqtt/attribute/queryAttributesByName")
    ResultData queryAttributesByName(@RequestParam("name") String name);

    /**
     * 保存属性
     *
     * @param name        属性标示名称英文字符串
     * @param description 属性描述
     * @param required    该属性是否必须有
     * @return 新增条数
     */
    @PostMapping("/mqtt/attribute/save")
    ResultData saveAttribute(@RequestParam("name") String name, @RequestParam("description") String description, @RequestParam("required") Boolean required);

    /**
     * 删除属性
     *
     * @param attributeId 属性id
     * @return 删除行数
     */
    @PostMapping("/mqtt/attribute/delete")
    ResultData deleteAttribute(@RequestParam("attributeId") String attributeId);

    /**
     * 模糊查询，查询不包含其属性的行为信息
     *
     * @param name 行为标识名称的英文字符串
     * @return 行为列表
     */
    @GetMapping("/mqtt/action/queryActionsByName")
    ResultData queryActionsByName(@RequestParam("name") String name);

    /**
     * 保存行为
     *
     * @param name        行为标识名称的英文字符串
     * @param description 行为描述说明
     * @return 数据库变化条数
     */
    @PostMapping("/mqtt/action/save")
    ResultData saveAction(@RequestParam("name") String name, @RequestParam("description") String description);

    /**
     * 删除行为
     *
     * @param actionId 行为id
     * @return 删除行数
     */
    @PostMapping("/mqtt/action/delete")
    ResultData deleteAction(@RequestParam("actionId") String actionId);

    /**
     * 根据行为id查询该行为所有的属性
     *
     * @param actionId 行为id
     * @return 属性列表
     */
    @GetMapping("/mqtt/attribute/queryAttributesByAction")
    ResultData queryAttributesByAction(@RequestParam("actionId") String actionId);

    /**
     * 新增行为与属性的对应关系
     *
     * @param actionId    行为id
     * @param attributeId 属性id
     * @return 新增条数
     */
    @PostMapping("/mqtt/action/addActionAttribute")
    ResultData addActionAttributeRelation(@RequestParam("actionId") String actionId, @RequestParam("attributeId") String attributeId);

    /**
     * 模糊查询，查询不包含其行为的型号信息
     *
     * @param name 型号标识名称的英文字符串
     * @return 型号列表
     */
    @GetMapping("/mqtt/model/queryModelsByName")
    ResultData queryModelsByName(@RequestParam("name") String name);

    /**
     * 保存机器型号
     *
     * @param name        型号标识名称的英文字符串
     * @param description 机器型号的描述说明
     * @return 数据库变化条数
     */
    @PostMapping("/mqtt/model/save")
    ResultData saveModel(@RequestParam("name") String name, @RequestParam("description") String description);

    /**
     * 删除型号
     *
     * @param modelId 型号id
     * @return 删除行数
     */
    @PostMapping("/mqtt/model/delete")
    ResultData deleteModel(@RequestParam("modelId") String modelId);

    /**
     * 根据型号id查询该型号所有的行为
     *
     * @param modelId 型号id
     * @return 行为列表
     */
    @GetMapping("/mqtt/action/queryActionsByModel")
    ResultData queryActionsByModel(@RequestParam("modelId") String modelId);

    /**
     * 新增型号与行为的对应关系
     *
     * @param modelId  型号id
     * @param actionId 行为id
     * @return 新增条数
     */
    @PostMapping("/mqtt/model/addModelAction")
    ResultData addModelActionRelation(@RequestParam("modelId") String modelId, @RequestParam("actionId") String actionId);

    /**
     * 模糊查询，查询主题列表
     *
     * @param topicDetail 主题格式
     * @return 主题列表
     */
    @GetMapping("/mqtt/topic/getTopicByDetail")
    ResultData getTopicByDetail(@RequestParam("topicDetail") String topicDetail);

    /**
     * 新增主题
     *
     * @param topicDetail      主题格式
     * @param topicDescription 主题描述
     * @return 新增行数
     */
    @PostMapping("/mqtt/topic/create")
    ResultData createTopic(@RequestParam("topicDetail") String topicDetail, @RequestParam("topicDescription") String topicDescription);

    /**
     * 删除主题
     *
     * @param topicId 主题id
     * @return 删除行数
     */
    @PostMapping("/mqtt/topic/delete")
    ResultData deleteTopic(@RequestParam("topicId") String topicId);

    /**
     * 新增固件版本信息
     *
     * @param version 版本号
     * @param link    下载链接
     * @param model   设备型号
     * @return 新增的固件
     */
    @PostMapping("/core/firmware/create")
    ResultData createFirmware(@RequestParam("version") String version, @RequestParam("link") String link, @RequestParam("model") String model);

    /**
     * 查询固件
     *
     * @param version 版本号
     * @param model   设备型号
     * @return 固件列表
     */
    @GetMapping("/core/firmware/query")
    ResultData getFirmware(@RequestParam("version") String version, @RequestParam("version") String model);
}
