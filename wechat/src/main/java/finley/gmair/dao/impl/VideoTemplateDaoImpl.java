package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.VideoTemplateDao;
import finley.gmair.model.wechat.PictureTemplate;
import finley.gmair.model.wechat.VideoTemplate;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public class VideoTemplateDaoImpl extends BaseDao implements VideoTemplateDao {
    private Logger logger = LoggerFactory.getLogger(VideoTemplateDaoImpl.class);

    @Override
    @Transactional
    public ResultData insert(VideoTemplate videoTemplate) {
        ResultData result = new ResultData();
        videoTemplate.setTemplateId(IDGenerator.generate("VTI"));
        logger.info(videoTemplate.toString());
        try {
            sqlSession.insert("gmair.wechat.videotemplate.insert", videoTemplate);
            result.setData(videoTemplate);
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
            List<PictureTemplate> list = sqlSession.selectList("gmair.wechat.videotemplate.query", condition);
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