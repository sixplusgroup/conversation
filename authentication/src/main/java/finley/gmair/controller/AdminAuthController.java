package finley.gmair.controller;

import finley.gmair.form.admin.LoginForm;
import finley.gmair.service.AdminService;
import finley.gmair.util.Encryption;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AdminAuthController {
    @Autowired
    private AdminService adminService;

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/admin/login")
    public ResultData login(@RequestBody LoginForm form) {
        ResultData result = new ResultData();
        if (!StringUtils.isEmpty(form.getEmail()) && !StringUtils.isEmpty(form.getPassword())) {
            String email = form.getEmail();
            String password = Encryption.md5(form.getPassword());
            Map<String, Object> condition = new HashMap<>();
            condition.put("email", email);
            condition.put("password", password);
            condition.put("blockFlag", false);
            ResultData response = adminService.fetchAdmin(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
            }
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("Please make sure you have the correct email & password");
            }
            if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to fetch the correct information");
            }
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Please make sure you fill the username and password");
        return result;
    }
}
