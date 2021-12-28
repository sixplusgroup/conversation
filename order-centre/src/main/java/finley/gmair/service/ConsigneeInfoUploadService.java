package finley.gmair.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import finley.gmair.converter.ConsigneeInfoConverter;
import finley.gmair.listener.JdConsigneeExcelUploadListener;
import finley.gmair.model.dto.ConsigneeDTO;
import finley.gmair.model.dto.JdConsigneeExcel;
import finley.gmair.util.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-23 20:17
 * @description ：
 */

@Service
public class ConsigneeInfoUploadService {

    @Resource
    ConsigneeInfoConverter consigneeInfoConverter;

    public boolean saveOrderPartInfoExcel(String fileParentPath, String filename, byte[] content) {
        return FileUtil.saveFile(fileParentPath, filename, content);
    }

    public List<ConsigneeDTO> getConsigneeInfoList(String filePath, String password) {
        List<ConsigneeDTO> resultList;
        ExcelReader excelReader = null;

        try {
            JdConsigneeExcelUploadListener csvListener = new JdConsigneeExcelUploadListener();
            // build ExcelReader
            ExcelReaderBuilder readerBuilder = EasyExcel.read();
            readerBuilder.file(filePath);
            readerBuilder.head(JdConsigneeExcel.class);
            readerBuilder.registerReadListener(csvListener);
            if (StringUtils.isNotEmpty(password)) {
                readerBuilder.password(password);
            }
            excelReader = readerBuilder.build();
            // build ReadSheet
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            // read
            excelReader.read(readSheet);

            // get result data
            List<JdConsigneeExcel> data = csvListener.getReadData();
            resultList = data.stream().map(consigneeInfoConverter::fromExcel)
                    .filter(distinctByKey(ConsigneeDTO::getTid)).collect(Collectors.toList());
        } finally {
            if (excelReader != null) {
                excelReader.finish();
            }
        }
        return resultList;
    }


    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
