package finley.gmair.service;

import finley.gmair.model.installation.PictureMd5;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface PictureMd5Service {
    ResultData create(PictureMd5 pictureMd5);

    ResultData fetch(Map<String, Object> condition);
}
