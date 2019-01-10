package finley.gmair.dao.Impl;

import finley.gmair.dao.BarcodeDao;
import finley.gmair.model.assemble.Barcode;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Repository
public class BarcodeDaoImpl extends BaseDao implements BarcodeDao {

    @Override
    public ResultData insert(Barcode barcode) {
        ResultData result = new ResultData();
        barcode.setCodeId(IDGenerator.generate("BRC"));
        try {
            sqlSession.insert("gmair.assemble.barcode.insert", barcode);
            result.setData(barcode);
        } catch (Exception e) {
            e.printStackTrace();
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    @Override
    public ResultData insertBatch(List<Barcode> barcodeList) {
        ResultData result = new ResultData();
        for (Barcode barcode : barcodeList) {
            if (StringUtils.isEmpty(barcode.getCodeId()));
                barcode.setCodeId(IDGenerator.generate("BRC"));
        }
        try {
            sqlSession.insert("gmair.assemble.barcode.insertBatch", barcodeList);
            result.setData(barcodeList);
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
            List<Barcode> list = sqlSession.selectList("gmair.assemble.barcode.query", condition);
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
            sqlSession.update("gmair.assemble.barcode.update", condition);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
