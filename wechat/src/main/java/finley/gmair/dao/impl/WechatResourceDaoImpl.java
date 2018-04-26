package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.WechatResourceDao;
import finley.gmair.model.wechat.WechatResource;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public class WechatResourceDaoImpl extends BaseDao implements WechatResourceDao {
    private Logger logger = LoggerFactory.getLogger(WechatResourceDaoImpl.class);

    @Override
    @Transactional
    public ResultData insert(WechatResource resource) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.wechat.resource.insert", resource);
            result.setData(resource);
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
            List<WechatResource> list = sqlSession.selectList("gmair.wechat.resource.query", condition);
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
    public ResultData update(WechatResource resource) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.wechat.resource.update", resource);
            result.setData(resource);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}