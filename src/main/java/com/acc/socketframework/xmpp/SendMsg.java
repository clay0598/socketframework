package com.acc.socketframework.xmpp;

import com.acc.socketframework.bean.Platform;
import com.acc.socketframework.factory.ConnectionFactory;
import com.acc.socketframework.factory.DataFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.XMPPConnection;

import java.util.*;
import java.util.Map.Entry;

/**
 * 向主机发送数据保存
 * 
 * @author luson
 */
public class SendMsg extends TimerTask{

	private static Log logger = (Log) LogFactory.getLog(SendMsg.class);
	
	/**
	 * 存放待发送主机的数据列表
	 */
	public static List<Platform> list = new ArrayList<Platform>();

	@Override
	public void run() {
		Platform bean;
		while((bean = DataFactory.queue.poll()) != null) {
			list.add(bean);
        }
	}
	
	/**
	 * 返回待发送数据队列
	 * 
	 * @return 电文
	 */
	public synchronized static Queue<Platform> getPlatformData(int id) {
		Queue<Platform> queue = new LinkedList<Platform>();
		StringBuffer message = new StringBuffer("待发送数据:" + list.size() + " ");
		for(int i = 0; i < list.size(); i++){								//将当前设备待发送数据加入队列，并删除相应list值
			Platform bean = (Platform)list.get(i);
			if(bean != null) {   
				message.append(bean.getId() + "/");
				if(id == bean.getId()) {
					queue.offer(bean);										//插入队列
					list.remove(i);											//从数据列表中移除数据
					i--;													//list的大小减少,下标后移1位
				}
			}
		}
		logger.info(message.substring(0, message.length() - 1));
		message.setLength(0);
		
		if(queue.size() > 30){
			logger.warn("设备 " + id + " 待发送数据超过30，清空并断开。");
			
			queue.clear();													//清空发送队列
			
			/*断掉并移除当前连接*/
			XMPPConnection connection = ConnectionFactory.map.get(id);
			connection.disconnect();
			connection = null;
			ConnectionFactory.map.remove(id);
		}
		
		if(list.size() > 100){
			logger.warn("待发送数据超过100，全部清空并断开。");
			
			list.clear();													//删除所有待发送数据
			
			/*断掉并移除所有连接*/
			for(Entry<String, XMPPConnection> entry: ConnectionFactory.map.entrySet()){
				XMPPConnection connection = entry.getValue();
				connection.disconnect();
				connection = null;
				ConnectionFactory.map.remove(entry.getKey());
			}
		}
		
		return queue;
	}
}
