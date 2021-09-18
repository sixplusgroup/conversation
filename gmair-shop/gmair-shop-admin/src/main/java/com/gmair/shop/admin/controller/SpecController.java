

package com.gmair.shop.admin.controller;

import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gmair.shop.common.enums.GmairHttpStatus;

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

import com.gmair.shop.bean.enums.ProdPropRule;
import com.gmair.shop.bean.model.ProdProp;
import com.gmair.shop.bean.model.ProdPropValue;
import com.gmair.shop.common.exception.GmairShopBindException;
import com.gmair.shop.service.ProdPropService;
import com.gmair.shop.service.ProdPropValueService;

import javax.validation.Valid;

/**
 * 规格管理
 *
 *
 */
@RestController
@RequestMapping("/prod/spec")
public class SpecController {

    @Autowired
    private ProdPropService prodPropService;
    @Autowired
    private ProdPropValueService prodPropValueService;

    /**
     * 分页获取
     */
    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('prod:spec:page')")
    public ResponseEntity<IPage<ProdProp>> page(ProdProp prodProp,PageParam<ProdProp> page) {
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
        IPage<ProdProp> list = prodPropService.pagePropAndValue(prodProp, page);
        return ResponseEntity.ok(list);
    }


    /**
     * 获取所有的规格
     */
    @GetMapping("/list")
    public ResponseEntity<List<ProdProp>> list() {
        List<ProdProp> list = prodPropService.list(new LambdaQueryWrapper<ProdProp>().eq(ProdProp::getRule, ProdPropRule.SPEC.value()).eq(ProdProp::getShopId, SecurityUtils.getSysUser().getShopId()));
        return ResponseEntity.ok(list);
    }

    /**
     * 根据规格id获取规格值
     */
    @GetMapping("/listSpecValue/{specId}")
    public ResponseEntity<List<ProdPropValue>> listSpecValue(@PathVariable("specId") Long specId) {
        List<ProdPropValue> list = prodPropValueService.list(new LambdaQueryWrapper<ProdPropValue>().eq(ProdPropValue::getPropId, specId));
        return ResponseEntity.ok(list);
    }

    /**
     * 保存
     */
    @PostMapping
    @PreAuthorize("@pms.hasPermission('prod:spec:save')")
    public ResponseEntity<Void> save(@Valid @RequestBody ProdProp prodProp) {
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
        prodPropService.saveProdPropAndValues(prodProp);
        return ResponseEntity.ok().build();
    }

    /**
     * 修改
     */
    @PutMapping
    @PreAuthorize("@pms.hasPermission('prod:spec:update')")
    public ResponseEntity<Void> update(@Valid @RequestBody ProdProp prodProp) {
        ProdProp dbProdProp = prodPropService.getById(prodProp.getPropId());
        if (!Objects.equals(dbProdProp.getShopId(), SecurityUtils.getSysUser().getShopId())) {
            throw new GmairShopBindException("没有权限获取该商品规格信息");
        }
        prodProp.setRule(ProdPropRule.SPEC.value());
        prodProp.setShopId(SecurityUtils.getSysUser().getShopId());
        prodPropService.updateProdPropAndValues(prodProp);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('prod:spec:delete')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        prodPropService.deleteProdPropAndValues(id, ProdPropRule.SPEC.value(), SecurityUtils.getSysUser().getShopId());
        return ResponseEntity.ok().build();
    }

    /**
     * 根据获取规格值最大的自增id
     */
    @GetMapping("/listSpecMaxValueId")
    public ResponseEntity<Long> listSpecMaxValueId() {
        ProdPropValue propValue = prodPropValueService.getOne(new LambdaQueryWrapper<ProdPropValue>()
                .orderByDesc(ProdPropValue::getValueId).last("limit 1"));
        return ResponseEntity.ok(Objects.isNull(propValue) ? 0L : propValue.getValueId());
    }

}
