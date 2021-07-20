package finley.gmair.dao.impl;

import finley.gmair.dao.ActivityThumbnailDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ActivityThumbnailDaoImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/8/14 10:38 AM
 */
@Repository
public class ActivityThumbnailDaoImpl extends BaseDao implements ActivityThumbnailDao {
    private Logger logger = LoggerFactory.getLogger(ActivityThumbnailDaoImpl.class);

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List list = sqlSession.selectList("gmair.drift.activity.thumbnail.query", condition);
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}
