package finley.gmair.controller;

import finley.gmair.form.drift.DriftAddressForm;
import finley.gmair.model.drift.DriftAddress;
import finley.gmair.service.AddressService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/drift/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping(value = "/create")
    public ResultData createAddress(DriftAddressForm form) {
        ResultData result = new ResultData();
        if (StringUtil.isEmpty(form.getConsumerId(), form.getConsignee(), form.getProvince(), form.getCity(),
                form.getDistrict(), form.getAddressDetail(), form.getPhone()) && StringUtils.isEmpty(form.getDefaultAddress())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        String consumerId = form.getConsumerId();
        String consignee = form.getConsignee();
        String province = form.getProvince();
        String city = form.getCity();
        String district = form.getDistrict();
        String addressDetail = form.getAddressDetail();
        String phone = form.getPhone();
        int defaultAddress = form.getDefaultAddress();
        DriftAddress address = new DriftAddress(consumerId, consignee, province, city, district, addressDetail, phone,defaultAddress);
        ResultData response = addressService.createAddress(address);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器正忙，请稍后重试");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @GetMapping(value = "/list/byopenid")
    public ResultData getList(String consumerId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("consumerId", consumerId);
        ResultData response = addressService.fetchAddress(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No address found with the user");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to get address with the user");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
        }
        return result;
    }

    /**
     * The function is called to update user address's base information
     * @param consumerId(用户Id)
     * @param addressId(当前地址Id)
     * @param consignee
     * @param province
     * @param city
     * @param district
     * @param addressDetail
     * @param phone
     * @param defaultAddress
     */
    @PostMapping(value = "/update")
    public ResultData updateBaseInformation(String consumerId, String addressId, String consignee, String province,
                                            String city, String district, String addressDetail, String phone, int defaultAddress) {
        ResultData result = new ResultData();
        if (StringUtil.isEmpty(consumerId, addressId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please ensure you have filled all the required fields");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(defaultAddress) && defaultAddress == 1) {
            condition.put("consumerId", consumerId);
            condition.put("blockFlag", false);
            ResultData response = addressService.fetchAddress(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                List<DriftAddress> list = (List<DriftAddress>) response.getData();
                new Thread(() -> {
                    for (DriftAddress item : list) {
                        if (item.getDefaultAddress() == 1) {
                            item.setDefaultAddress(0);
                            addressService.updateAddress(item);
                        }
                    }
                }).start();
            }
        }
        condition.put("consumerId", consumerId);
        condition.put("addressId", addressId);
        condition.put("blockFlag", false);
        ResultData response = addressService.fetchAddress(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("未获取到该用户的详细地址信息");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器正忙，请稍后重试");
            return result;
        }
        DriftAddress address = ((List<DriftAddress>) response.getData()).get(0);
        if (!StringUtils.isEmpty(defaultAddress)) {
            address.setDefaultAddress(defaultAddress);
        }
        if (!StringUtils.isEmpty(consignee)) {
            address.setConsignee(consignee);
        }
        if (!StringUtils.isEmpty(province)) {
            address.setProvince(province);
        }
        if (!StringUtils.isEmpty(city)) {
            address.setCity(city);
        }
        if (!StringUtils.isEmpty(district)) {
            address.setDistrict(district);
        }
        if (!StringUtils.isEmpty(addressDetail)) {
            address.setAddressDetail(addressDetail);
        }
        if (!StringUtils.isEmpty(phone)) {
            address.setPhone(phone);
        }
        response = addressService.updateAddress(address);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("用户地址信息更新失败");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

}
