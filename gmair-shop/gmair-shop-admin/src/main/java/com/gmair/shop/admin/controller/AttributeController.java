

package com.gmair.shop.admin.controller;

import java.util.Objects;

import javax.validation.Valid;

import com.gmair.shop.common.util.PageParam;

import com.gmair.shop.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.gmair.shop.bean.enums.ProdPropRule;
import com.gmair.shop.bean.model.ProdProp;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.service.ProdPropService;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 参数管理
 *
 */
@RestController
@RequestMapping("/admin/attribute")
public class AttributeController {

    @Autowired
    private ProdPropService prodPropService;

	/**
	 * 分页获取
	 */
    @GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('admin:attribute:page')")
	public ResponseEntity<IPage<ProdProp>> page(ProdProp prodProp,PageParam<ProdProp> page){
    	prodProp.setRule(ProdPropRule.ATTRIBUTE.value());
    	prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
		IPage<ProdProp> prodPropIPage = prodPropService.pagePropAndValue(prodProp,page);
		return ResponseEntity.ok(prodPropIPage);
	}

    /**
	 * 获取信息
	 */
	@GetMapping("/info/{id}")
	@PreAuthorize("@pms.hasPermission('admin:attribute:info')")
	public ResponseEntity<ProdProp> info(@PathVariable("id") Long id){
		ProdProp prodProp = prodPropService.getById(id);
		return ResponseEntity.ok(prodProp);
	}

	/**
	 * 保存
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('admin:attribute:save')")
	public ResponseEntity<Void> save(@Valid ProdProp prodProp){
		prodProp.setRule(ProdPropRule.ATTRIBUTE.value());
		prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
		prodPropService.saveProdPropAndValues(prodProp);
		return ResponseEntity.ok().build();
	}

	/**
	 * 修改
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('admin:attribute:update')")
	public ResponseEntity<Void> update(@Valid ProdProp prodProp){
		ProdProp dbProdProp = prodPropService.getById(prodProp.getPropId());
		if (!Objects.equals(dbProdProp.getShopId(), SecurityUtils.getSysUser().getShopId())) {
			throw new GmairShopGlobalException("没有权限获取该商品规格信息");
		}
		prodProp.setRule(ProdPropRule.ATTRIBUTE.value());
		prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
		prodPropService.updateProdPropAndValues(prodProp);
		return ResponseEntity.ok().build();
	}

	/**
	 * 删除
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('admin:attribute:delete')")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		prodPropService.deleteProdPropAndValues(id,ProdPropRule.ATTRIBUTE.value(),SecurityUtils.getSysUser().getShopId());
		return ResponseEntity.ok().build();
	}
}
