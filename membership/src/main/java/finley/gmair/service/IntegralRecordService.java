package finley.gmair.service;

import com.baomidou.mybatisplus.extension.service.IService;
import finley.gmair.model.IntegralRecord;


import java.util.List;

/**
 * @Author Joby
 */
public interface IntegralRecordService extends IService<IntegralRecord> {

    void createRecord(IntegralRecord integralRecord);

    List<IntegralRecord> getMyRecordsByConsumerId(String consumerId);
}
