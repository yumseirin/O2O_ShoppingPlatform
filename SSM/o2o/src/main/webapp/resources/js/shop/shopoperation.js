/**
 * 店铺操作：注册和修改
 */
$(function() {
	// 初始化店铺信息：店铺分类和区域信息列表，用于注册店铺
	var initUrl = "/o2o/shopadmin/getshopinitinfo";
	// 注册店铺
	var registerShopUrl = "/o2o/shopadmin/registershop";
	/*
	 * 获取店铺初始化信息：店铺分类和区域信息列表
	 */
	function getShopInitInfo() {
		arlert("2");
		$.getJSON(initUrl, function(data) {
			// 数据存在
			if (data.success) {
				var tempHtml = "";
				var tempAreaHtml = "";
				// 迭代店铺分类列表
				data.shopCategoryList.map(function(item, index) {
					tempHtml += '<option data-id="' + item.shopCategoryId
							+ '">' + item.shopCategoryName + '</option>';
				});
				// 迭代区域信息
				data.areaList.map(function(item, index) {
					tempAreaHtml += '<option data-id="' + item.areaId + '">'
							+ item.areaName + '</option>';
				});
				$('#shop-category').html(tempHtml);
				$('#area').html(tempAreaHtml);
			}
		});
		$('#submit').click(function(){
			var shop = {};
			// 如果是编辑，需要传入shopId
			if (isEdit) {
				shop.shopId = shopId;
			}
			shop.shopName = $("#shop-name").val();
			shop.shopAddr = $("#shop-addr").val();
			shop.phone = $("#shop-phone").val();
			shop.shopDesc = $("#shop-desc").val();
			// 选择id,双重否定=肯定
			shop.shopCategory = {
				// 这里定义的变量要和ShopCategory.shopCategoryId保持一致，否则使用databind转换会抛出异常
				shopCategoryId : $('#shop-category').find('option').not(function() {
					return !this.selected;
				}).data('id')
			};
			shop.area = {
				// 这里定义的变量要和Area.areaId属性名称保持一致，否则使用databind转换会抛出异常
				areaId : $('#area').find('option').not(function() {
					return !this.selected;
				}).data('id')
			};
			// 获取图片文件流
			var shopImg = $('#shop-img')[0].files[0];
			var formData = new FormData();
			formData.append('shopImg', shopImg);
			formData.append('shopStr', JSON.stringify(shop));
//			var verifyCodeActual = $('#j_kaptcha').val();
//			if (!verifyCodeActual) {
//				$.toast("请输入验证码！");
//				return;
//			} else {
//				formData.append('verifyCodeActual', verifyCodeActual);
//			}
			$.ajax({
				url:isEdit ? modifyShopUrl:registerShopUrl,
				type : 'POST',
				data : formData,
				contentType : false,
				processData : false,
				cache : false,
				success : function(data) {
					if (data.success) {
						$.toast("提交成功！");
					} else {
						$.toast("提交失败！" + data.errMsg);
					}
					// 更换验证码
//					$('#kaptcha_img').click();
				}
			});
			
		});
		
	 }
	
})