package finley.gmair.service.impl;

import finley.gmair.dao.PicDao;
import finley.gmair.model.installation.Pic;
import finley.gmair.service.PicService;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.SystemTellerUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PicServiceImpl implements PicService {
    @Autowired
    private PicDao picDao;

    @Override
    public ResultData createPic(Pic pic){
        ResultData result = new ResultData();
        ResultData response=picDao.insertPic(pic);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert pic" + pic.toString());
        }
        return result;
    }

    @Override
    public ResultData fetchPic(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = picDao.queryPic(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to fetch pic");
        }
        else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch pic");
        }
        else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No pic found");
        }
        return result;
    }

    @Override
    public ResultData savePic(String memberPhone,String path)
    {
        ResultData result = new ResultData();
        String pics[] = path.split(",");
        for(int i=0;i<pics.length;i++)
        {
            System.out.println("start save the path "+pics[i]);
            File file = new File(pics[i]);
            if(file.exists()==false) {
                result.setDescription(result.getDescription()+"."+pics[i]+" is not a correct path.");
                continue;
            }

            Pic pic = new Pic(pics[i],memberPhone);
            try {
                //拿到文件路径,对图片文件进行md5运算.
                String picMd5 = DigestUtils.md5Hex(new FileInputStream(pic.getPicAddress()));
                pic.setPicMd5(picMd5);
                //查找是否存在重复md5的文件,计算copyFlag
                Map<String,Object> condition = new HashMap<>();
                condition.put("picMd5",picMd5);
                condition.put("blockFlag",false);
                ResultData response = fetchPic(condition);
                if(response.getResponseCode()==ResponseCode.RESPONSE_OK)
                    pic.setCopyFlag(true);
                else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL)
                    pic.setCopyFlag(false);
                //保存
                createPic(pic);
            }
            catch (Exception e){
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
                return result;
            }
        }
        return result;
    }

    @Override
    public ResultData uploadPic(MultipartFile file) {
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
            directory.mkdirs();
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

