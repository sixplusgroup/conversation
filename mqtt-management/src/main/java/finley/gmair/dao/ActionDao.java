package finley.gmair.dao;

import finley.gmair.model.mqttManagement.Action;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息包含的行为的数据操作
 *
 * @author lycheeshell
 * @date 2020/12/9 00:26
 */
@Mapper
public interface ActionDao {

    /**
     * 新增行为
     *
     * @param action 行为信息
     * @return 新增条数
     */
    int insertAction(Action action);

    /**
     * 更新行为
     *
     * @param action 行为信息
     * @return 更新条数
     */
    int updateAction(Action action);

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
     * @param action 行为的查询条件
     * @return 行为列表
     */
    List<Action> queryActionsWithoutAttribute(Action action);

    /**
     * 模糊查询，查询不包含其属性的行为信息
     *
     * @param name 行为的查询条件
     * @return 行为列表
     */
    List<Action> queryActionsWithoutAttributeByName(String name);

    /**
     * 新增行为与属性的对应关系
     *
     * @param actionId    行为id
     * @param attributeId 属性id
     * @return 新增条数
     */
    int insertActionAttributeRelation(@Param("actionId") String actionId, @Param("attributeId") String attributeId);

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
