package finley.gmair.service.impl;

import finley.gmair.dao.MachineEfficientFilterConfigDao;
import finley.gmair.model.machine.MachineEfficientFilterConfig;
import finley.gmair.service.MachineEfficientFilterConfigService;
import finley.gmair.vo.machine.FilterUpdByFormulaConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/11/28 11:25
 * @description: MachineEfficientFilterConfigServiceImpl
 */

@Service
public class MachineEfficientFilterConfigServiceImpl implements MachineEfficientFilterConfigService {

    @Resource
    private MachineEfficientFilterConfigDao machineEfficientFilterConfigDao;

    @Override
    public void insert(MachineEfficientFilterConfig config) {
        machineEfficientFilterConfigDao.insert(config);
    }

    @Override
    public List<FilterUpdByFormulaConfig> fetchConfigList() {
        return machineEfficientFilterConfigDao.queryConfigList();
    }

    @Override
    public boolean update(Map<String, Object> condition) {
        return machineEfficientFilterConfigDao.update(condition);
    }
}
