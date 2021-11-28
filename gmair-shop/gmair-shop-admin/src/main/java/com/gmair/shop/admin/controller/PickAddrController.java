

package com.gmair.shop.admin.controller;

import java.util.Arrays;
import java.util.Objects;

import javax.validation.Valid;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gmair.shop.common.enums.GmairHttpStatus;
import com.gmair.shop.common.exception.GmairShopGlobalException;
import com.gmair.shop.security.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.gmair.shop.common.util.PageParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gmair.shop.bean.model.PickAddr;
import com.gmair.shop.service.PickAddrService;

import cn.hutool.core.util.StrUtil;



/**
 *
 *
 */
@RestController
@RequestMapping("/shop/manager/shop/pickAddr")
public class PickAddrController {

    @Autowired
    private PickAddrService pickAddrService;

	/**
	 * 分页获取
	 */
    @GetMapping("/page")
	@PreAuthorize("@pms.hasPermission('shop:pickAddr:page')")
	public ResponseEntity<IPage<PickAddr>> page(PickAddr pickAddr,PageParam<PickAddr> page){
		IPage<PickAddr> pickAddrs = pickAddrService.page(page,new LambdaQueryWrapper<PickAddr>()
													.like(StrUtil.isNotBlank(pickAddr.getAddrName()),PickAddr::getAddrName,pickAddr.getAddrName())
													.orderByDesc(PickAddr::getAddrId));
		return ResponseEntity.ok(pickAddrs);
	}

    /**
	 * 获取信息
	 */
	@GetMapping("/info/{id}")
	@PreAuthorize("@pms.hasPermission('shop:pickAddr:info')")
	public ResponseEntity<PickAddr> info(@PathVariable("id") Long id){
		PickAddr pickAddr = pickAddrService.getById(id);
		return ResponseEntity.ok(pickAddr);
	}

	/**
	 * 保存
	 */
	@PostMapping
	@PreAuthorize("@pms.hasPermission('shop:pickAddr:save')")
	public ResponseEntity<Void> save(@Valid @RequestBody PickAddr pickAddr){
		pickAddr.setShopId(SecurityUtils.getSysUser().getShopId());
		pickAddrService.save(pickAddr);
		return ResponseEntity.ok().build();
	}

	/**
	 * 修改
	 */
	@PutMapping
	@PreAuthorize("@pms.hasPermission('shop:pickAddr:update')")
	public ResponseEntity<Void> update(@Valid @RequestBody PickAddr pickAddr){
		PickAddr dbPickAddr = pickAddrService.getById(pickAddr.getAddrId());

		if (!Objects.equals(dbPickAddr.getShopId(),SecurityUtils.getSysUser().getShopId())) {
			throw new GmairShopGlobalException(GmairHttpStatus.UNAUTHORIZED);
		}
		pickAddrService.updateById(pickAddr);
		return ResponseEntity.ok().build();
	}

	/**
	 * 删除
	 */
	@DeleteMapping
	@PreAuthorize("@pms.hasPermission('shop:pickAddr:delete')")
	public ResponseEntity<Void> delete(@RequestBody Long[] ids){
		pickAddrService.removeByIds(Arrays.asList(ids));
		return ResponseEntity.ok().build();
	}
}
