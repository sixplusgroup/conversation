package finley.gmair.dao;

import finley.gmair.model.mqttManagement.MachineAlert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 设备警报的数据操作
 *
 * @author lycheeshell
 * @date 2020/12/18 16:09
 */
@Mapper
public interface MachineAlertDao {

    /**
     * 新增设备警报
     *
     * @param machineAlert 设备警报
     * @return 新增条数
     */
    int insert(MachineAlert machineAlert);

    /**
     * 更新设备警报
     *
     * @param condition 更新信息
     * @return 更新条数
     */
    int updateSingle(Map<String, Object> condition);

    /**
     * 批量更新设备警报
     *
     * @param condition 更新信息
     * @return 更新条数
     */
    int updateBatch(Map<String, Object> condition);

    /**
     * 查询设备警报列表
     *
     * @param condition 查询条件
     * @return 设备警报列表
     */
    List<MachineAlert> query(Map<String, Object> condition);
}
