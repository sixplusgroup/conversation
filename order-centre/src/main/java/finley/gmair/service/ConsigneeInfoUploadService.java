package finley.gmair.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.google.common.collect.Lists;
import finley.gmair.converter.ConsigneeInfoConverter;
import finley.gmair.listener.JdConsigneeExcelUploadListener;
import finley.gmair.model.domain.UnifiedShop;
import finley.gmair.model.dto.ConsigneeDTO;
import finley.gmair.model.dto.JdConsigneeExcel;
import finley.gmair.model.enums.PlatformEnum;
import finley.gmair.repo.UnifiedShopRepo;
import finley.gmair.util.DateUtil;
import finley.gmair.util.FileUtil;
import finley.gmair.util.ResultData;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipException;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-23 20:17
 * @description ：
 */

@Service
public class ConsigneeInfoUploadService {

    private static final String FILE_ROOT_PATH = "/gmairOrderExcel/";

    private static final String CSV = ".csv";

    @Resource
    ConsigneeInfoConverter consigneeInfoConverter;

    @Resource
    UnifiedShopRepo unifiedShopRepo;

    @Resource
    TradeWriteService tradeWriteService;

    private Logger logger = LoggerFactory.getLogger(ConsigneeInfoUploadService.class);

    public ResultData handleConsigneeInfoFile(MultipartFile file, String password, String shopId) {
        // step0:校验参数
        UnifiedShop shop = unifiedShopRepo.findById(shopId);
        if (null == shop) {
            return ResultData.error("shopId不存在");
        }

        // step1:保存文件,以shopId为路径保存文件,同名(同名文件内容一般相同)覆盖
        String fileParentPath = FILE_ROOT_PATH + shopId + "/";
        boolean saveRes = saveOrderPartInfoExcel(fileParentPath, file);
        if (!saveRes) {
            return ResultData.error("文件保存失败");
        }

        // step2:解析文件,去模糊化
        String filePath = fileParentPath + file.getOriginalFilename();
        File savedFile = new File(filePath);
        List<ConsigneeDTO> list = Lists.newArrayList();
        try {
            if (shop.getPlatform() == PlatformEnum.JD.getValue()) {
                list = getJdConsigneeInfoList(savedFile, password);
            } else if (shop.getPlatform() == PlatformEnum.TMALL.getValue()) {
                list = getTbConsigneeInfoList(savedFile, password);
            }
        } catch (Exception e) {
            logger.error("get consigneeInfo from file error", e);
            return ResultData.error("文件解析失败");
        }
        if (CollectionUtils.isEmpty(list)) {
            return ResultData.error("excel解析结果为空");
        }

        int count = tradeWriteService.defuzzyConsigneeInfoList(list, shopId);
        return ResultData.ok(count);
    }


    private boolean saveOrderPartInfoExcel(String fileParentPath, MultipartFile file) {
        try {
            return FileUtil.saveFile(fileParentPath, file.getOriginalFilename(), file.getBytes());
        } catch (IOException e) {
            logger.error("file " + file.getOriginalFilename() + " gets bytes failed", e);
            return false;
        }
    }

    private List<ConsigneeDTO> getJdConsigneeInfoList(File file, String password) throws IOException {
        List<ConsigneeDTO> resultList = Lists.newArrayList();

        //读取zip
        ZipFile zipFile = new ZipFile(file, password.toCharArray());
        List<FileHeader> fileHeaders = zipFile.getFileHeaders();
        if (CollectionUtils.isEmpty(fileHeaders)) {
            throw new ZipException("京东文件格式错误:压缩文件内容为空");
        }
        if (fileHeaders.size() > 1) {
            throw new ZipException("京东文件格式错误:压缩文件内容有多个");
        }
        //读取zip第一个csv文件的输入流
        InputStream inputStream = zipFile.getInputStream(fileHeaders.get(0));
        //编码格式GBK
        Reader reader = new InputStreamReader(inputStream, "GBK");

        //使用common-csv解析csv文件输入流
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().withAllowMissingColumnNames().parse(reader);
        for (CSVRecord record : records) {
            ConsigneeDTO consigneeDTO = new ConsigneeDTO();
            consigneeDTO.setTid(record.get("订单号").trim());
            consigneeDTO.setCreateTime(DateUtil.str2Date(record.get("下单时间").trim()));
            consigneeDTO.setPhone(record.get("联系电话").trim());
            resultList.add(consigneeDTO);
        }
        return resultList;
    }

    private List<ConsigneeDTO> getTbConsigneeInfoList(File file, String password) throws IOException {
        List<ConsigneeDTO> resultList;
        ExcelReader excelReader = null;

        try {
            JdConsigneeExcelUploadListener csvListener = new JdConsigneeExcelUploadListener();
            // build ExcelReader
            ExcelReaderBuilder readerBuilder = EasyExcel.read();
            readerBuilder.file(file);
            readerBuilder.head(JdConsigneeExcel.class);
            readerBuilder.registerReadListener(csvListener);
            readerBuilder.password(password);
            excelReader = readerBuilder.build();
            // build ReadSheet
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            // read
            excelReader.read(readSheet);

            // get result data
            List<JdConsigneeExcel> data = csvListener.getReadData();
            resultList = data.stream().map(consigneeInfoConverter::fromExcel).collect(Collectors.toList());
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
