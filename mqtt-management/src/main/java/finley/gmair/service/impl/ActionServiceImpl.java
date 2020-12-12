package finley.gmair.service.impl;

import finley.gmair.dao.ActionDao;
import finley.gmair.exception.MqttBusinessException;
import finley.gmair.model.mqttManagement.Action;
import finley.gmair.service.ActionService;
import finley.gmair.util.IDGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lycheeshell
 * @date 2020/12/9 01:28
 */
@Service
public class ActionServiceImpl implements ActionService {

    @Resource
    private ActionDao actionDao;

    /**
     * 新增行为
     *
     * @param name        行为标识名称的英文字符串
     * @param description 行为描述说明
     * @return 新增条数
     * @throws MqttBusinessException 异常
     */
    @Override
    public int saveAction(String name, String description) throws MqttBusinessException {
        //检查该行为是否已经存在
        Action queryAction = new Action();
        queryAction.setName(name);
        List<Action> existActions = actionDao.queryActionsWithoutAttribute(queryAction);
        if (existActions != null && existActions.size() > 1) {
            throw new MqttBusinessException("该名称的行为数量超过一个");
        }

        int affectedLines;
        Action action = new Action();
        if (null == existActions || existActions.size() == 0) {
            //不存在则新增行为
            action.setActionId(IDGenerator.generate("ACTI"));
            action.setName(name);
            action.setDescription(description);
            affectedLines = actionDao.insertAction(action);
        } else {
            //存在则更新行为
            action.setActionId(existActions.get(0).getActionId());
            action.setDescription(description);
            affectedLines = actionDao.updateAction(action);
        }

        return affectedLines;
    }

    /**
     * 根据行为id查询不包含其属性的行为信息
     *
     * @param actionId 行为id
     * @return 行为
     */
    @Override
    public Action queryOneWithoutAttribute(String actionId) {
        return actionDao.queryOneWithoutAttribute(actionId);
    }

    /**
     * 查询不包含其属性的行为信息
     *
     * @param name 行为标识名称的英文字符串
     * @return 行为列表
     */
    @Override
    public List<Action> queryActionsWithoutAttribute(String name) {
        Action queryAction = new Action();
        queryAction.setName(name);
        return actionDao.queryActionsWithoutAttribute(queryAction);
    }

    /**
     * 新增行为与属性的对应关系
     *
     * @param actionId    行为id
     * @param attributeId 属性id
     * @return 新增条数
     */
    @Override
    public int insertActionAttributeRelation(String actionId, String attributeId) {
        return actionDao.insertActionAttributeRelation(actionId, attributeId);
    }

    /**
     * 查询所有行为，包含其属性
     *
     * @return 行为列表
     */
    @Override
    public List<Action> queryActionsWithAttribute() {
        return actionDao.queryActionsWithAttribute();
    }

    /**
     * 根据型号id查询该型号所有的行为
     *
     * @param modelId 型号id
     * @return 行为列表
     */
    @Override
    public List<Action> queryActionsByModel(String modelId) {
        return actionDao.queryActionsByModel(modelId);
    }
}
