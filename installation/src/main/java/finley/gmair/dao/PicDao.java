package finley.gmair.dao;

import finley.gmair.model.installation.Pic;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface PicDao  {
    ResultData insertPic(Pic pic);

    ResultData queryPic(Map<String, Object> condition);

    ResultData deletePic(Map<String, Object> condition);
}
