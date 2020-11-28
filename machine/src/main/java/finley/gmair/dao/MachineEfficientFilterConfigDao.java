package finley.gmair.dao;

import finley.gmair.model.machine.MachineEfficientFilterConfig;
import finley.gmair.vo.machine.FilterUpdByFormulaConfig;

import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/11/28 11:19
 * @description: MachineEfficientFilterConfigDao
 */
public interface MachineEfficientFilterConfigDao {

    void insert(MachineEfficientFilterConfig config);

    List<FilterUpdByFormulaConfig> queryConfigList();

    void update(Map<String, Object> condition);
}
