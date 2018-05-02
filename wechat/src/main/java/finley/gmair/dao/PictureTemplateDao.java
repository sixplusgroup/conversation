package finley.gmair.dao;

import finley.gmair.model.wechat.PictureTemplate;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface PictureTemplateDao {
    ResultData insert(PictureTemplate pictureTemplate);

    ResultData query(Map<String, Object> condition);
}
