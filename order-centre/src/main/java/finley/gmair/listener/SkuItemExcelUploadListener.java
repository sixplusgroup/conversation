package finley.gmair.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import finley.gmair.converter.SkuItemExcelConverter;
import finley.gmair.model.dto.SkuItemDTO;
import finley.gmair.model.dto.SkuItemExcel;
import finley.gmair.service.SkuItemWriteService;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-28 13:49
 * @description ：
 */

public class SkuItemExcelUploadListener extends AnalysisEventListener<SkuItemExcel> {

    private List<SkuItemExcel> readData = new ArrayList<>();

    public SkuItemExcelUploadListener(SkuItemWriteService skuItemWriteService, SkuItemExcelConverter skuItemExcelConverter) {
        this.skuItemWriteService = skuItemWriteService;
        this.skuItemExcelConverter = skuItemExcelConverter;
    }

    SkuItemWriteService skuItemWriteService;

    SkuItemExcelConverter skuItemExcelConverter;

    @Override
    public void invoke(SkuItemExcel data, AnalysisContext context) {
        readData.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (CollectionUtils.isEmpty(readData)) {
            return;
        }
        List<SkuItemDTO> skuItemDTOList = readData.stream().map(skuItemExcelConverter::fromExcel)
                .collect(Collectors.toList());
        skuItemWriteService.updateSkuItem(skuItemDTOList);
    }
}