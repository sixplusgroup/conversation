package finley.gmair.service.impl;

import finley.gmair.dao.MachineListDailyDao;
import finley.gmair.model.machine.MachineListDaily;
import finley.gmair.service.MachineListDailyService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MachineListDailyServiceImpl implements MachineListDailyService {

    @Autowired
    private MachineListDailyDao machineListDailyDao;

    @Override
    public ResultData insertMachineListDailyBatch(List<MachineListDaily> list) {
        return machineListDailyDao.insertMachineListDailyBatch(list);

    }

    @Override
    public ResultData queryMachineListDaily(Map<String, Object> condition){
        return machineListDailyDao.queryMachineListView(condition);
    }
}
