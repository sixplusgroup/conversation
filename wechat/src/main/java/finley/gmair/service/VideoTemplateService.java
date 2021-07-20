package finley.gmair.service;

import finley.gmair.model.wechat.PictureTemplate;
import finley.gmair.model.wechat.VideoTemplate;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface VideoTemplateService {
    ResultData create(VideoTemplate videoTemplate);

    ResultData fetch(Map<String, Object> condition);
}
