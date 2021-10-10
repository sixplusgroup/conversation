package com.gmair.shop.api.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gmair.shop.bean.app.dto.ProductDto;
import com.gmair.shop.bean.model.Product;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.common.util.PageParam;
import com.gmair.shop.service.MembershipService;
import com.gmair.shop.service.ProductService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final ProductService prodService;

    @GetMapping("/center/listProds")
    public ResponseEntity<IPage<ProductDto>> listMembershipProds(PageParam<Product> page){
        IPage<ProductDto> productDtoIPage = prodService.pageBymembershipProdsList(page);
        return ResponseEntity.ok(productDtoIPage);
    }

}
