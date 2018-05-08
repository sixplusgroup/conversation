package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.CoreDao;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

@Repository
public class CoreDaoImpl extends BaseDao implements CoreDao {

    @Override
    public ResultData insert() {
        ResultData result = new ResultData();
        try {
              mongoTemplate.insert(null);

        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
