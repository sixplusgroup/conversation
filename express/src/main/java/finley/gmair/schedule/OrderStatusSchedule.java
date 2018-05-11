package finley.gmair.schedule;

import finley.gmair.company.CompanyTransfer;
import finley.gmair.dao.ExpressCompanyDao;
import finley.gmair.dao.ExpressOrderDao;
import finley.gmair.model.express.ExpressCompany;
import finley.gmair.model.express.ExpressOrder;
import finley.gmair.model.express.ExpressStatus;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderStatusSchedule {

    @Autowired
    private ExpressOrderDao expressOrderDao;

    @Autowired
    private ExpressCompanyDao expressCompanyDao;

    @Autowired
    private CompanyTransfer companyTransfer;


    /**
     *This method is used to update express status every hour
     *
     */
    @Scheduled(cron = "0 * * * * ?")
    public void update(){
        System.out.println(new Timestamp(System.currentTimeMillis()));
        Map<String, Object> condition = new HashMap<>();
        condition.put("expressStatus", 3);
        condition.put("blockFlag",false);
        ResultData response = expressOrderDao.queryExpressOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<ExpressOrder> list = (List<ExpressOrder>) response.getData();
            response = this.classify_order(list);
            if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
                System.out.println(response.getDescription());
            }else{
                System.out.println("order_express update successfully");
            }
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            System.out.println("none of order_express need to update");
        }
    }

    /**
     *This method is used to classify orders
     *
     */

    public ResultData classify_order(List<ExpressOrder> list){
        List<ExpressOrder> order_status_picked = new ArrayList<>();
        List<ExpressOrder> order_status_shipping = new ArrayList<>();
        List<ExpressOrder> order_status_received = new ArrayList<>();
        List<ExpressOrder> order_status_returned = new ArrayList<>();
        for(ExpressOrder expressOrder : list){
            int currentStatus = expressOrder.getExpressStatus().getValue();
            int queryStatus = currentStatus;
            Map<String, Object> condition = new HashMap<>();
            condition.put("companyId", expressOrder.getCompanyId());
            condition.put("blockFlag", false);
            ResultData response = expressCompanyDao.queryExpressCompany(condition);
            if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
                List<ExpressCompany> listCompany = (List<ExpressCompany>) response.getData();
                ExpressCompany expressCompany = listCompany.get(0);
                response = companyTransfer.transfer(expressCompany.getCompanyCode(), expressOrder.getExpressNo());
                if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
                    queryStatus = (int) response.getData();
                }
            }
            if(currentStatus != queryStatus){
                int value = queryStatus;
                switch(value){
                    case 1 :
                        expressOrder.setExpressStatus(ExpressStatus.PICKED);
                        order_status_picked.add(expressOrder);
                        break;
                    case 2 :
                        expressOrder.setExpressStatus(ExpressStatus.SHIPPING);
                        order_status_shipping.add(expressOrder);
                        break;
                    case 3 :
                        expressOrder.setExpressStatus(ExpressStatus.RECEIVED);
                        order_status_received.add(expressOrder);
                        break;
                    case 4 : order_status_returned.add(expressOrder);
                        expressOrder.setExpressStatus(ExpressStatus.RETURNED);
                        order_status_returned.add(expressOrder);
                        break;
                }
            }
        }
        ResultData response = this.update_order(order_status_picked);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            response.setDescription("order_status_picked update failed");
            return response;
        }
        response = this.update_order(order_status_shipping);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            response.setDescription("order_status_shipping update failed");
            return response;
        }
        response = this.update_order(order_status_received);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            response.setDescription("order_status_received update failed");
            return response;
        }
        response = this.update_order(order_status_returned);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            response.setDescription("order_status_returned update failed");
            return response;
        }
        return response;
    }

    /**
     *This method is used to update orders
     *
     */

    public ResultData update_order(List<ExpressOrder> list){
        ResultData result = new ResultData();
        if(list.size() > 0) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("list", list);
            condition.put("expressStatus", list.get(0).getExpressStatus().getValue());
            condition.put("deliverTime", new Timestamp(System.currentTimeMillis()));
            if(list.get(0).getExpressStatus().getValue() == ExpressStatus.RECEIVED.getValue()){
                condition.put("receiveTime", new Timestamp(System.currentTimeMillis()));
            }
            ResultData response = expressOrderDao.updateExpressOrder(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
            }
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            }
        }
        return result;
    }

}
