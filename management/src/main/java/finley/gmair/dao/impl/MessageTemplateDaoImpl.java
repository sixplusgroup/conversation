package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MessageTemplateDao;
import finley.gmair.model.message.MessageTemplate;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

@Repository
public class MessageTemplateDaoImpl extends BaseDao implements MessageTemplateDao {
    @Override
    public ResultData insertTemplate(MessageTemplate template) {
        ResultData result = new ResultData();

        return result;
    }
}
