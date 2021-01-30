package finley.gmair.service;

import finley.gmair.model.machine.MachineEfficientFilterConfig;
import finley.gmair.vo.machine.FilterUpdByFormulaConfig;

import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/11/28 11:24
 * @description: MachineEfficientFilterConfigService
 */
public interface MachineEfficientFilterConfigService {

    void insert(MachineEfficientFilterConfig config);

    List<FilterUpdByFormulaConfig> fetchConfigList();

    boolean update(Map<String, Object> condition);
}
