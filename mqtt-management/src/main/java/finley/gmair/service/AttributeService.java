package finley.gmair.service;

import finley.gmair.exception.MqttBusinessException;
import finley.gmair.model.mqttManagement.Action;
import finley.gmair.model.mqttManagement.Attribute;

import java.util.List;

/**
 * 消息行为的属性的服务
 *
 * @author lycheeshell
 * @date 2020/12/10 21:25
 */
public interface AttributeService {

    /**
     * 保存属性
     *
     * @param name        属性标示名称英文字符串
     * @param description 属性描述
     * @param required    该属性是否必须有
     * @return 新增条数
     * @throws MqttBusinessException 异常
     */
    int saveAttribute(String name, String description, Boolean required) throws MqttBusinessException;

    /**
     * 根据属性id查询属性信息
     *
     * @param attributeId 属性id
     * @return 属性
     */
    Attribute queryOne(String attributeId);

    /**
     * 模糊查询属性信息
     *
     * @param name 属性标示名称英文字符串
     * @return 属性列表
     */
    List<Attribute> queryAttributesByName(String name);

    /**
     * 根据行为id查询该行为所有的属性
     *
     * @param actionId 行为id
     * @return 属性列表
     */
    List<Attribute> queryAttributesByAction(String actionId);

}
