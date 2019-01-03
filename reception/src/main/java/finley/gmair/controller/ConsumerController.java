package finley.gmair.controller;

import finley.gmair.form.consumer.LocationForm;
import finley.gmair.pool.ReceptionPool;
import finley.gmair.service.AuthConsumerService;
import finley.gmair.service.LogService;
import finley.gmair.service.MachineService;
import finley.gmair.util.IPUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private LogService logService;

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
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResultData response = authConsumerService.profile(consumerId);
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
    public ResultData bindWechat(String openid, HttpServletRequest request){
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(openid)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResultData response = authConsumerService.bindWechat(consumerId,openid);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to bind whechat");
            ReceptionPool.getLogExecutor().execute(new Thread(() -> {
                logService.createUser(consumerId, "wechatBind", new StringBuffer("User:").append(consumerId).append(" bind wechat with id ").append(openid).toString(), IPUtil.getIP(request));
            }));
        }
        return result;
    }

    @RequestMapping(value="/wechat/unbind",method = RequestMethod.POST)
    public ResultData unbindWechat(HttpServletRequest request){
        ResultData result = new ResultData();
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResultData response = authConsumerService.unbindWechat(consumerId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to unbind whechat");
            ReceptionPool.getLogExecutor().execute(new Thread(() -> {
                logService.createUser(consumerId, "wechatUnBind", new StringBuffer("User:").append(consumerId).append(" unbind wechat").toString(), IPUtil.getIP(request));
            }));
        }
        return result;
    }

    @RequestMapping(value = "/edit/username", method = RequestMethod.POST)
    public ResultData editUsername(String username, HttpServletRequest request) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(username)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResultData response = authConsumerService.editUsername(consumerId, username);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to edit username.");
            ReceptionPool.getLogExecutor().execute(new Thread(() -> {
                logService.createUser(consumerId, "editUserName", new StringBuffer("User:").append(consumerId).append(" edit user name to ").append(username).toString(), IPUtil.getIP(request));
            }));
        }
        return result;
    }

    @RequestMapping(value = "/edit/phone", method = RequestMethod.POST)
    public ResultData editPhone(String newPhone, HttpServletRequest request) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(newPhone)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResultData response = authConsumerService.editPhone(consumerId, newPhone);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to edit phone.");
            ReceptionPool.getLogExecutor().execute(new Thread(() -> {
                logService.createUser(consumerId, "editUserPhone", new StringBuffer("User:").append(consumerId).append(" edit user phone to ").append(newPhone).toString(), IPUtil.getIP(request));
            }));
        }
        return result;
    }

    @RequestMapping(value = "/edit/location", method = RequestMethod.POST)
    public ResultData editLocation(LocationForm form, HttpServletRequest request) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getProvince()) || StringUtils.isEmpty(form.getCity()) || StringUtils.isEmpty(form.getDistrict()) || StringUtils.isEmpty(form.getDetail())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide all information");
            return result;
        }
        String consumerId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResultData response = authConsumerService.editLocation(consumerId, form.getProvince().trim(), form.getCity().trim(), form.getDistrict().trim(), form.getDetail().trim(), form.isPreferred());
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to edit location");
            ReceptionPool.getLogExecutor().execute(new Thread(() -> {
                logService.createUser(consumerId, "editLocation", new StringBuffer("User:").append(consumerId).append(" edit user location to ").append(form.toString()).toString(), IPUtil.getIP(request));
            }));
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
