package finley.gmair.service.impl;

import finley.gmair.dao.AttributeDao;
import finley.gmair.exception.MqttBusinessException;
import finley.gmair.model.mqttManagement.Attribute;
import finley.gmair.service.AttributeService;
import finley.gmair.util.IDGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lycheeshell
 * @date 2020/12/10 22:07
 */
@Service
public class AttributeServiceImpl implements AttributeService {

    @Resource
    private AttributeDao attributeDao;

    /**
     * 保存属性
     *
     * @param name        属性标示名称英文字符串
     * @param description 属性描述
     * @param required    该属性是否必须有
     * @return 新增条数
     * @throws MqttBusinessException 异常
     */
    @Override
    public int saveAttribute(String name, String description, Boolean required) throws MqttBusinessException {
        //检查该属性是否已经存在
        Attribute queryAttribute = new Attribute();
        queryAttribute.setName(name);
        List<Attribute> existAttributes = attributeDao.queryAttributes(queryAttribute);
        if (existAttributes != null && existAttributes.size() > 1) {
            throw new MqttBusinessException("该名称的属性数量超过一个");
        }

        int affectedLines;
        Attribute attribute = new Attribute();
        if (existAttributes == null || existAttributes.size() == 0) {
            //不存在则新增属性
            attribute.setAttributeId(IDGenerator.generate("ATTR"));
            attribute.setName(name);
            attribute.setDescription(description);
            attribute.setRequired(required);
            affectedLines = attributeDao.insertAttribute(attribute);
        } else {
            //存在则更新属性
            attribute.setAttributeId(existAttributes.get(0).getAttributeId());
            attribute.setDescription(description);
            attribute.setRequired(required);
            affectedLines = attributeDao.updateAttribute(attribute);
        }

        return affectedLines;
    }

    /**
     * 根据属性id查询属性信息
     *
     * @param attributeId 属性id
     * @return 属性
     */
    @Override
    public Attribute queryOne(String attributeId) {
        return attributeDao.queryOne(attributeId);
    }

    /**
     * 模糊查询属性信息
     *
     * @param name 属性标识名称英文字符串
     * @return 属性列表
     */
    @Override
    public List<Attribute> queryAttributesByName(String name) {
        return attributeDao.queryAttributesByName(name);
    }

    /**
     * 根据行为id查询该行为所有的属性
     *
     * @param actionId 行为id
     * @return 属性列表
     */
    @Override
    public List<Attribute> queryAttributesByAction(String actionId) {
        return attributeDao.queryAttributesByAction(actionId);
    }

    /**
     * 删除属性
     *
     * @param attributeId 属性id
     * @return 删除行数
     * @throws MqttBusinessException 异常
     */
    @Override
    public int deleteAttribute(String attributeId) throws MqttBusinessException {

        int usedNum = attributeDao.getAttributeUsedNumber(attributeId);

        if (usedNum == 0) {
            return attributeDao.deleteAttribute(attributeId);
        } else {
            throw new MqttBusinessException("该属性已被使用，无法删除");
        }
    }
}
