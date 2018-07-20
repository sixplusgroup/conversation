package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.PictureTemplateDao;
import finley.gmair.model.wechat.PictureTemplate;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.wechat.PictureReplyVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public class PictureTemplateDaoImpl extends BaseDao implements PictureTemplateDao {
    private Logger logger = LoggerFactory.getLogger(PictureTemplateDaoImpl.class);

    @Override
    @Transactional
    public ResultData insert(PictureTemplate pictureTemplate) {
        ResultData result = new ResultData();
        pictureTemplate.setTemplateId(IDGenerator.generate("PTI"));
        try {
            sqlSession.insert("gmair.wechat.picturetemplate.insert", pictureTemplate);
            result.setData(pictureTemplate);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<PictureTemplate> list = sqlSession.selectList("gmair.wechat.picturetemplate.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryPictureReply(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<PictureReplyVo> list = sqlSession.selectList("gmair.wechat.picturetemplate.pictureReplyQuery", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}