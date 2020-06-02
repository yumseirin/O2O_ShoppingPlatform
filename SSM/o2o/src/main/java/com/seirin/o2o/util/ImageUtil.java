package com.seirin.o2o.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * @Description: 图片处理工具类
 * @author seirin
 *
 */
public class ImageUtil {
	// 获取classpath的绝对值路径
	private static String basePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	// 时间格式化的格式
	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	// 随机数对象
	private static final Random r = new Random();
	//日志
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

	/**
	 * 将文件流CommonsMultipartFile转换成File
	 * 
	 * @param cFile
	 * @return
	 */
	public static File transferCommonsMultipartFileToFile(CommonsMultipartFile cFile) {
		File newFile = new File(cFile.getOriginalFilename());
		try {
			cFile.transferTo(newFile);
		} catch (IllegalStateException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return newFile;
	}

	/**
	 * 根据文件流，处理缩略图，并返回新生成图片的相对路径
	 * 
	 * @param thumbnail  Spring自带的文件处理对象
	 * @param targetAddr 图片存储路径
	 * @return
	 */
	public static String generateThumbnail(CommonsMultipartFile thumbnail, String targetAddr) {
		// 获取随机文件名，防止文件重名
		String realFileName = getRandomFileName();
		// 获取文件扩展名
		String extension = getFileExtension(thumbnail);
		// 在文件夹不存在时创建
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is:" + relativeAddr);
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is:" + PathUtil.getImgBasePath()+relativeAddr);
		try {
			Thumbnails.of(thumbnail.getInputStream()).size(200, 200)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.5f)
					.outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			throw new RuntimeException("创建缩略图失败：" + e.toString());
		}
		return relativeAddr;
	}

	/**
	 * 根据文件，处理缩略图，并返回新生成图片的相对路径
	 * 
	 * @param thumbnail
	 * @param targetAddr
	 * @return
	 */
	public static String generateThumbnail(File thumbnail, String targetAddr) {
		// 获取随机文件名，防止文件重名
		String realFileName = getRandomFileName();
		// 获取文件扩展名
		String extension = getFileExtension(thumbnail);
		// 在文件夹不存在时创建
		makeDirPath(targetAddr);
		String relativeAddr = targetAddr + realFileName + extension;
		logger.debug("current relativeAddr is:" + relativeAddr);
		File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
		logger.debug("current complete addr is:" + PathUtil.getImgBasePath() + relativeAddr);
		try {
			Thumbnails.of(thumbnail).size(200, 200)
					.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.5f)
					.outputQuality(0.8f).toFile(dest);
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		return relativeAddr;
	}

	/**
	 * 生成随机文件名：当前年月日时分秒+五位随机数（为了在实际项目中防止文件同名而进行的处理）
	 */
	public static String getRandomFileName() {
		// 获取随机五位数
		int rannum = (int) (r.nextDouble() * (99999 - 10000 + 1)) + 10000;
		// 当前时间
		String nowTimeStr = sDateFormat.format(new Date());
		return nowTimeStr + rannum;
	}

	/**
	 * 获取输入流的文件扩展名
	 * 
	 * @param thumbnail
	 * @return
	 */
	private static String getFileExtension(CommonsMultipartFile cFile) {
		String originalFileName = cFile.getOriginalFilename();// 返回客户端文件系统中的原始文件名
		return originalFileName.substring(originalFileName.lastIndexOf("."));
	}

	/**
	 * 获取文件的扩展名
	 * 
	 * @param cFile
	 * @return
	 */
	private static String getFileExtension(File cFile) {
		// 输入的图片，只需或获取最后一个 . 后面的字符即可
		String originalFileName = cFile.getName();
		return originalFileName.substring(originalFileName.lastIndexOf("."));
	}

	/**
	 * 创建目标路径上所涉及的目录
	 * 
	 * @param targetAddr
	 */
	private static void makeDirPath(String targetAddr) {
		String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
		File dirPath = new File(realFileParentPath);
		// 目录文件不存在
		if (!dirPath.exists()) {
			dirPath.mkdirs();
		}
	}

	/**
	 * https://github.com/coobird/thumbnailator/wiki/Examples
	 */
	public static void main(String[] args) throws IOException {

		Thumbnails.of(new File("E:\\xiaoying.jpg")).size(200, 200)
				.watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f)
				.outputQuality(0.8f).toFile("E:\\xiaoying_new.jpg");
	}
}
