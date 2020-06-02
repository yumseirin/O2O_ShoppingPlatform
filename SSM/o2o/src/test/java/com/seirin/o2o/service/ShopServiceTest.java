package com.seirin.o2o.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.seirin.o2o.BaseTest;
import com.seirin.o2o.dto.ShopExecution;
import com.seirin.o2o.entity.Area;
import com.seirin.o2o.entity.PersonInfo;
import com.seirin.o2o.entity.Shop;
import com.seirin.o2o.entity.ShopCategory;
import com.seirin.o2o.enums.ShopStateEnum;

public class ShopServiceTest extends BaseTest {

	@Autowired
	private ShopService shopService;

	@Test
	public void testAddShop() throws IOException {
		Shop shop = new Shop();
		PersonInfo owner = new PersonInfo();
		Area area = new Area();
		ShopCategory shopCategory = new ShopCategory();
		owner.setUserId(1L);
		area.setAreaId(2);
		shopCategory.setShopCategoryId(1L);
		shop.setOwner(owner);
		shop.setArea(area);
		shop.setShopCategory(shopCategory);
		shop.setShopName("测试的店铺2");
		shop.setShopDesc("test1");
		shop.setShopAddr("test");
		shop.setPhone("test1");
		shop.setCreateTime(new Date());
		shop.setEnableStatus(ShopStateEnum.CHECK.getState());
		shop.setAdvice("审核中");
		File shopImg = new File("E:\\xiaoying.jpg");
		ShopExecution se = shopService.addShop(shop, shopImg);
		assertEquals(ShopStateEnum.CHECK.getState(), se.getState());
	}

	/**
	 * filePath to MultipartFile
	 * 
	 * @param filePath
	 * @throws IOException
	 */
//	private MultipartFile path2MultipartFile(String filePath) throws IOException {
//		File file = new File(filePath);
//		FileInputStream input = new FileInputStream(file);
//		MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", input);
//		return multipartFile;
//	}
}
