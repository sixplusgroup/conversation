package finley.gmair.dao;

import finley.gmair.model.wechat.ArticleTemplate;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ArticleTemplateDao {
    ResultData insert(ArticleTemplate articleTemplate);

    ResultData query(Map<String, Object> condition);

    ResultData queryArticleReply(Map<String, Object> condition);
}
