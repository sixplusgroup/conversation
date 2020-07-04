package finley.gmair.service.impl;

import finley.gmair.dao.MachineFilterCleanDao;
import finley.gmair.service.MachineFilterCleanService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/4 13:32
 * @description: TODO
 */

@Service
public class MachineFilterCleanServiceImpl implements MachineFilterCleanService {

    @Autowired
    private MachineFilterCleanDao machineFilterCleanDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return machineFilterCleanDao.query(condition);
    }

    @Override
    public ResultData modify(Map<String, Object> condition) {
        return machineFilterCleanDao.update(condition);
    }
}
