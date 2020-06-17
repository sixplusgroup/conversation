package finley.gmair.dao;

import finley.gmair.model.installation.PictureMd5;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface PictureMd5Dao {

    ResultData insert(PictureMd5 pictureMd5);

    ResultData query(Map<String, Object> condition);
}
