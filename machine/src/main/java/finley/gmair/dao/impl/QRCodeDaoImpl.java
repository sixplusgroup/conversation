package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.QRCodeDao;
import finley.gmair.model.machine.QRCode;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/5/28
 */
@Repository
public class QRCodeDaoImpl extends BaseDao implements QRCodeDao {

    @Override
    @Transactional
    public ResultData insert(QRCode code) {
        ResultData result = new ResultData();
        code.setCodeId(IDGenerator.generate("QRC"));
        try {
            sqlSession.insert("gmair.machine.qrcode.insert", code);
            result.setData(code);
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
            List<QRCode> list = sqlSession.selectList("gmair.machine.qrcode.query", condition);
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