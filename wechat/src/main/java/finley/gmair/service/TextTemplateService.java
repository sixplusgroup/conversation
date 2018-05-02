package finley.gmair.service;

import finley.gmair.model.wechat.TextTemplate;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface TextTemplateService {
    ResultData create(TextTemplate template);

    ResultData fetch(Map<String, Object> condition);

    ResultData modify(TextTemplate template);
}
