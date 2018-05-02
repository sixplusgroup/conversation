package finley.gmair.schedule;

import finley.gmair.dao.ExpressParcelDao;
import finley.gmair.model.express.ExpressParcel;
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
public class ParcelStatusSchedule {

    @Autowired
    private ExpressParcelDao expressParcelDao;

    /**
     *This method is used to update express parcel status every hour
     *
     */
    @Scheduled(cron = "30 * * * * ?")
    public void update(){
        System.out.println(new Timestamp(System.currentTimeMillis()));
        Map<String, Object> condition = new HashMap<>();
        condition.put("expressStatus", 3);
        condition.put("blockFlag",false);
        ResultData response = expressParcelDao.queryExpressParcel(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<ExpressParcel> list = (List<ExpressParcel>) response.getData();
            response = this.classify_parcel(list);
            if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
                System.out.println(response.getDescription());
            }else{
                System.out.println("parcel_express update successfully");
            }
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            System.out.println("none of parcel_express need to update");
        }
    }

    /**
     *This method is used to classify parcels
     *
     */

    public ResultData classify_parcel(List<ExpressParcel> list){
        List<ExpressParcel> parcel_status_picked = new ArrayList<>();
        List<ExpressParcel> parcel_status_shipping = new ArrayList<>();
        List<ExpressParcel> parcel_status_received = new ArrayList<>();
        List<ExpressParcel> parcel_status_returned = new ArrayList<>();
        for(ExpressParcel expressParcel : list){
            /**
             * query status ...  compare status  ...  set status
             *
             */
            //expressParcel.setExpressStatus(ExpressStatus.RECEIVED);
            boolean changed = true;
            if(changed){
                int value = expressParcel.getExpressStatus().getValue();
                switch(value){
                    case 1 : parcel_status_picked.add(expressParcel);
                    case 2 : parcel_status_shipping.add(expressParcel);
                    case 3 : parcel_status_received.add(expressParcel);
                    case 4 : parcel_status_returned.add(expressParcel);
                }
            }
        }
        ResultData response = this.update_parcel(parcel_status_picked);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            response.setDescription("parcel_status_picked update failed");
            return response;
        }
        response = this.update_parcel(parcel_status_shipping);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            response.setDescription("parcel_status_shipping update failed");
            return response;
        }
        response = this.update_parcel(parcel_status_received);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            response.setDescription("parcel_status_received update failed");
            return response;
        }
        response = this.update_parcel(parcel_status_returned);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
            response.setDescription("parcel_status_returned update failed");
            return response;
        }
        return response;
    }

    /**
     *This method is used to update parcels
     *
     */

    public ResultData update_parcel(List<ExpressParcel> list){
        ResultData result = new ResultData();
        if(list.size() > 0) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("list", list);
            condition.put("expressStatus", list.get(0).getExpressStatus().getValue());
            ResultData response = expressParcelDao.updateExpressParcel(condition);
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
