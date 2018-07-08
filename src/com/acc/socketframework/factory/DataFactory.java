package com.acc.socketframework.factory;

import java.util.LinkedList;
import java.util.Queue;

import com.acc.socketframework.bean.Platform;

/**
 * 存放数据的工厂
 * 
 * @author luson
 */
public class DataFactory {
	/**
	 * 平台返回数据存放队列对象
	 */
	public static Queue<Platform> queue = new LinkedList<Platform>();
	
	/**
	 * 保存平台返回数据
	 * 
	 * @param id 主机ID
	 * @param pData 主机电文
	 */
	public synchronized static void setPlaform(int id, byte[] pData, int plength) {
		Platform bean = new Platform();
		bean.setId(id);
		bean.setPData(pData);
		bean.setPlength(plength);
		//数据存放在内存数据队列中
		queue.offer(bean);
	}
}
