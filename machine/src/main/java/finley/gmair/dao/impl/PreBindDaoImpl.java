package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.PreBindDao;
import finley.gmair.model.machine.PreBindCode;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/31
 */
@Repository
public class PreBindDaoImpl extends BaseDao implements PreBindDao {
    @Override
    @Transactional
    public ResultData insert(PreBindCode preBindCode) {
        ResultData result = new ResultData();
        preBindCode.setBindId(IDGenerator.generate("PBC"));
        try {
            sqlSession.insert("gmair.machine.prebind.insert", preBindCode);
            result.setData(preBindCode);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<PreBindCode> list = sqlSession.selectList("gmair.machine.prebind.query", condition);
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
    public ResultData delete(String codeValue) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.machine.prebind.updateIdleMachine", codeValue);
            sqlSession.delete("gmair.machine.prebind.deletePrebind", codeValue);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryBy2Id(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<PreBindCode> list = sqlSession.selectList("gmair.machine.prebind.queryBy2Id", condition);
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
    public ResultData queryByDate(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<PreBindCode> list = sqlSession.selectList("gmair.machine.prebind.queryByDate", condition);
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

}