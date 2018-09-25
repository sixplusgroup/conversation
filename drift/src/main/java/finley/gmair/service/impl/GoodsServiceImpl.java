package finley.gmair.service.impl;

import finley.gmair.dao.GoodsDao;
import finley.gmair.model.drift.Goods;
import finley.gmair.service.GoodsService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/9/22
 */
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;

    @Override
    public ResultData fetchGoods(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = goodsDao.queryGoods(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No drift goods found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve drift goods");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData createGoods(Goods goods) {
        ResultData result = new ResultData();
        ResultData response = goodsDao.insertGoods(goods);
        switch (response.getResponseCode()) {
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to insert drift goods to database");
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public ResultData modifyGoods(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = goodsDao.updateGoods(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("Succeed to update drift goods to database");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to update drift goods to database");
                break;
            default:
                break;
        }
        return result;
    }
}