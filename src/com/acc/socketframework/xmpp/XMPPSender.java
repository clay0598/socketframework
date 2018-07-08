package com.acc.socketframework.xmpp;

import java.io.OutputStream;
import java.util.List;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.Packet;

import com.acc.socketframework.bean.F16;
import com.acc.socketframework.factory.ConnectionFactory;
import com.acc.socketframework.util.XMPPUtil;

public class XMPPSender {

	static XMPPUtil x = new XMPPUtil();

	public static void sendHeartInfo(F16 position, OutputStream out) {
		List<Packet> list = x.heartIQ(position);	//生成要发送的packet列表
		if(list.size() == 0){
			return;
		}
		
		Connection connection = ConnectionFactory.getConnection(String.valueOf(position.nDeviceID), position);
		//Connection conn = x.logins("1000015", Constant.RESOURCE);
		//Connection conn = x.logins(String.valueOf(position.nDeviceID));
		if(connection != null){
			for(Packet packet: list){
				connection.sendPacket(packet);
			}
		}
	}
	
	public static void sendZjxx(F16 position, OutputStream out) {
		List<Packet> list = x.zjIQ(position);	//生成要发送的packet列表
		if(list.size() == 0){
			return;
		}
		
		Connection connection = ConnectionFactory.getConnection(String.valueOf(position.nDeviceID), position);
		if(connection != null){
			for(Packet packet: list){
				//logger.info("本次发送的自检项:" + packet.toXML());
				connection.sendPacket(packet);
			}
		}
	}
	
}