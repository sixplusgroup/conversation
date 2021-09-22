package com.gmair.shop.api.controller;

import cn.hutool.core.util.StrUtil;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.service.MembershipService;
import io.swagger.annotations.Api;
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
@Api(tags = "会员操作接口")
public class MembershipController {
    private final MembershipService membershipService;
//    第一次登录的时候, 用户加入会员
//    @PostMapping(value = "/enroll")
//    public ResponseEntity<Boolean> enroll(String userId){
//        if(StrUtil.isBlank(userId)){
//            throw new GmairShopGlobalException("用户ID不可为空");
//        }
//        return ResponseEntity.ok(membershipService.createMembership(userId));
//
//    }

}
