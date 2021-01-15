package finley.gmair.controller;

import finley.gmair.form.admin.AdminPartInfoQuery;
import finley.gmair.form.consumer.ConsumerPartInfoQuery;
import finley.gmair.service.AuthService;
import finley.gmair.service.ConsumerService;
import finley.gmair.util.ResultData;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: Bright Chan
 * @date: 2020/12/26 14:54
 * @description: UserController
 */

@CrossOrigin
@RestController
@RequestMapping("/management/user")
public class UserController {

    @Resource
    private AuthService authService;

    @Resource
    private ConsumerService consumerService;

    @PostMapping("/admin/accounts")
    public ResultData queryAdminAccounts(@RequestBody @Validated AdminPartInfoQuery query) {
        return authService.queryAdminAccounts(query);
    }

    @PostMapping("/consumer/accounts")
    public ResultData queryConsumerAccounts(@RequestBody @Validated ConsumerPartInfoQuery query) {
        return consumerService.queryConsumerAccounts(query);
    }
}
