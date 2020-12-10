package finley.gmair.dao;

import finley.gmair.model.mqttManagement.Attribute;
import org.apache.ibatis.annotations.Mapper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 消息行为的属性的数据操作
 *
 * @author lycheeshell
 * @date 2020/12/10 17:12
 */
@Mapper
public interface AttributeDao {

    /**
     * 新增属性
     *
     * @param attribute 属性信息
     * @return 新增条数
     */
    int insertAttribute(Attribute attribute);

    /**
     * 根据属性id查询属性信息
     *
     * @param attributeId 属性id
     * @return 属性
     */
    Attribute queryOne(@NotNull String attributeId);

    /**
     * 查询属性信息
     *
     * @param attribute 属性的查询条件
     * @return 属性列表
     */
    List<Attribute> queryAttributes(Attribute attribute);

    /**
     * 根据行为id查询该行为所有的属性
     *
     * @param actionId 行为id
     * @return 属性列表
     */
    List<Attribute> queryAttributesByAction(@NotNull String actionId);

}
