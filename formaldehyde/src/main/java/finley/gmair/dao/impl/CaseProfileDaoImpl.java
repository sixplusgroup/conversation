package finley.gmair.dao.impl;

import finley.gmair.dao.CaseProfileDao;
import finley.gmair.model.formaldehyde.CaseProfile;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CaseProfileDaoImpl extends BaseDao implements CaseProfileDao {

    @Override
    public ResultData insert(CaseProfile caseProfile) {
        ResultData result = new ResultData();
        caseProfile.setCaseId(IDGenerator.generate("CAS"));
        try {
            sqlSession.insert("gmair.formaldehyde.case_profile.insert", caseProfile);
            result.setData(caseProfile);
        } catch (Exception e) {
            e.printStackTrace();
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<CaseProfile> list = sqlSession.selectList("gmair.formaldehyde.case_profile.query", condition);
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
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.formaldehyde.case_profile.update", condition);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
