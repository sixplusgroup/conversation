package com.gmair.shop.admin.controller;

import cn.hutool.core.util.StrUtil;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.service.MembershipService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Joby
 */
@RestController
@RequestMapping("/membership/membership")
@AllArgsConstructor
public class MembershipController {
    private final MembershipService membershipService;


}
