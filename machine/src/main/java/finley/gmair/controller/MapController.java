package finley.gmair.controller;
import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.model.machine.MachineDefaultLocation;
import finley.gmair.model.machine.Ownership;
import finley.gmair.model.map.MachineLoc;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


//这个Controller是用来给Map模块提供接口调用的
@RestController
@RequestMapping("/machine/map")
public class MapController {

    @Autowired
    private MachineQrcodeBindService machineQrcodeBindService;
    @Autowired
    private ConsumerQRcodeBindService consumerQRcodeBindService;
    @Autowired
    private MachineDefaultLocationService machineDefaultLocationService;
    @Autowired
    private LocationService locationService;

    //查询已与用户绑定的所有机器
    private ResultData probeMachineList() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("ownership", Ownership.OWNER.getValue());
        condition.put("blockFlag", false);
        ResultData response = consumerQRcodeBindService.fetchConsumerQRcodeBind(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch the machine list");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find any machine");
            return result;
        }
        List<ConsumerQRcodeBind> cqbList = (List<ConsumerQRcodeBind>) response.getData();
        List<MachineLoc> machineLocList = new ArrayList<>();
        for (ConsumerQRcodeBind cqb : cqbList) {
            String codeValue = cqb.getCodeValue();
            condition.clear();
            condition.put("codeValue", codeValue);
            condition.put("blockFlag", false);
            response = machineQrcodeBindService.fetch(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK)
                continue;
            String machineId = ((List<MachineQrcodeBindVo>) response.getData()).get(0).getMachineId();
            MachineLoc machineLoc = new MachineLoc(cqb.getConsumerId(), cqb.getBindName(), machineId, codeValue, -1, -1);
            machineLocList.add(machineLoc);
        }

        result.setData(machineLocList);
        result.setDescription("success to find the machine list,list.size = " + machineLocList);
        return result;
    }

    //根据qrcode查询经纬度
    private ResultData probeLatLngByQRcode(String qrcode) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = machineDefaultLocationService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        }
        String cityId = (String) ((List<MachineDefaultLocation>) response.getData()).get(0).getCityId();
        return locationService.cityProfile(cityId);
    }

    @GetMapping("/fetch")
    public ResultData fetchMachineLatLngList() {
        ResultData result = new ResultData();

        //首先查找出所有的机器列表
        ResultData response = probeMachineList();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK)
            return response;
        List<MachineLoc> machineLocList = (List<MachineLoc>) response.getData();

        //针对机器列表,查出该机器所在地
        List<MachineLoc> resultList = new ArrayList<>();
        for (MachineLoc loc : machineLocList) {
            response = probeLatLngByQRcode(loc.getCodeValue());
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK)
                continue;
            List<LinkedHashMap> cityList = (List<LinkedHashMap>) response.getData();
            double latitude = (double) (cityList.get(0)).get("latitude");
            double longitude = (double) (cityList.get(0)).get("longitude");

            //生成 -0.5 - 0.5 的随机浮点数
            double generatorDouble1 = new Random().nextDouble() * 0.5 - 0.25;
            double generatorDouble2 = new Random().nextDouble() * 0.5 - 0.25;
            loc.setLongitude(latitude + generatorDouble1);
            loc.setLatitude(longitude + generatorDouble2);
            resultList.add(loc);

        }
        result.setData(resultList);
        result.setDescription("success to get the list,list.size=" + resultList.size());
        return result;
    }
}
