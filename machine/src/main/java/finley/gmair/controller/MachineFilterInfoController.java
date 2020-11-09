package finley.gmair.controller;

import finley.gmair.dto.MachineEfficientFilterInfo;
import finley.gmair.dto.MachineFilterInfoQuery;
import finley.gmair.dto.MachinePrimaryFilterInfo;
import finley.gmair.model.machine.MachineFilterType;
import finley.gmair.service.MachineFilterInfoService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: Bright Chan
 * @date: 2020/11/8 11:47
 * @description: 用于提供设备滤网状况概览功能接口
 */

@RestController
@RequestMapping("/machine/filter/info")
public class MachineFilterInfoController {

    @Resource
    private MachineFilterInfoService machineFilterInfoService;

    @GetMapping("/query")
    public ResultData queryMachineFilterInfo(@RequestBody MachineFilterInfoQuery query) {
        ResultData res = new ResultData();

        if (!checkParams(query)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("params error");
        }
        else {
            MachineFilterType type = query.getMachineFilterType();
            if (type.equals(MachineFilterType.PRIMARY)) {
                List<MachinePrimaryFilterInfo> store =
                        machineFilterInfoService.queryMachinePrimaryFilterInfo(query);
                res.setData(store);
            }
            else if (type.equals(MachineFilterType.EFFICIENT)) {
                List<MachineEfficientFilterInfo> store =
                        machineFilterInfoService.queryMachineEfficientFilterInfo(query);
                res.setData(store);
            }
        }
        return res;
    }

    private boolean checkParams(MachineFilterInfoQuery query) {
        if (query != null) {
            MachineFilterType type = query.getMachineFilterType();
            if (type.equals(MachineFilterType.PRIMARY)
                    || type.equals(MachineFilterType.EFFICIENT)) {
                return query.getPageIndex() >= 1 && query.getPageSize() >= 1;
            }
        }
        return false;
    }

}
