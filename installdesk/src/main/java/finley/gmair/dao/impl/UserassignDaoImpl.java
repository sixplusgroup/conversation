package finley.gmair.dao.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.BaseDao;
import finley.gmair.dao.UserassignDao;
import finley.gmair.model.installation.Userassign;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class UserassignDaoImpl extends BaseDao implements UserassignDao {

    private Logger logger = LoggerFactory.getLogger(AssignDaoImpl.class);

    @Override
    public ResultData insert(Userassign userassign) {
        ResultData resultData = new ResultData();
        userassign.setUserassignId(IDGenerator.generate("IUA"));
        try{
            sqlSession.insert("gmair.install.userassign.insert",userassign);
            resultData.setData(userassign);
        }catch(Exception e){
            logger.error(e.getMessage());
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription(e.getMessage());
        }
        return resultData;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List list = sqlSession.selectList("gmair.install.userassign.query", condition);
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
    public ResultData query(Map<String, Object> condition, int start, int length) {
        ResultData result = new ResultData();
        JSONObject data = new JSONObject();
        try {
            List list = sqlSession.selectList("gmair.install.userassign.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            data.put("size", list.size());
            list = sqlSession.selectList("gmair.install.userassign.query", condition, new RowBounds(start, length));
            data.put("data", list);
            result.setData(data);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData principal(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List list = sqlSession.selectList("gmair.install.userassign.principal", condition);
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
    public ResultData principal(Map<String, Object> condition, int start, int length) {
        ResultData result = new ResultData();
        JSONObject data = new JSONObject();
        try {
            int size = sqlSession.selectOne("gmair.install.userassign.principalLength", condition);
            if (size==0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            condition.put("start",start);
            condition.put("length",length);
            data.put("size", size);
            data.put("totalPage", (size-1) / length + 1);
            List list = sqlSession.selectList("gmair.install.userassign.principal", condition);
            data.put("list", list);
            result.setData(data);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateUserassignStatus(String userassignId,int status) {
        ResultData result = new ResultData();
        try {
            Timestamp userassignDate = new Timestamp(System.currentTimeMillis());
            Map<String,Object> map = new HashMap<>();
            map.put("userassignId",userassignId);
            map.put("userassignDate",userassignDate);
            map.put("status",status);

            sqlSession.update("gmair.install.userassign.updateUserassignStatus", map);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Userassign userassign) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.install.userassign.update", userassign);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
