package finley.gmair.service.impl;

import finley.gmair.dao.MachineFilterInfoDao;
import finley.gmair.dto.MachineEfficientFilterInfo;
import finley.gmair.dto.MachinePrimaryFilterInfo;
import finley.gmair.dto.MachineTypeInfo;
import finley.gmair.form.machine.MachineFilterInfoQuery;
import finley.gmair.service.MachineFilterInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/11/8 14:38
 * @description: MachineFilterInfoServiceImpl
 */

@Service
public class MachineFilterInfoServiceImpl implements MachineFilterInfoService {

    @Resource
    private MachineFilterInfoDao machineFilterInfoDao;

    @Override
    public List<MachinePrimaryFilterInfo> queryMachinePrimaryFilterInfo(MachineFilterInfoQuery query) {
        return machineFilterInfoDao.fetchMachinePrimaryFilterInfo(getQueryCondition(query));
    }

    @Override
    public List<MachineEfficientFilterInfo> queryMachineEfficientFilterInfo(MachineFilterInfoQuery query) {
        return machineFilterInfoDao.fetchMachineEfficientFilterInfo(getQueryCondition(query));
    }

    @Override
    public List<MachineTypeInfo> queryMachineTypeInfo() {
        return machineFilterInfoDao.fetchMachineTypeInfo();
    }

    private Map<String, Object> getQueryCondition(MachineFilterInfoQuery query) {
        int pageIndex = query.getPageIndex(), pageSize = query.getPageSize();
        int offset = (pageIndex - 1) * pageSize;

        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", query.getQrcode());
        condition.put("machineModelName", query.getMachineModelName());
        condition.put("machineModelCode", query.getMachineModelCode());
        condition.put("offset", offset);
        condition.put("pageSize", pageSize);
        condition.put("blockFlag", false);
        return condition;
    }
}
