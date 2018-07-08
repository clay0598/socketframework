package com.acc.socketframework.bean;

/**
 * 电话号码信息保存
 * 
 * @author Administrator
 */
public class PlatformPhone {

	/**
	 * 保留
	 */
	private int reserve;
	
	/**
	 * 电话号码长度
	 */
	private int length;
	
	/**
	 * 电话号码
	 */
	private String phoneNumber;

	/**
	 * 保留
	 * 
	 * @return 保留
	 */
	public int getReserve() {
		return reserve;
	}

	public void setReserve(int reserve) {
		this.reserve = reserve;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
