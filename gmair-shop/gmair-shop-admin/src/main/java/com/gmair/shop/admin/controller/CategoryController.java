

package com.gmair.shop.admin.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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


import com.gmair.shop.bean.model.Category;
import com.gmair.shop.common.annotation.SysLog;
import com.gmair.shop.service.CategoryService;


/**
 * 分类管理
 *
 *
 */
@RestController
@RequestMapping("/prod/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	/**
	 * 获取菜单页面的表
	 * @return
	 */
	@GetMapping("/table")
	@PreAuthorize("@pms.hasPermission('prod:category:page')")
	public ResponseEntity<List<Category>> table(){
		List<Category> categoryMenuList = categoryService.tableCategory(SecurityUtils.getSysUser().getShopId());
		return ResponseEntity.ok(categoryMenuList);
	}

	/**
	 * 获取分类信息
	 */
	@GetMapping("/info/{categoryId}")
	public ResponseEntity<Category> info(@PathVariable("categoryId") Long categoryId){
		Category category = categoryService.getById(categoryId);
		return ResponseEntity.ok(category);
	}



	/**
	 * 保存分类
	 */
	@SysLog("保存分类")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('prod:category:save')")
	public ResponseEntity<Void> save(@RequestBody Category category){
		category.setShopId(SecurityUtils.getSysUser().getShopId());
		category.setRecTime(new Date());
		Category categoryName = categoryService.getOne(new LambdaQueryWrapper<Category>().eq(Category::getCategoryName,category.getCategoryName())
				.eq(Category::getShopId,category.getShopId()));
		if(categoryName != null){
			throw new GmairShopGlobalException("类目名称已存在！");
		}
		categoryService.saveCategroy(category);
		return ResponseEntity.ok().build();
	}

	/**
	 * 更新分类
	 */
	@SysLog("更新分类")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('prod:category:update')")
	public ResponseEntity<String> update(@RequestBody Category category){
		category.setShopId(SecurityUtils.getSysUser().getShopId());
		if (Objects.equals(category.getParentId(),category.getCategoryId())) {
			return ResponseEntity.badRequest().body("分类的上级不能是自己本身");
		}
		Category categoryName = categoryService.getOne(new LambdaQueryWrapper<Category>().eq(Category::getCategoryName,category.getCategoryName())
				.eq(Category::getShopId,category.getShopId()).ne(Category::getCategoryId,category.getCategoryId()));
		if(categoryName != null){
			throw new GmairShopGlobalException("类目名称已存在！");
		}
		Category categoryDB = categoryService.getById(category.getCategoryId());
		// 如果从下线改成正常，则需要判断上级的状态
		if (Objects.equals(categoryDB.getStatus(),0) && Objects.equals(category.getStatus(),1) && !Objects.equals(category.getParentId(),0L)){
			Category parentCategory = categoryService.getOne(new LambdaQueryWrapper<Category>().eq(Category::getCategoryId, category.getParentId()));
			if(Objects.isNull(parentCategory) || Objects.equals(parentCategory.getStatus(),0)){
				// 修改失败，上级分类不存在或者不为正常状态
				throw new GmairShopGlobalException("修改失败，上级分类不存在或者不为正常状态");
			}
		}
		categoryService.updateCategroy(category);
		return ResponseEntity.ok().build();
	}

	/**
	 * 删除分类
	 */
	@SysLog("删除分类")
	@DeleteMapping("/{categoryId}")
	@PreAuthorize("@pms.hasPermission('prod:category:delete')")
	public ResponseEntity<String> delete(@PathVariable("categoryId") Long categoryId){
		if (categoryService.count(new LambdaQueryWrapper<Category>().eq(Category::getParentId,categoryId)) >0) {
			return ResponseEntity.badRequest().body("请删除子分类，再删除该分类");
		}
		categoryService.deleteCategroy(categoryId);
		return ResponseEntity.ok().build();
	}

	/**
	 * 所有的
	 */
	@GetMapping("/listCategory")
	public ResponseEntity<List<Category>> listCategory(){

		return ResponseEntity.ok(categoryService.list(new LambdaQueryWrapper<Category>()
														.le(Category::getGrade, 2)
														.eq(Category::getShopId, SecurityUtils.getSysUser().getShopId())
														.orderByAsc(Category::getSeq)));
	}

	/**
	 * 所有的产品分类
	 */
	@GetMapping("/listProdCategory")
	public ResponseEntity<List<Category>> listProdCategory(){
    	List<Category> categories = categoryService.treeSelect(SecurityUtils.getSysUser().getShopId(),2);
		return ResponseEntity.ok(categories);
	}
}
