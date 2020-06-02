package com.seirin.o2o.service;

import java.util.List;

import com.seirin.o2o.entity.ShopCategory;

/**
 * @Description: 店鋪类别业务接口
 * @author seirin
 *
 */
public interface ShopCategoryService {

	/**
	 * 条件获取店铺类别分页列表
	 * 
	 * @param shopCategoryCondition 查询条件
	 * @return
	 */
	List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
