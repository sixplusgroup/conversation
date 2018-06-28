package finley.gmair.controller;

import com.ctc.wstx.util.StringUtil;
import finley.gmair.form.consumer.LocationForm;
import finley.gmair.service.AuthConsumerService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;

/**
 * ConsumerController is responsible for all operations done by consumer regarding his/her account information,
 * e.g. Username, password, phone number
 */
@RestController
@RequestMapping("/reception/consumer")
public class ConsumerController {

    @Autowired
    private AuthConsumerService authConsumerService;

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

    @RequestMapping(value = "edit/phone", method = RequestMethod.POST)
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

    @RequestMapping(value = "edit/location", method = RequestMethod.POST)
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

    @RequestMapping(value = "check/phone",  method = RequestMethod.GET)
    public ResultData checkPhoneUsed(String phone) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(phone)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        ResultData response = authConsumerService.checkPhoneExist(phone);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("the phone number has been used");
        } else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(("the phone number has not been used"));
        }else{
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
        }
        return result;
    }
}
