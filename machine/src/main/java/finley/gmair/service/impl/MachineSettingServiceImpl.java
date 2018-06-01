package finley.gmair.service.impl;

import finley.gmair.dao.MachineSettingDao;
import finley.gmair.service.MachineSettingService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class MachineSettingServiceImpl implements MachineSettingService{

    @Autowired
    private MachineSettingDao machineSettingDao;

    @Override
    public ResultData fetchPowerActionMachine() {
        LocalDateTime localDateTime = LocalDateTime.now();
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute() < 30 ? 0 : 30;

        Map<String, Object> condition = new HashMap<>();
        condition.put("triggerHour", hour);
        condition.put("triggerMinute", minute);

        return machineSettingDao.selectPowerSetting(condition);
    }
}
