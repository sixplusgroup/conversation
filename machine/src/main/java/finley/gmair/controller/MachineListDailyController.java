package finley.gmair.controller;


import finley.gmair.model.machine.MachineListDaily;
import finley.gmair.model.machine.MachineSecondListView;
import finley.gmair.service.ConsumerQRcodeBindService;
import finley.gmair.service.MachineListDailyService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/machine/info/list/daily")
public class MachineListDailyController {

    @Autowired
    private MachineListDailyService machineListDailyService;

    @Autowired
    private ConsumerQRcodeBindService consumerQRcodeBindService;

    @PostMapping("/schedule/create")
    public ResultData createMachineListDaily() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        ResultData response = consumerQRcodeBindService.queryMachineListView(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            return result;
        }
        result.setData(response.getData());
        List<MachineInfoVo> infoList = (List<MachineInfoVo>) response.getData();
        List<MachineListDaily> list = new ArrayList<>();
        for (MachineInfoVo miv : infoList) {
            MachineListDaily mld = new MachineListDaily(miv.getConsumerId(), miv.getBindName(), miv.getCodeValue(), miv.getMachineId(), miv.getConsumerName(), miv.getConsumerPhone(), 0, true);
            list.add(mld);
        }
        response = consumerQRcodeBindService.queryMachineSecondListView(condition);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            return result;
        }
        List<MachineSecondListView> secondList = (List<MachineSecondListView>)response.getData();
        for(MachineListDaily mld: list){
            for(MachineSecondListView mslv: secondList){
                if(mld.getConsumerId().equals(mslv.getConsumerId())){
                    mld.setOverCount(mslv.getOverCount());
                    mld.setOffline(false);
                }
            }
        }

        //list = list.stream().filter(e -> e.getOverCount() !=0).collect(Collectors.toList());
        machineListDailyService.insertMachineListDailyBatch(list);
        result.setData(list);
        return result;
    }
}
