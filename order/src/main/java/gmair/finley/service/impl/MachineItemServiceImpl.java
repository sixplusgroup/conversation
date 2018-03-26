package gmair.finley.service.impl;


import finley.gmair.model.order.MachineItem;
import finley.gmair.pagination.DataTableParam;
import finley.gmair.util.ResultData;
import gmair.finley.dao.MachineItemDao;
import gmair.finley.service.MachineItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

/**
 * Created by XXH on 2018/1/20.
 */
@Service
public class MachineItemServiceImpl implements MachineItemService {

    @Autowired
    private MachineItemDao machineItemDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return machineItemDao.query(condition);
    }

    @Override
    public ResultData fetch(DataTableParam param) {
        return machineItemDao.query(param);
    }

    @Override
    public ResultData create(MachineItem machineItem) {
        return machineItemDao.insert(machineItem);
    }

    @Override
    public ResultData update(MachineItem machineItem) {
        return machineItemDao.update(machineItem);
    }

    @Override
    public ResultData updateBatch(List<MachineItem> machineItems) {
        return machineItemDao.updateBatch(machineItems);
    }
}
