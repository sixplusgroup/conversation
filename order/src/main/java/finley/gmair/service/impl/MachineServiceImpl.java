package finley.gmair.service.impl;

import finley.gmair.dao.MachineDao;
import finley.gmair.dao.SetupProviderDao;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class MachineServiceImpl implements MachineService{
    @Autowired
    MachineDao machineDao;

    @Autowired
    SetupProviderDao setupProviderDao;

    @Override
    public ResultData fetchInstallType(Map<String, Object> condition) {
        return machineDao.queryInstallType(condition);
    }

    @Override
    public ResultData fetchSetupProvider(Map<String, Object> condition) {
        return setupProviderDao.query(condition);
    }
}
