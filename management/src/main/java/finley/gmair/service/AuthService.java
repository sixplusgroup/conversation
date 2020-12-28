package finley.gmair.service;


import finley.gmair.form.admin.AdminPartInfoQuery;
import finley.gmair.util.ResultData;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("admin-auth-agent")
public interface AuthService {

    @GetMapping("/auth/getAdmin/byAccount")
    ResultData getAdmin(@RequestParam("account") String account);

    @PostMapping("/auth/admin/accounts")
    ResultData queryAdminAccounts(@RequestBody AdminPartInfoQuery query);
}