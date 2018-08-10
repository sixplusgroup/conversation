package finley.gmair.controller;

import finley.gmair.form.consumer.LocationForm;
import finley.gmair.service.AuthConsumerService;
import finley.gmair.service.MachineService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * ConsumerController is responsible for all operations done by consumer regarding his/her account information,
 * e.g. Username, password, phone number
 */
@RestController
@RequestMapping("/reception/consumer")
@CrossOrigin
public class ConsumerController {

    @Autowired
    private AuthConsumerService authConsumerService;

    @Autowired
    private MachineService machineService;

    @RequestMapping(value= "/check/right/qrcode",method = RequestMethod.GET)
    public ResultData checkUserAccessToQRcode(String qrcode){
        ResultData result = new ResultData();
        //check empty
        if(StringUtils.isEmpty(qrcode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        //check this consumerId and the qrcode has been binded.
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String consumerId = (String) authConsumerService.getConsumerId(phone).getData();

        ResultData response = machineService.checkConsumerAccesstoQRcode(consumerId,qrcode);
        if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("this consumer has access to the machine(qrcode).");
            return result;
        }else if(response.getResponseCode() ==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to check the consumerId access to the qrcode");
            return result;
        }else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("this consumer has no access to the machine");
            return result;
        }

        return result;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ResultData profile() {
        ResultData result = new ResultData();
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResultData response = authConsumerService.profile(phone);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not found profile with this phone");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to found the profile");
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(value="/wechat/bind",method = RequestMethod.POST)
    public ResultData bindWechat(String openid){
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(openid)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResultData response = authConsumerService.bindWechat(phone,openid);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to bind whechat");
        }
        return result;
    }

    @RequestMapping(value="/wechat/unbind",method = RequestMethod.POST)
    public ResultData unbindWechat(){
        ResultData result = new ResultData();
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResultData response = authConsumerService.unbindWechat(phone);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to unbind whechat");
        }
        return result;
    }

    @RequestMapping(value = "/edit/username", method = RequestMethod.POST)
    public ResultData editUsername(String username) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(username)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResultData response = authConsumerService.editUsername(phone, username);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to edit username.");
        }
        return result;
    }

    @RequestMapping(value = "/edit/phone", method = RequestMethod.POST)
    public ResultData editPhone(String newPhone) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(newPhone)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        String oldPhone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResultData response = authConsumerService.editPhone(oldPhone, newPhone);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to edit phone.");
        }
        return result;
    }

    @RequestMapping(value = "/edit/location", method = RequestMethod.POST)
    public ResultData editLocation(LocationForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getProvince()) || StringUtils.isEmpty(form.getCity()) || StringUtils.isEmpty(form.getDistrict()) || StringUtils.isEmpty(form.getDetail())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        String phone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResultData response = authConsumerService.editLocation(phone, form.getProvince().trim(), form.getCity().trim(), form.getDistrict().trim(), form.getDetail().trim(), form.isPreferred());
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to edit location");
        }
        return result;
    }

    @RequestMapping(value = "/check/phone",  method = RequestMethod.GET)
    public ResultData checkPhoneUsed(String phone) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(phone)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all required information");
            return result;
        }
        ResultData response = authConsumerService.checkPhoneExist(phone);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("The phone number ").append(phone).append(" has already been registered.").toString());
        } else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(("The phone number is available for registration"));
        }else{
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to check whether the phone is used or not.");
        }
        return result;
    }
}
