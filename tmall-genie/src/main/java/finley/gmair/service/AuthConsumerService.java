package finley.gmair.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("consumer-auth-agent")
public interface AuthConsumerService {

    @GetMapping("/oauth/authorize")
    feign.Response authorize(@RequestParam("response_type") String responseType,
                             @RequestParam("client_id") String clientId,
                             @RequestParam("redirect_uri") String redirectUri,
                             @RequestParam("access_token") String accessToken);

    @PostMapping("/oauth/token")
    Map<String, String> getToken(@RequestParam("code") String code,
                                 @RequestParam("grant_type") String grantType,
                                 @RequestParam("redirect_uri") String redirectUri,
                                 @RequestParam("client_id") String clientId,
                                 @RequestParam("client_secret") String clientSecret);
}
