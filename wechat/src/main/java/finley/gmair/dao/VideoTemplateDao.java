package finley.gmair.dao;

import finley.gmair.model.wechat.PictureTemplate;
import finley.gmair.model.wechat.VideoTemplate;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface VideoTemplateDao {
    ResultData insert(VideoTemplate videoTemplate);

    ResultData query(Map<String, Object> condition);
}
