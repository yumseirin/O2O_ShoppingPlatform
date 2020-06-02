package com.seirin.o2o.exception;

public class ShopOperationException extends RuntimeException {

	/**
	 * 随机序列号ID
	 */
	private static final long serialVersionUID = 5180947696973096561L;

	public ShopOperationException(String msg) {
		super(msg);
	}
}
