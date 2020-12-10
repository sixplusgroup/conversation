package finley.gmair.service;

import finley.gmair.model.mqttManagement.Action;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息包含的行为的服务
 *
 * @author lycheeshell
 * @date 2020/12/9 01:25
 */
public interface ActionService {

    /**
     * 保存行为
     *
     * @param name        行为标识名称的英文字符串
     * @param description 行为描述说明
     * @return 新增条数
     * @throws Exception 异常
     */
    int saveAction(String name, String description) throws Exception;

    /**
     * 根据行为id查询不包含其属性的行为信息
     *
     * @param actionId 行为id
     * @return 行为
     */
    Action queryOneWithoutAttribute(String actionId);

    /**
     * 查询不包含其属性的行为信息
     *
     * @param name 行为标识名称的英文字符串
     * @return 行为列表
     */
    List<Action> queryActionsWithoutAttribute(String name);

    /**
     * 新增行为与属性的对应关系
     *
     * @param actionId    行为id
     * @param attributeId 属性id
     * @return 新增条数
     */
    int insertActionAttributeRelation(String actionId, String attributeId);

    /**
     * 查询所有行为，包含其属性
     *
     * @return 行为列表
     */
    List<Action> queryActionsWithAttribute();

    /**
     * 根据型号id查询该型号所有的行为
     *
     * @param modelId 型号id
     * @return 行为列表
     */
    List<Action> queryActionsByModel(String modelId);

}
