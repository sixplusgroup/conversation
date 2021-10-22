package finley.gmair.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import finley.gmair.dto.membership.IntegralRecordDto;
import finley.gmair.model.membership.IntegralRecord;
import finley.gmair.param.installation.IntegralRecordParam;
import finley.gmair.util.PaginationAdapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Author Joby
 */
public interface IntegralRecordMapper extends BaseMapper<IntegralRecord> {

    List<IntegralRecordDto> listRecordPage(@Param("integralRecordParam") IntegralRecordParam integralRecordParam, @Param("paginationAdapter")PaginationAdapter paginationAdapter);

    long countRecordPage(@Param("integralRecordParam")IntegralRecordParam integralRecordParam);

}
