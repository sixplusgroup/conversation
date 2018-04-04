package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.SetupProviderDao;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.SetupProviderVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SetupProviderDaoImpl extends BaseDao implements SetupProviderDao{

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<SetupProviderVo> list = sqlSession.selectList("gmair.machine.setupprovider.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return null;
    }
}
