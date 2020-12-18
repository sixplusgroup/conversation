package finley.gmair.service;

import finley.gmair.model.mqttManagement.MachineAlert;

import java.util.List;

/**
 * 设备警报服务
 *
 * @author lycheeshell
 * @date 2020/12/18 16:32
 */
public interface MachineAlertService {

    /**
     * 创建设备警报
     *
     * @param machineId 设备id
     * @param code      警报码
     * @param msg       警报信息
     * @return 新增条数
     */
    int createMachineAlert(String machineId, int code, String msg);

    /**
     * 更新设备警报
     *
     * @param machineId 设备id
     * @param code      警报码
     * @return 更新条数
     */
    int updateMachineAlert(String machineId, int code);

    /**
     * 查询设备警报
     *
     * @param machineId   设备id
     * @param code        警报码
     * @param alertStatus 警报状态
     * @return 设备警报列表
     */
    List<MachineAlert> queryMachine(String machineId, Integer code, Boolean alertStatus);

}
