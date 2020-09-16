package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.MachineEfficientInformationDao;
import finley.gmair.model.machine.MachineEfficientInformation;
import finley.gmair.service.DataAnalysisAgent;
import finley.gmair.service.MachineEfficientInfoService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/9/16 10:00
 * @description: MachineEfficientInfoServiceImpl
 */

@Service
public class MachineEfficientInfoServiceImpl implements MachineEfficientInfoService {

    private static final long LAST_N_DAY = new Date().getTime() / (1000 * 3600 * 24);

    private static final String STATUS_TYPE_POWER = "power", POWER_ON_MIN = "powerOnMinute";

    @Autowired
    private DataAnalysisAgent dataAnalysisAgent;

    @Autowired
    private MachineEfficientInformationDao machineEfficientInformationDao;

    @Override
    public ResultData getSubSti(String qrcode) {
        return null;
    }

    @Override
    public ResultData getRunning(String qrcode) {
        ResultData res = new ResultData();
        long powerOnTime = 0;

        //TODO 如何检测qrcode正确性？

        ResultData response =
                dataAnalysisAgent.fetchLastNDayData(qrcode, (int) LAST_N_DAY, STATUS_TYPE_POWER);

        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            JSONArray dataList = JSON.parseArray(JSON.toJSONString(response.getData()));
            for (int i = 0; i < dataList.size(); i++) {
                JSONObject one = dataList.getJSONObject(i);
                if (one.containsKey(POWER_ON_MIN))
                    powerOnTime += (Integer) one.get(POWER_ON_MIN);
            }
        }
        else {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("fetch last n day data failed!");
            return res;
        }
        // minute -> hour
        res.setData(powerOnTime / 60);
        return res;
    }

    private int getLastNDay(String qrcode) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("qrcode", qrcode);

        ResultData response = machineEfficientInformationDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<MachineEfficientInformation> list = (List<MachineEfficientInformation>) response.getData();
            MachineEfficientInformation one = list.get(0);

        }

        return 0;
    }
}
