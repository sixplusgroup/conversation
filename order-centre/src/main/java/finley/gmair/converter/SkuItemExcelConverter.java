package finley.gmair.converter;

import finley.gmair.model.dto.SkuItemDTO;
import finley.gmair.model.dto.SkuItemExcel;
import org.mapstruct.Mapper;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-28 14:05
 * @description ：
 */

@Mapper(componentModel = "spring")
public interface SkuItemExcelConverter {
    SkuItemDTO fromExcel(SkuItemExcel excel);
}
