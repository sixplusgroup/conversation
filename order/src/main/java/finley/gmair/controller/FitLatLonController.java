package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.order.PlatformOrder;
import finley.gmair.service.LocationService;
import finley.gmair.service.OrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        for (PlatformOrder order : platformOrderList) {
            if (order.getLatitude() != 0 || order.getLongitude() != 0)
                continue;
            String address = order.getAddress();
            ResultData locationResult = locationService.geocoder(address);
            if (locationResult.getResponseCode() == ResponseCode.RESPONSE_OK) {
                JSONObject address_components = (JSON.parseObject(JSON.toJSONString(locationResult.getData()))).getJSONObject("address_components");
                String province = address_components.getString("province");
                String city = address_components.getString("city");
                String district = address_components.getString("district");

                JSONObject location = (JSON.parseObject(JSON.toJSONString(locationResult.getData()))).getJSONObject("location");
                double latitude = Double.parseDouble(location.getString("latitude"));
                double longitude = Double.parseDouble(location.getString("longitude"));

                PlatformOrder orderUpdate = new PlatformOrder();
                orderUpdate.setLocation(province, city, district, latitude, longitude);
                orderUpdate.setTotalPrice(order.getTotalPrice());
                orderUpdate.setStatus(order.getStatus());
                orderUpdate.setOrderId(order.getOrderId());
                response = orderService.updatePlatformOrder(orderUpdate);
            }

        }
        return result;
    }


}
