package com.acc.socketframework.bean;

/**
 * 数据节点信息保存
 * 
 * @author Administrator
 */
public class PlatformNode {

	/**
	 * 开始时间
	 */
	private String startTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
	/**
	 * 心跳周期，分钟
	 */
	private int period;

	public String getStartTime() {
		return startTime;
	}
    
	/**
	 * 开始时间设定
	 * 
	 * @param startTime 开始时间
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	/**
	 * 结束时间设定
	 * 
	 * @param endTime 结束时间
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getPeriod() {
		return period;
	}

	/**
	 * 心跳次数设定
	 * 
	 * @param period 心跳次数
	 */
	public void setPeriod(int period) {
		this.period = period;
	}
}
