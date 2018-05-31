package finley.gmair.schedule;

import finley.gmair.company.CompanyTransfer;
import finley.gmair.dao.ExpressCompanyDao;
import finley.gmair.dao.ExpressOrderDao;
import finley.gmair.dao.ExpressParcelDao;
import finley.gmair.model.express.ExpressCompany;
import finley.gmair.model.express.ExpressOrder;
import finley.gmair.model.express.ExpressParcel;
import finley.gmair.model.express.ExpressStatus;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParcelStatusSchedule {

    @Autowired
    private ExpressParcelDao expressParcelDao;

    @Autowired
    private ExpressOrderDao expressOrderDao;

    @Autowired
    private ExpressCompanyDao expressCompanyDao;

    @Autowired
    private CompanyTransfer companyTransfer;

    /**
     *This method is used to update express parcel status every hour
     *
     */
    public ResultData update(){
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("expressStatus", 3);
        condition.put("blockFlag",false);
        ResultData response = expressParcelDao.queryExpressParcel(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<ExpressParcel> list = (List<ExpressParcel>) response.getData();
            response = this.classify_parcel(list);
            if(response.getResponseCode() != ResponseCode.RESPONSE_OK){
                result.setDescription(response.getDescription());
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            }else{
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("parcel_express update successfully");
            }
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("none of parcel_express need to update");
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
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
            int currentStatus = expressParcel.getExpressStatus().getValue();
            int queryStatus = currentStatus;
            Map<String, Object> condition = new HashMap<>();
            condition.put("expressId", expressParcel.getParentExpress());
            condition.put("blockFlag", false);
            ResultData response = expressOrderDao.queryExpressOrder(condition);
            if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
                List<ExpressOrder> listOrder = (List<ExpressOrder>) response.getData();
                ExpressOrder expressOrder = listOrder.get(0);
                condition.remove("expressId");
                condition.put("companyId", expressOrder.getCompanyId());
                response = expressCompanyDao.queryExpressCompany(condition);
                if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
                    List<ExpressCompany> listCompany = (List<ExpressCompany>) response.getData();
                    ExpressCompany expressCompany = listCompany.get(0);
                    response = companyTransfer.transfer(expressCompany.getCompanyCode(), expressParcel.getExpressNo());
                    if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
                        queryStatus = (int) response.getData();
                    }
                }

            }
            if(currentStatus != queryStatus){
                int value = queryStatus;
                switch(value){
                    case 1 :
                        expressParcel.setExpressStatus(ExpressStatus.PICKED);
                        parcel_status_picked.add(expressParcel);
                        break;
                    case 2 :
                        expressParcel.setExpressStatus(ExpressStatus.SHIPPING);
                        parcel_status_shipping.add(expressParcel);
                        break;
                    case 3 :
                        expressParcel.setExpressStatus(ExpressStatus.RECEIVED);
                        parcel_status_received.add(expressParcel);
                        break;
                    case 4 :
                        expressParcel.setExpressStatus(ExpressStatus.RETURNED);
                        parcel_status_returned.add(expressParcel);
                        break;
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
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            }
        }
        return result;
    }

}
