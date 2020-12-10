package finley.gmair.service;

import finley.gmair.model.mqttManagement.Action;

import java.util.List;

/**
 * 消息包含的行为的服务
 *
 * @author lycheeshell
 * @date 2020/12/9 01:25
 */
public interface ActionService {

    /**
     * 查询所有行为，包含其属性
     *
     * @return 行为列表
     */
    List<Action> queryActionsWithAttribute();

}
