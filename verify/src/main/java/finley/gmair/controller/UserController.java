package finley.gmair.controller;

import finley.gmair.service.VerificationService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify/user")
public class UserController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/check")
    public ResultData check(String name, String idno) {
        ResultData result = new ResultData();
        if (StringUtil.isEmpty(name, idno)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return error(result, "请提供用户的姓名和身份证号");
        }
        ResultData response = verificationService.verify(idno, name);
        if ((Boolean) response.getData() == true) {
            return ok(result, "身份信息核验成功");
        } else {
            return empty(result, "未能查询到身份证号为: " + idno + "，姓名为: " + name + "的用户");
        }
    }
}
