package finley.gmair.service.Impl;

import finley.gmair.dao.BarcodeDao;
import finley.gmair.model.assemble.Barcode;
import finley.gmair.service.BarcodeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BarcodeServiceImpl implements BarcodeService {
    @Autowired
    private BarcodeDao barcodeDao;

    @Override
    public ResultData create(Barcode barcode){
        ResultData result = new ResultData();
        ResultData response = barcodeDao.insert(barcode);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert barcode into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData createBatch(List<Barcode> barcodeList){
        ResultData result = new ResultData();
        ResultData response = barcodeDao.insertBatch(barcodeList);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert barcode list into database.").toString());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = barcodeDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch barcode from database.");
            return result;
        }
        else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No barcode found.");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to find barcode");
        return result;
    }

}
