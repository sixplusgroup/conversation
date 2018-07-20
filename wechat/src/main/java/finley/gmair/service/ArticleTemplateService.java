package finley.gmair.service;

import finley.gmair.model.wechat.ArticleTemplate;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ArticleTemplateService {
    ResultData create(ArticleTemplate template);

    ResultData fetch(Map<String, Object> condition);

    ResultData fetchArticleReply(Map<String, Object> condition);
}
