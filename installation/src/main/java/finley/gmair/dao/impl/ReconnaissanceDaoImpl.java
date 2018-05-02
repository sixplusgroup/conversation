package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ReconnaissanceDao;
import finley.gmair.model.installation.Reconnaissance;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ReconnaissanceDaoImpl extends BaseDao implements ReconnaissanceDao {

    @Override
    public ResultData insert(Reconnaissance reconnaissance) {
        ResultData result = new ResultData();
        reconnaissance.setReconId(IDGenerator.generate("RNE"));
        try {
            sqlSession.insert("gmair.install.reconnaissance.insert", reconnaissance);
            result.setData(reconnaissance);
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
            List<Reconnaissance> list = sqlSession.selectList("gmair.install.reconnaissance.query", condition);
            if(list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        }catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Reconnaissance reconnaissance) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.install.reconnaissance.update", reconnaissance);
            result.setData(reconnaissance);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
