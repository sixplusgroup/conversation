package finley.gmair.service.impl;

import finley.gmair.model.tmallGenie.AliGenieRe;
import finley.gmair.model.tmallGenie.Attribute;
import finley.gmair.model.tmallGenie.Header;
import finley.gmair.model.tmallGenie.Payload;
import finley.gmair.service.MachineService;
import finley.gmair.service.TmallQueryService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.tmall.TmallQueryEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class TmallQueryServiceImpl implements TmallQueryService {

    private Logger logger = LoggerFactory.getLogger(TmallQueryServiceImpl.class);

    @Autowired
    private CommonServiceImpl commonServiceImpl;

    @Autowired
    private MachineService machineService;

    @Override
    public AliGenieRe query(Payload payload, Header header) {
        String name = header.getName();
        String deviceId = payload.getDeviceId();

        List<Attribute> properties = new ArrayList<>();
        ResultData runningStatus = machineService.runningStatus(deviceId);
        LinkedHashMap<String, Object> runningInfo = (LinkedHashMap<String, Object>) runningStatus.getData();
        if (runningStatus.getResponseCode() == ResponseCode.RESPONSE_OK && runningInfo != null) {
            TmallQueryEnum queryEnum = TmallQueryEnum.valueOf(name);

            logger.info("query type: " + queryEnum);

            switch (queryEnum) {
                case Query:
                    // 就是设备发现的结果
                    properties = commonServiceImpl.getProperties(deviceId, getModelId(deviceId));
                    break;
                case QueryTemperature:
                    properties.add(new Attribute("temperature", Integer.toString((int) runningInfo.get("temperature"))));
                    break;
                case QueryWindSpeed:
                    // 档位
                    String modelId = getModelId(deviceId);
                    if(modelId != null) {
                        String windspeed = commonServiceImpl.getWindspeedByVolume((int) runningInfo.get("volume"), modelId);
                        properties.add(new Attribute("windspeed", windspeed));
                    }
                    break;
                case QueryPowerState:
                    // 电源状态
                    int p = (int) runningInfo.get("power");
                    String power = commonServiceImpl.getPower(p);
                    if (power != null) {
                        properties.add(new Attribute("powerstate", power));
                    }
                    break;
            }
        }

        AliGenieRe response = new AliGenieRe();
        response.setProperties(properties);
        header.setName(commonServiceImpl.setResponseName(header.getName()));
        response.setHeader(header);
        Payload responsePayload = new Payload(deviceId);
        response.setPayload(responsePayload);
        return response;
    }

    private String getModelId(String deviceId) {
        String modelId = null;
        ResultData response = machineService.getModel(deviceId);
        List<LinkedHashMap<String, Object>> goodsModelList = (List<LinkedHashMap<String, Object>>) response.getData();
        if (goodsModelList != null && goodsModelList.size() != 0) {
            // 根据型号获取型号具体信息
            modelId = (String) goodsModelList.get(0).get("modelId");
        }
        return modelId;
    }

}
