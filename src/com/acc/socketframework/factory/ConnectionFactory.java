package com.acc.socketframework.factory;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import com.acc.socketframework.bean.F16;
import com.acc.socketframework.iq.heart.TimeIQ;
import com.acc.socketframework.iq.provider.SelfIQProvider;
import com.acc.socketframework.util.Constant;

public class ConnectionFactory {

	private static Log logger = (Log) LogFactory.getLog(ConnectionFactory.class);
	public static Map<String,XMPPConnection> map = new HashMap<String,XMPPConnection>();
	private static XMPPConnection connection;
	
	/**
	 * 存放每一个主机建立的XMPPConnection对象
	 * String : 存放主机ID
	 * XMPPConnection : 存放XMPPConnection连接对象
	 */
	public static synchronized XMPPConnection getConnection(String username, F16 position) {
		logger.info("当前长连接数:" + map.size());
		
		connection = map.get(username);			//获得已经保存的XMPPConnection类对象
		
		if (connection == null || connection.isSocketClosed() || !connection.isConnected()) {	//连接对象XMPPConnection是否可用
			try {
				openConnection();
			} catch (XMPPException e) {
				logger.error("创建连接时出现异常:" + e.getMessage());
				map.remove(username);			//移除当前连接
				return null;
			}
		}
		
		if(!connection.isAuthenticated()) {
			try {
				logins(username, position);
				map.put(username, connection);	//保存当前连接
			} catch (XMPPException e) {
				logger.error("设备 " + username + " 登录时出现异常:" + e.getMessage());
				map.remove(username);			//移除当前连接
				return null;
			}
		}
		
		return connection;
	}
	/*public static synchronized XMPPConnection getConnection(String username, MachineBean position) {
		connection = map.get(username);			//获得已经保存的XMPPConnection类对象
		
		if (connection == null || !connection.isAuthenticated() || connection.isSocketClosed() || !connection.isConnected()) {	//连接对象XMPPConnection是否可用
			openConnection();
			
			if(connection != null){
				try {
					connection.login(username, "", Constant.RESOURCE);		//设备登录
					
					ProviderManager.getInstance().addIQProvider(TimeIQ.ELEMENT_NAME, TimeIQ.NAMESPACE, new SelfIQProvider(position, connection));
					
					创建监听
					PacketListener myListener = new PacketListener() {
						public void processPacket(Packet packet) {
						}
					};
					//创建一个packet过滤器来监听来自一个特定用户的新的消息，可以使用一个AndFilter来结合其它两个过滤器。
					//PacketFilter packetFilter = new AndFilter(new PacketTypeFilter(IQ.class), new PacketIDFilter(packet.getPacketID()));
					PacketFilter packetFilter = new AndFilter(new PacketTypeFilter(IQ.class));
					connection.addPacketListener(myListener, packetFilter); // 注册这个监听器
					
					map.put(username, connection);	//保存当前连接
				} catch (XMPPException e) {
					logger.error("设备 " + username + " 登录时出现异常:" + e.getMessage());
					map.remove(username);			//移除当前连接
					connection = null;
				} 
			}
		}
		
		logger.info("当前长连接数:" + map.size());
		return connection;
	}*/
	
	/**
	 * open connection
	 * @throws XMPPException 
	 */
	private static void openConnection() throws XMPPException {
		// Create the configuration for this new connection
		ConnectionConfiguration config = new ConnectionConfiguration(Constant.HOST, Constant.PORT);
		// Sets if the connection is going to use stream compression.
		config.setCompressionEnabled(false);
		// Sets if the new connection about to be establish is going to be debugged.
		config.setDebuggerEnabled(false);
		// Sets if the reconnection mechanism is allowed to be used.
		config.setReconnectionAllowed(true);
		// Sets if the enable security verification
		config.setSASLAuthenticationEnabled(false);
		// Sets the TLS security mode used when making the connection.
		config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
		// Sets if an initial available presence will be sent to the server.
		config.setSendPresence(true);
		connection = new XMPPConnection(config);
		// Connect to the server
		connection.connect();
	}
	
	private static void logins(String username, F16 position) throws XMPPException {
		connection.login(username, "", Constant.RESOURCE);		//设备登录
		
		ProviderManager.getInstance().addIQProvider(TimeIQ.ELEMENT_NAME, TimeIQ.NAMESPACE, new SelfIQProvider(position, connection));
		
		//创建监听
		PacketListener myListener = new PacketListener() {
			public void processPacket(Packet packet) {
			}
		};
		//创建一个packet过滤器来监听来自一个特定用户的新的消息，可以使用一个AndFilter来结合其它两个过滤器。
		//PacketFilter packetFilter = new AndFilter(new PacketTypeFilter(IQ.class), new PacketIDFilter(packet.getPacketID()));
		PacketFilter packetFilter = new AndFilter(new PacketTypeFilter(IQ.class));
		connection.addPacketListener(myListener, packetFilter); // 注册这个监听器
	}
}