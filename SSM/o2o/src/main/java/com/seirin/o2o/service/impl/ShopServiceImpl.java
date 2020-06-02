package com.seirin.o2o.service.impl;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seirin.o2o.dao.ShopDao;
import com.seirin.o2o.dto.ShopExecution;
import com.seirin.o2o.entity.Shop;
import com.seirin.o2o.enums.ShopStateEnum;
import com.seirin.o2o.exception.ShopOperationException;
import com.seirin.o2o.service.ShopService;
import com.seirin.o2o.util.ImageUtil;
import com.seirin.o2o.util.PathUtil;

@Service
public class ShopServiceImpl implements ShopService {

	@Autowired
	private ShopDao shopDao;

	@Transactional
	public ShopExecution addShop(Shop shop, File shopImg) {
		// 空值判断
		if (shop == null) {
			return new ShopExecution(ShopStateEnum.NULL_SHOP);
		}
		try {
			// 给店铺信息赋值初始值
			shop.setEnableStatus(0);
			shop.setCreateTime(new Date());
			shop.setLastEditTime(new Date());
			// 添加店铺信息
			int effectedNum = shopDao.insertShop(shop);
			if (effectedNum <= 0) {
				throw new ShopOperationException("店铺创建失败");
			} else {
				if (shopImg != null) {
					// 存储图片
					try {
						addShopImg(shop, shopImg);
					} catch (Exception e) {
						// RuntimeException
						throw new ShopOperationException("addShopImg error:" + e.getMessage());
					}
					// 更新店铺图片地址
					effectedNum = shopDao.updateShop(shop);
					if (effectedNum <= 0) {
						throw new ShopOperationException("更新图片地址失败");
					}
				}
			}
		} catch (Exception e) {
			throw new ShopOperationException("addShop error:" + e.getMessage());
		}
		return new ShopExecution(ShopStateEnum.CHECK, shop);
	}

	private void addShopImg(Shop shop, File shopImg) {
		// 获取Shop图片目录的相对值路径
		String dest = PathUtil.getShopImagePath(shop.getShopId());
		String shopImgAddr = ImageUtil.generateThumbnail(shopImg, dest);
		// 将图片路径存储用于更新店铺信息
		shop.setShopImg(shopImgAddr);

	}

}
