package gmair.finley.dao;


import finley.gmair.model.order.MachineItem;
import finley.gmair.pagination.DataTableParam;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

/**
 * Created by XXH on 2018/1/20.
 */
public interface MachineItemDao {

    ResultData query(Map<String, Object> condition);

    ResultData query(DataTableParam param);

    ResultData insert(MachineItem machineItem);

    ResultData insertBatch(List<MachineItem> machineItemList);

    ResultData update(MachineItem machineItem);

    ResultData updateBatch(List<MachineItem> machineItems);
}
