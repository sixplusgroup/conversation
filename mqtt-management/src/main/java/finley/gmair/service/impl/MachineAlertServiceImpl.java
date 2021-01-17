package finley.gmair.service.impl;

import finley.gmair.dao.MachineAlertDao;
import finley.gmair.model.mqttManagement.MachineAlert;
import finley.gmair.service.MachineAlertService;
import finley.gmair.util.IDGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 备警报服务
 *
 * @author lycheeshell
 * @date 2020/12/18 16:43
 */
@Service
public class MachineAlertServiceImpl implements MachineAlertService {

    @Resource
    private MachineAlertDao machineAlertDao;

    /**
     * 创建设备警报
     *
     * @param machineId 设备id
     * @param code      警报码
     * @param msg       警报信息
     * @return 新增条数
     */
    @Override
    public MachineAlert createMachineAlert(String machineId, int code, String msg) {
        MachineAlert alert = new MachineAlert(machineId, code, msg);
        alert.setAlertId(IDGenerator.generate("MAI"));
        machineAlertDao.insert(alert);
        return alert;
    }

    /**
     * 更新设备警报
     *
     * @param machineId 设备id
     * @param code      警报码
     * @return 更新条数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateMachineAlert(String machineId, int code) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("machineId", machineId);
        condition.put("alertStatus", false);
        condition.put("alertCode", code);
        List<MachineAlert> machineAlertList = machineAlertDao.query(condition);
        if (machineAlertList == null || machineAlertList.size() == 0) {
            return 0;
        } else if (machineAlertList.size() == 1) {
            condition.clear();
            condition.put("alertStatus", true);
            condition.put("alertId", machineAlertList.get(0).getAlertId());
            return machineAlertDao.updateSingle(condition);
        } else {
            condition.clear();
            condition.put("list", machineAlertList);
            condition.put("alertStatus", true);
            return machineAlertDao.updateBatch(condition);
        }
    }

    /**
     * 查询设备警报
     *
     * @param machineId   设备id
     * @param code        警报码
     * @param alertStatus 警报状态
     * @return 设备警报列表
     */
    @Override
    public List<MachineAlert> queryMachineAlert(String machineId, Integer code, Boolean alertStatus) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        if (StringUtils.isNotEmpty(machineId)) {
            condition.put("machineId", machineId);
        }
        if (code != null) {
            condition.put("alertCode", code);
        }
        if (alertStatus != null)  {
            condition.put("alertStatus", alertStatus);
        }
        return machineAlertDao.query(condition);
    }
}
