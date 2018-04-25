package finley.gmair.service.impl;

import finley.gmair.service.UploadService;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.SystemTellerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UploadServiceImpl implements UploadService {

    private Logger logger = LoggerFactory.getLogger(UploadServiceImpl.class);

    @Override
    public ResultData upload(MultipartFile file) {
        ResultData result = new ResultData();
        //check the file not empty.
        try {
            if (file == null || file.getBytes().length == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                return result;
            }
        }
        catch (IOException e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }

        //create the local saving path
        String PATH = "/Users/wjq/desktop/uploadIMG";
        Date current = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String time = format.format(current);
        StringBuilder builder = new StringBuilder(PATH);
        builder.append(File.separator);
        builder.append(time);

        //according to the path,create a file.
        File directory = new File(builder.toString());
        if (!directory.exists()) {
            boolean isDone = directory.mkdirs();
            if (isDone) {
                logger.info("save file to " + directory.getAbsolutePath());
            }
            else {
                logger.error("cannot create directory " + directory.getAbsolutePath());
            }
        }

        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String key = IDGenerator.generate("PIC");
        String name = key + suffix;
        String completeName = builder.append(File.separator).append(name).toString();

        File temp = new File(completeName);
        try {
            file.transferTo(temp);
            int index = temp.getPath().indexOf(SystemTellerUtil.tellPath(PATH + File.separator + time));
            result.setData(temp.getPath().substring(index));
        } catch (IOException e) {
            //logger.debug(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        } finally {
            return result;
        }
    }
}
