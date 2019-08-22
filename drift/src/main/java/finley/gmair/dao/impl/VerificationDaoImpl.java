package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.VerificationDao;
import finley.gmair.model.drift.VerifyInfo;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author ：lycheeshell
 * @Date ：Created in 14:02 2019/8/22
 */
@Repository
public class VerificationDaoImpl extends BaseDao implements VerificationDao {

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<VerifyInfo> list = sqlSession.selectList("gmair.drift.verification.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insert(VerifyInfo verifyInfo) {
        ResultData result = new ResultData();
        verifyInfo.setVerifyId(IDGenerator.generate("VRF"));
        try {
            sqlSession.insert("gmair.drift.verification.insert", verifyInfo);
            result.setData(verifyInfo);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
