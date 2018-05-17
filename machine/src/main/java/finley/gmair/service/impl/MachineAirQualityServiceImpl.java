package finley.gmair.service.impl;

import finley.gmair.dao.MachineAirQualityDao;
import finley.gmair.model.air.MachineAirQuality;
import finley.gmair.service.MachineAirQualityService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MachineAirQualityServiceImpl implements MachineAirQualityService{

    @Autowired
    private MachineAirQualityDao machineAirQualityDao;

    @Override
    public ResultData add(MachineAirQuality machineAirQuality) {
        return machineAirQualityDao.insert(machineAirQuality);
    }

    @Override
    public ResultData addBatch(List<MachineAirQuality> list) {
        return machineAirQualityDao.insertBatch(list);
    }
}
