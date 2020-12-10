package finley.gmair.service.impl;

import finley.gmair.dao.ActionDao;
import finley.gmair.model.mqttManagement.Action;
import finley.gmair.service.ActionService;
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
     * 查询所有行为，包含其属性
     *
     * @return 行为列表
     */
    @Override
    public List<Action> queryActionsWithAttribute() {
        return actionDao.queryActionsWithAttribute();
    }
}
