package finley.gmair.service.impl;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.response.TradesSoldGetResponse;

import finley.gmair.model.ordernew.Order;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author zm
 * @date 2020/10/26 0026 11:03
 * @description Crm系统提供的接口
 **/
@Service
public class CrmAPIServiceImpl {

    @Value("${crm.add}")
    private String orderAddUrl;
    @Value("${crm.update}")
    private String statusUpdateUrl;

    public ResultData orderAdd(Order newOrder) {
        ResultData result = new ResultData();

       return result;
    }

    public ResultData orderUpdate(Order newOrder) {
        ResultData result = new ResultData();

        return result;
    }
}
