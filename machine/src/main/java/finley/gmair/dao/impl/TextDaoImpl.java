package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.TextDao;
import finley.gmair.model.machine.Text;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author ：CK
 * @date ：Created in 2020/11/2 11:30
 * @description：
 */
@Repository
public class TextDaoImpl extends BaseDao implements TextDao {

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            Text text = sqlSession.selectOne("gmair.machine.text.query", condition);
            if (text==null) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(text);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
