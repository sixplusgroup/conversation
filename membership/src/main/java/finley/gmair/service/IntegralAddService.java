package finley.gmair.service;

import com.baomidou.mybatisplus.extension.service.IService;
import finley.gmair.model.membership.IntegralAdd;


/**
 * @Author Joby
 */
public interface IntegralAddService extends IService<IntegralAdd> {

    void createAdd(IntegralAdd integralAdd);

    void confirmIntegralById(Long integralAddId);


}
