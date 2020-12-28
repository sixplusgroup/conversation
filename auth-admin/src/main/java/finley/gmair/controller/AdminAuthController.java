package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import finley.gmair.form.admin.AdminForm;
import finley.gmair.form.admin.AdminPartInfoQuery;
import finley.gmair.form.admin.LoginForm;
import finley.gmair.model.admin.Admin;
import finley.gmair.service.AdminService;
import finley.gmair.util.Encryption;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.admin.AdminPartInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
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
                result.setData(response.getData());
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

    @RequestMapping(method = RequestMethod.POST, value = "/admin/create")
    public ResultData create(AdminForm form) {
        ResultData result = new ResultData();
        if (!StringUtils.isEmpty(form.getEmail()) && !StringUtils.isEmpty(form.getName()) && !StringUtils.isEmpty(form.getPassword())&& !StringUtils.isEmpty(form.getRole())) {
            Admin admin = new Admin(form.getEmail(), form.getName(), Encryption.md5(form.getPassword()), form.getRole());
            ResultData response = adminService.createAdmin(admin);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription(new StringBuffer("Admin successfully created, ").append(JSON.toJSONString(response.getData())).toString());
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(new StringBuffer("Fail to create admin with info: ").append(JSON.toJSONString(admin)).toString());
            }
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/admin")
    public Principal user(Principal user) {
        return user;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAdmin/byAccount")
    ResultData getAdminByAccount(String account){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(account)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please submit account");
            return result;
        }
        Map<String,Object> condition = new HashMap<>();
        condition.put("blockFlag",false);
        condition.put("email",account);
        ResultData response = adminService.fetchAdmin(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("account is null");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("query error");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    /**
     * 根据query给出的条件查询符合条件的账户信息
     * @param query 查询条件对象
     * @return 查询结果
     */
    @PostMapping("/admin/accounts")
    public ResultData queryAdminAccounts(@RequestBody AdminPartInfoQuery query) {
        ResultData res = new ResultData();

        Map<String, Object> resData = new HashMap<>();
        List<AdminPartInfoVo> store = adminService.fetchAdminAccounts(query);
        resData.put("size", store.size());
        resData.put("list", store);

        res.setData(resData);
        return res;
    }
}
