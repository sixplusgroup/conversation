package finley.gmair.service;

import com.baomidou.mybatisplus.extension.service.IService;
import finley.gmair.dto.management.IntegralConfirmDto;
import finley.gmair.model.membership.IntegralAdd;
import finley.gmair.param.management.IntegralConfirmParam;
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

    void closeIntegralById(String id);

    void giveIntegral(Long id,Integer integralValue);

}
