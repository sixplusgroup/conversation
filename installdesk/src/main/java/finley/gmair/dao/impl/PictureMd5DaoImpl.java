package finley.gmair.dao.impl;

import finley.gmair.dao.PictureMd5Dao;
import finley.gmair.model.installation.PictureMd5;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResultData;
import finley.gmair.dao.BaseDao;
import finley.gmair.util.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class PictureMd5DaoImpl extends BaseDao implements PictureMd5Dao {

    private Logger logger = LoggerFactory.getLogger(AssignDaoImpl.class);
    @Override
    public ResultData insert(PictureMd5 pictureMd5) {
        ResultData result = new ResultData();
        pictureMd5.setImageId(IDGenerator.generate("IMG"));
        try {
            sqlSession.insert("gmair.install.assign.md5.insert", pictureMd5);
            result.setData(pictureMd5);
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
            List list = sqlSession.selectList("gmair.install.assign.md5.query", condition);
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
