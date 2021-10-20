package finley.gmair.service;

import com.baomidou.mybatisplus.extension.service.IService;
import finley.gmair.dto.installation.IntegralConfirmDto;
import finley.gmair.dto.membership.IntegralRecordDto;
import finley.gmair.model.membership.IntegralAdd;
import finley.gmair.param.installation.IntegralConfirmParam;
import finley.gmair.util.PaginationParam;


/**
 * @Author Joby
 */
public interface IntegralAddService extends IService<IntegralAdd> {

    void createAdd(IntegralAdd integralAdd);

    void confirmIntegralById(Long integralAddId);

    PaginationParam<IntegralConfirmDto> getConfirmPage(IntegralConfirmParam integralConfirmParam, PaginationParam<IntegralConfirmDto> paginationParam);

    IntegralConfirmDto getIntegralConfirmById(String id);

    void deleteById(String id);


}
