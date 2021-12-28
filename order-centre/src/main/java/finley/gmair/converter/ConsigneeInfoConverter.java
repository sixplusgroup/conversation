package finley.gmair.converter;

import finley.gmair.model.dto.ConsigneeDTO;
import finley.gmair.model.dto.JdConsigneeExcel;
import org.mapstruct.Mapper;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-23 21:04
 * @description ：
 */

@Mapper(componentModel = "spring")
public interface ConsigneeInfoConverter {

    ConsigneeDTO fromExcel(JdConsigneeExcel excel);
}
