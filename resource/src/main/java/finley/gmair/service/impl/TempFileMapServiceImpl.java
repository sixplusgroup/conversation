package finley.gmair.service.impl;

import finley.gmair.dao.TempFileMapDao;
import finley.gmair.model.resource.FileMap;
import finley.gmair.service.TempFileMapService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Map;

@Service
public class TempFileMapServiceImpl implements TempFileMapService {

    @Autowired
    private TempFileMapDao tempFileMapDao;

    @Override
    public ResultData createTempFileMap(FileMap tempFileMap){
        ResultData result = new ResultData();

        ResultData response=tempFileMapDao.insertTempFileMap(tempFileMap);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert tempfilemap" + tempFileMap.toString());
        }
        return result;
    }

    @Override
    public ResultData fetchTempFileMap(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = tempFileMapDao.queryTempFileMap(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to fetch tempfilemap");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No tempfilemap found");
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch tempfilemap");
        }
        return result;
    }

    @Override
    public ResultData deleteTempFileMap(Map<String, Object> condition)
    {
        ResultData result = new ResultData();
        ResultData response = tempFileMapDao.deleteTempFileMap(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to delete tempFileMap");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR)
        {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to delete tempFileMap");
        }
        return result;
    }

    @Override
    public String transToMD5(MultipartFile file) throws IOException {
        String value = null;
        FileInputStream in = (FileInputStream) file.getInputStream();
        try {
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.getSize());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }
}
