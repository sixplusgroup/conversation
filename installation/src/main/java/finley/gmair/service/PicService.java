package finley.gmair.service;

import finley.gmair.model.installation.Pic;
import finley.gmair.util.ResultData;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface PicService {

    ResultData createPic(Pic pic);

    ResultData fetchPic(Map<String,Object> condition);

    ResultData savePic(String memberPhone,String url,String path);

    //ResultData uploadPic(MultipartFile file);

}
