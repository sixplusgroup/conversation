package finley.gmair.service;

import finley.gmair.model.wechat.PictureTemplate;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface PictureTemplateService {
    ResultData create(PictureTemplate template);

    ResultData fetch(Map<String, Object> condition);

    ResultData fetchPictureReply(Map<String, Object> condition);
}
