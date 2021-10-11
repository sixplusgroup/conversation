package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.order.PlatformOrder;
import finley.gmair.service.LocationService;
import finley.gmair.service.OrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order/fit")
public class FitLatLonController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private LocationService locationService;

    @RequestMapping(method = RequestMethod.GET, value = "/latlon")
    public ResultData fitLatLon() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = orderService.fetchPlatformOrder(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }

        List<PlatformOrder> platformOrderList = (List<PlatformOrder>) response.getData();
        List<PlatformOrder> failList = new ArrayList<>();
        for (PlatformOrder order : platformOrderList) {
            //1.过滤掉经纬度有值的数据
            if (order.getLatitude() != 0 || order.getLongitude() != 0)
                continue;
            //2.解析经纬度,并填充经纬度
            String address = order.getAddress();
            ResultData locationResult = locationService.geocoder(address);
            if (locationResult.getResponseCode() == ResponseCode.RESPONSE_OK) {
                JSONObject address_components = (JSON.parseObject(JSON.toJSONString(locationResult.getData()))).getJSONObject("address_components");
                String province = address_components.getString("province");
                String city = address_components.getString("city");
                String district = address_components.getString("district");

                JSONObject location = (JSON.parseObject(JSON.toJSONString(locationResult.getData()))).getJSONObject("location");
                Double latitude = 0.0,longitude = 0.0;
                try{
                    latitude = Double.parseDouble(location.getString("lat"));
                    longitude = Double.parseDouble(location.getString("lng"));
                }catch (Exception e){
                    e.printStackTrace();
                }

                PlatformOrder orderUpdate = new PlatformOrder();
                orderUpdate.setLocation(province, city, district, latitude, longitude);
                orderUpdate.setTotalPrice(order.getTotalPrice());
                orderUpdate.setStatus(order.getStatus());
                orderUpdate.setOrderId(order.getOrderId());
                response = orderService.updatePlatformOrder(orderUpdate);
                if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
                    failList.add(order);
                }
            }
        }
        result.setData(failList);
        result.setDescription("the data in response is the order whose lat=0 and lng=0 but fail to be filled");
        return result;
    }


}
