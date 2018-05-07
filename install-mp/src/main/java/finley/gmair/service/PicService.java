package finley.gmair.service;

import finley.gmair.model.installation.Pic;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface PicService {

    ResultData createPic(Pic pic);

    ResultData fetchPic(Map<String, Object> condition);

    ResultData deletePic(Map<String, Object> condition);
}
