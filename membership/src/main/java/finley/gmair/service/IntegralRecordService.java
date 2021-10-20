package finley.gmair.service;

import com.baomidou.mybatisplus.extension.service.IService;
import finley.gmair.dto.membership.IntegralRecordDto;
import finley.gmair.model.membership.IntegralRecord;
import finley.gmair.param.installation.IntegralRecordParam;
import finley.gmair.util.PaginationParam;


import java.util.List;

/**
 * @Author Joby
 */
public interface IntegralRecordService extends IService<IntegralRecord> {

    void createRecord(IntegralRecord integralRecord);

    List<IntegralRecord> getMyRecordsByConsumerId(String consumerId);

    PaginationParam<IntegralRecordDto> getRecordPage(IntegralRecordParam integralRecordParam, PaginationParam<IntegralRecordDto> paginationParam);
}
