package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ControlOptionDao;
import finley.gmair.model.machine.ControlOption;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.ControlOptionVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/22
 */
@Repository
public class ControlOptionDaoImpl extends BaseDao implements ControlOptionDao {
    @Override
    public ResultData insert(ControlOption option) {
        ResultData result = new ResultData();
        option.setControlId(IDGenerator.generate("COC"));
        try {
            sqlSession.insert("gmair.machine.control.option.insert", option);
            result.setData(option);
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
            List<ControlOptionVo> list = sqlSession.selectList("gmair.machine.control.option.query", condition);
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