package finley.gmair.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import finley.gmair.listener.TbOrderPartInfoListener;
import finley.gmair.model.dto.TbOrderPartInfo;
import finley.gmair.service.TbOrderPartInfoService;
import finley.gmair.util.FileUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Bright Chan
 * @date: 2020/11/3 14:12
 * @description: TbOrderPartInfoServiceImpl
 */

@Service
public class TbOrderPartInfoServiceImpl implements TbOrderPartInfoService {

    private Logger logger = LoggerFactory.getLogger(TbOrderPartInfoServiceImpl.class);

    @Override
    public boolean saveOrderPartInfoExcel(String fileParentPath, String filename, byte[] content) {
        return FileUtil.saveFile(fileParentPath, filename, content);
    }

    @Override
    public ResultData getTbOrderPartInfo(String filePath, String password) {
        ResultData res = new ResultData();
        ExcelReader excelReader = null;

        try{
            TbOrderPartInfoListener csvListener = new TbOrderPartInfoListener();
            // build ExcelReader
            ExcelReaderBuilder readerBuilder = EasyExcel.read();
            readerBuilder.file(filePath);
            readerBuilder.head(TbOrderPartInfo.class);
            readerBuilder.registerReadListener(csvListener);
            readerBuilder.password(password);
            excelReader = readerBuilder.build();
            // build ReadSheet
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            // read
            excelReader.read(readSheet);

            // get result data
            List<TbOrderPartInfo> data = csvListener.getReadData();
            res.setData(data);
        }
        catch (Exception e) {
            logger.error("get TbOrderPartInfo from Excel failed: " + e.getMessage());
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("get TbOrderPartInfo from Excel failed!");
        }
        finally {
            if (excelReader != null)
                excelReader.finish();
        }

        return res;
    }
}
