package com.seirin.o2o.web.shopadmin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seirin.o2o.dto.ShopExecution;
import com.seirin.o2o.entity.Area;
import com.seirin.o2o.entity.PersonInfo;
import com.seirin.o2o.entity.Shop;
import com.seirin.o2o.entity.ShopCategory;
import com.seirin.o2o.enums.ShopStateEnum;
import com.seirin.o2o.service.AreaService;
import com.seirin.o2o.service.ShopCategoryService;
import com.seirin.o2o.service.ShopService;
import com.seirin.o2o.util.HttpServletRequestUtil;
import com.seirin.o2o.util.ImageUtil;
import com.seirin.o2o.util.PathUtil;

/**
 * @Description: 店铺操作控制器
 * @author seirin
 *
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {

	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ShopCategoryService shopCategoryService;

	@Autowired
	private AreaService areaService;
	
	/**
	 * 店铺信息初始化：店铺区域和店铺类别
	 * 
	 * @return
	 */
	@RequestMapping(value="/getshopinitinfo",method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> getShopInitInfo(){
		System.out.println("111111111");
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<Area> areaList = new ArrayList<Area>();
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		try {
			shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
			areaList = areaService.getAreaList();
			modelMap.put("shopCategoryList", shopCategoryList);
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errorMsg", e.getMessage());
		}
		return modelMap;
	}

	/**
	 * 注册店铺
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("null")
	@RequestMapping(value = "/registershop", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> registerShop(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		// 1、接受并转化相应参数，包括店铺信息及图片信息
		String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
		// 使用jackson-databind-->https://github.com/FasterXML/jackson-databind将json转换为pojo
		ObjectMapper mapper = new ObjectMapper(); // create once, reuse（创建一次，可重用）
		Shop shop = null;
		try {
			shop = mapper.readValue(shopStr, Shop.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.getMessage());
			return modelMap;
		}

		// 获取图片文件流
		CommonsMultipartFile shopImg = null;
		CommonsMultipartResolver commonsmultipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		if (commonsmultipartResolver.isMultipart(request)) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "上传图片不能为空");
			return modelMap;
		}
		// 2、注册店铺，尽量不要依靠前端信息
		if (shop != null && shopImg == null) {
			PersonInfo owner = new PersonInfo();
			owner.setUserId(1L);
			shop.setOwner(owner);
			File shopImgFile = new File(PathUtil.getImgBasePath() + ImageUtil.getRandomFileName());
			try {
				shopImgFile.createNewFile();
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
			try {
				inputStreamToFile(shopImg.getInputStream(), shopImgFile);
			} catch (IOException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
			ShopExecution se = shopService.addShop(shop, shopImgFile);
			if (se.getState() == ShopStateEnum.CHECK.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", se.getStateInfo());
			}
			return modelMap;
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入店铺信息");
			return modelMap;
		}

		// 3、返回结果
	}

	/**
	 * 视频教程中的私有方法
	 */
	public static void inputStreamToFile(InputStream ins, File file) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[1024];
			while ((bytesRead = ins.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			throw new RuntimeException("调用inputStreamToFile产生异常：" + e.getMessage());
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (ins != null) {
					ins.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("inputStreamToFile关闭io产生异常：" + e.getMessage());
			}
		}
	}
}
