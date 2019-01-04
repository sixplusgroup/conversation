package finley.gmair.service.Impl;

import finley.gmair.dao.MachineStatusRedisDao;
import finley.gmair.service.MachineStatusService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MachineStatusServiceImpl implements MachineStatusService {
    @Autowired
    private MachineStatusRedisDao machineStatusRedisDao;

    public ResultData getHourlyStatisticalData(){
        return machineStatusRedisDao.queryHourlyStatus();
    }
}
