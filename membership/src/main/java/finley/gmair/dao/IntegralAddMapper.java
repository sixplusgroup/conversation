package finley.gmair.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import finley.gmair.dto.management.IntegralConfirmDto;
import finley.gmair.model.membership.IntegralAdd;
import finley.gmair.param.management.IntegralConfirmParam;
import finley.gmair.util.PaginationAdapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Joby
 */
public interface IntegralAddMapper extends BaseMapper<IntegralAdd> {

    List<IntegralConfirmDto> listConfirmPage(@Param("integralConfirmParam") IntegralConfirmParam integralConfirmParam, @Param("paginationAdapter") PaginationAdapter paginationAdapter);

    long countConfirmPage(@Param("integralConfirmParam")IntegralConfirmParam integralConfirmParam);

    IntegralConfirmDto getIntegralConfirmById(@Param("id") String id);

}
