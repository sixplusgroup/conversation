package finley.gmair.schedule;

import finley.gmair.dao.ExpressOrderDao;
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
public class statusSchedule {

    @Autowired
    private ExpressOrderDao expressOrderDao;

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
            /**
             * query status ...  compare status  ...  set status
             * 
             */
            //expressOrder.setExpressStatus(ExpressStatus.RECEIVED);
            boolean changed = true;
            if(changed){
                int value = expressOrder.getExpressStatus().getValue();
                switch(value){
                    case 1 : order_status_picked.add(expressOrder);
                    case 2 : order_status_shipping.add(expressOrder);
                    case 3 : order_status_received.add(expressOrder);
                    case 4 : order_status_returned.add(expressOrder);
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
                System.out.println(response.getDescription());
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                System.out.println(response.getDescription());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            }
        }
        return result;
    }

}
