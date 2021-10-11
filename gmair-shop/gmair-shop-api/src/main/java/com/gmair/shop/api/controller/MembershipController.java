package com.gmair.shop.api.controller;

import com.gmair.shop.bean.model.User;
import com.gmair.shop.security.util.SecurityUtils;
import com.gmair.shop.service.UserService;
import com.gmair.shop.service.feign.MembershipFeignService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Joby
 */
@RestController
@RequestMapping("/p/membership")
@AllArgsConstructor
public class MembershipController {

    private final UserService userService;

    private final MembershipFeignService membershipFeignService;

    /**
     * @Description user join membership
     * @Date  2021/10/11 14:39
     * @return org.springframework.http.ResponseEntity<java.lang.Void>
     */
    @PostMapping("/joinMemebership")
    public ResponseEntity<Void> joinMembership(){
        // feign: make the user be a membership
        String userId = SecurityUtils.getUser().getUserId();
        User user = userService.getUserByUserId(userId);
        ResponseEntity<Void> response =membershipFeignService.enrollMembership(user.getConsumerId());
        return response;
    }
}
