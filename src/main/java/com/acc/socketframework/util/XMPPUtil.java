package com.acc.socketframework.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;

import com.acc.socketframework.bean.F16;
import com.acc.socketframework.bean.Platform;
import com.acc.socketframework.bean.PlatformNode;
import com.acc.socketframework.bean.PlatformPhone;
import com.acc.socketframework.iq.heart.AlertIQ;
import com.acc.socketframework.iq.heart.LocationIQ;
import com.acc.socketframework.iq.set.SetIQ;
import com.acc.socketframework.iq.zj.CPUIQ;
import com.acc.socketframework.iq.zj.CloseStateIQ;
import com.acc.socketframework.iq.zj.DiskIQ;
import com.acc.socketframework.iq.zj.EQuantityIQ;
import com.acc.socketframework.iq.zj.MemoryIQ;
import com.acc.socketframework.iq.zj.PhoneIQ;
import com.acc.socketframework.iq.zj.ShakeIQ;
import com.acc.socketframework.iq.zj.WorkStateIQ;

/**
 * Xmpp Tool
 * 
 * @author wg
 * 
 */
public class XMPPUtil {
	private static Log logger = (Log) LogFactory.getLog(XMPPUtil.class);

	//心跳IQ
	public List<Packet> heartIQ(F16 position) {
		List<Packet> list = new ArrayList();

		if(position.bBandBroken == 0 && position.Wristlowpower == 0 && position.WristException == 0 && position.Hostlowpower == 0 && position.Wristoffline == 0){
			Packet packet = locationIQ(position);
			list.add(packet);
		}else{
			list.addAll(alertIQ(position));
		}
		
		return list;
	}
	
	//报警IQ
	public static List<Packet> alertIQ(F16 position) {
		List<Packet> list = new ArrayList();
		
		if(position.bBandBroken == 1){
			AlertIQ packet = new AlertIQ();
			packet.setAlertTime(position.gpsTime);
			packet.setX(position.iLo);
			packet.setY(position.iLa);
			packet.setType(IQ.Type.SET);
			packet.setAlertType("001");
			list.add(packet);
		}
		if(position.WristException == 1){
			AlertIQ packet = new AlertIQ();
			packet.setAlertTime(position.gpsTime);
			packet.setX(position.iLo);
			packet.setY(position.iLa);
			packet.setType(IQ.Type.SET);
			packet.setAlertType("006");
			list.add(packet);
		}
		if(position.Wristlowpower == 1){
			AlertIQ packet = new AlertIQ();
			packet.setAlertTime(position.gpsTime);
			packet.setX(position.iLo);
			packet.setY(position.iLa);
			packet.setType(IQ.Type.SET);
			packet.setAlertType("007");
			list.add(packet);
		}
		if(position.Hostlowpower == 1){
			AlertIQ packet = new AlertIQ();
			packet.setAlertTime(position.gpsTime);
			packet.setX(position.iLo);
			packet.setY(position.iLa);
			packet.setType(IQ.Type.SET);
			packet.setAlertType("008");
			list.add(packet);
		}
		if(position.Wristoffline == 1){
			AlertIQ packet = new AlertIQ();
			packet.setAlertTime(position.gpsTime);
			packet.setX(position.iLo);
			packet.setY(position.iLa);
			packet.setType(IQ.Type.SET);
			packet.setAlertType("009");
			list.add(packet);
		}
		
		return list;
	}

	//定位IQ
	public static Packet locationIQ(F16 position) {
		LocationIQ packet = new LocationIQ();
		packet.setTime(position.gpsTime);
		packet.setX(position.iLo);
		packet.setY(position.iLa);
		packet.setType(IQ.Type.SET);
		return packet;
	}
	
	/*解析监听的iq*/
	public static synchronized Platform parseIQString(String iqString, F16 position, Connection conn) {
		//List<PlatformBean> messageBeanList = new ArrayList<PlatformBean>();						//创建返回结果List<MessageBean>
		//String[] iqArray = iqString.split(",");													//将IQ节点串转成数组	
		String setInfo;
		
		//for(int i = 0, len = iqArray.length; i < len; i++){										//遍历IQ节点数组
			//System.out.println(iqArray[i]);
			
			Platform messageBean = new Platform();										//每一个节点是一个MessageBean
			messageBean.setDirection(0x0C);														//消息传送方向
			messageBean.setId(position.nDeviceID);												//主机设备ID
			//messageBean.setSequence(position.nSequence);										//消息序列号
			
			//Element root = DocumentHelper.parseText(iqArray[i]).getRootElement();				//获取根节点
			Element root = null;
			try {
				root = DocumentHelper.parseText(iqString).getRootElement();						//获取根节点
			} catch (DocumentException e) {
				logger.error("获取根节点时出现异常：" + e.getMessage());
				return null;
			}				

			String id = root.attributeValue("id");												//IQ id
			if(id == null){
				return null;
				//continue;
			}
			
			String deviceID = messageBean.getId() + ":";										//设备id
			
        	if (root.element("frequency") != null) {											//采集频率设置
        		List<PlatformNode> nodeBeanList = new ArrayList<PlatformNode>();						//1到5个
        		List<Element> list = root.element("frequency").elements("freq");
        		setInfo = deviceID + "发送频率:";
        		for(Element element: list){
        			PlatformNode nodeBean = new PlatformNode();
        			nodeBean.setPeriod(Integer.valueOf(element.elementText("value")));
        			nodeBean.setStartTime(DateUtil.addTimeZero(element.elementText("startTime")));
        			nodeBean.setEndTime(DateUtil.addTimeZero(element.elementText("endTime")));
        			nodeBeanList.add(nodeBean);
        			
        			setInfo += nodeBean.getPeriod() + "分钟,时间段:" + DateUtil.addTimeZero(nodeBean.getStartTime()) + "-" + DateUtil.addTimeZero(nodeBean.getEndTime()) + "/"; 
        		}
        		logger.info(setInfo.substring(0, setInfo.length() - 1));
        		messageBean.setNodeBeanList(nodeBeanList);
        		messageBean.setNodeCount(nodeBeanList.size());
        		messageBean.setMessageId(0x02);		
        		
        		SetIQ setIQ = new SetIQ("frequency");
        		setIQ.setType(IQ.Type.RESULT);
        		setIQ.setPacketID(id);
				conn.sendPacket(setIQ);
	        } else if (root.element("time") != null) {											//时间设置
        		String time = root.element("time").elementText("value");						
    	        int seconds = (int)(DateUtil.getMSeconds(time)/1000);
    	        messageBean.setTime(seconds);
    	        messageBean.setMessageId(0x03);													
    	        //messageBean.setLength(5);														//消息有效长度
    	        setInfo = deviceID + "设置时间:" + time;// + "，秒数：" + messageBean.getTime();
    	        logger.info(setInfo);
    	        
    	        SetIQ setIQ = new SetIQ("time");
        		setIQ.setType(IQ.Type.RESULT);
        		setIQ.setPacketID(id);
				conn.sendPacket(setIQ);
        	} else if(root.element("telPhones") != null) {										//设置联系电话
        		List<PlatformPhone> phoneBeanList = new ArrayList<PlatformPhone>();						//1到3个
        		List<Element> list = root.element("telPhones").elements("telephone");
        		setInfo = deviceID + "联系电话:";
        		for(Element element: list){
        			PlatformPhone phoneBean = new PlatformPhone();
        			String telephone = element.getText();
        			
        			/*过滤电话中的非数字*/
        			String regEx="[^0-9]";   
        			Pattern p = Pattern.compile(regEx);   
        			Matcher m = p.matcher(telephone);   
        			telephone = m.replaceAll("").trim();

        			phoneBean.setPhoneNumber(telephone);
        			phoneBean.setLength(telephone.length());
        			phoneBeanList.add(phoneBean);
					setInfo += phoneBean.getPhoneNumber() + "/";
        		}
        		logger.info(setInfo.substring(0, setInfo.length() - 1));
        		messageBean.setPhoneCount(list.size());
        		messageBean.setPhoneBeanList(phoneBeanList);
        		messageBean.setMessageId(0x04);	
        		
        		SetIQ setIQ = new SetIQ("telPhones");
        		setIQ.setType(IQ.Type.RESULT);
        		setIQ.setPacketID(id);
				conn.sendPacket(setIQ);
	        } else if (root.element("power") != null) {											//主机关机
        		String power = root.element("power").elementText("value");										
    	        if("off".equals(power)){
    	        	messageBean.setMessageId(0x07);												
        	        setInfo = deviceID + "主机关机:" + power;
		    	    logger.info(setInfo);
		    	    
		    	    SetIQ setIQ = new SetIQ("power");
	        		setIQ.setType(IQ.Type.RESULT);
	        		setIQ.setPacketID(id);
					conn.sendPacket(setIQ);
					
					//conn.disconnect();
					//conn = null;
    	        }
        	} else if (root.element("voltageVal") != null) {									//主机低电报警阈值
        		int voltageVal = Integer.valueOf(root.element("voltageVal").elementText("value"));				
        		messageBean.setPercent(voltageVal);
        		messageBean.setMessageId(0x08);													
    	        setInfo = deviceID + "主机低电报警阈值:" + messageBean.getPercent();
	    	    logger.info(setInfo);
    	        
	    	    SetIQ setIQ = new SetIQ("voltageVal");
        		setIQ.setType(IQ.Type.RESULT);
        		setIQ.setPacketID(id);
				conn.sendPacket(setIQ);
        	} else if (root.element("running") != null) {										//主机停止/开始工作
        		String running = root.element("running").elementText("value");									
    	        if("off".equals(running)){
    	        	messageBean.setMessageId(0x09);												
    	        } else if("on".equals(running)){
    	        	messageBean.setMessageId(0x0a);												
    	        }
    	        setInfo = deviceID + "主机停止/开始工作:" + running;
	    	    logger.info(setInfo);
    	        
	    	    SetIQ setIQ = new SetIQ("running");
        		setIQ.setType(IQ.Type.RESULT);
        		setIQ.setPacketID(id);
				conn.sendPacket(setIQ);
        	} else if (root.element("disk") != null) {											//主机自检
        		String time = root.element("disk").elementText("time");				
        		messageBean.setMessageId(0x0b);													
    	        setInfo = deviceID + "主机自检:" + time;
	    	    logger.info(setInfo);
    	        
	    	    SetIQ setIQ = new SetIQ("disk");
        		setIQ.setType(IQ.Type.RESULT);
        		setIQ.setPacketID(id);
				conn.sendPacket(setIQ);
        	} else if (root.element("silentList") != null) {	
        		//setInfo = deviceID + "静默设置，不作处理";
 	    	    //logger.info(setInfo);
        		
 	    	    SetIQ setIQ = new SetIQ("silentList");
        		setIQ.setType(IQ.Type.RESULT);
        		setIQ.setPacketID(id);
				conn.sendPacket(setIQ);
				return null;
				//continue;
        	} else {
        		return null;
        		//continue;
        	}
			
        	//messageBeanList.add(messageBean);
		//}
		
		setInfo = null;
		return messageBean;
	}
	
	//自检IQ
	public List<Packet> zjIQ(F16 machineBean) {
		List<Packet> list = new ArrayList();
		list.add(DiskIQ(machineBean));
		list.add(EQuantityIQ(machineBean, "phone"));
		list.add(EQuantityIQ(machineBean, "watch"));
		list.add(CPUIQ(machineBean));
		list.add(MemoryIQ(machineBean));
		list.add(PhoneIQ(machineBean));
		list.add(ShakeIQ(machineBean));
		list.add(CloseStateIQ(machineBean));
		list.add(WorkStateIQ(machineBean, "watch"));
		list.add(WorkStateIQ(machineBean, "phone"));
		return list;
	}
	
	//获取主机容量信息
	public static Packet DiskIQ(F16 machineBean) {
		DiskIQ packet = new DiskIQ();
		packet.setTime(String.valueOf(machineBean.nCheckTime));
		packet.setTotal(String.valueOf(machineBean.nHostFlash));
		packet.setPercent(String.valueOf(machineBean.nHostFlashUsage));
		packet.setType(IQ.Type.SET);
		return packet;
	}
	
	//获取主机/腕表电量
	public static Packet EQuantityIQ(F16 machineBean, String setTypePhoneOrWatch) {
		EQuantityIQ packet = new EQuantityIQ();
		packet.setTypePhoneOrWatch(setTypePhoneOrWatch);
		packet.setValue(String.valueOf(machineBean.nHostPower));
		packet.setType(IQ.Type.SET);
		return packet;
	}
	
	//获取主机CPU信息
	public static Packet CPUIQ(F16 machineBean) {
		CPUIQ packet = new CPUIQ();
		packet.setTime(String.valueOf(machineBean.nCheckTime));
		packet.setTotal(String.valueOf(machineBean.nHostCPU));
		packet.setPercent(String.valueOf(machineBean.nHostCPUUsage));
		packet.setType(IQ.Type.SET);
		return packet;
	}
	
	//获取主机内存信息
	public static Packet MemoryIQ(F16 machineBean) {
		MemoryIQ packet = new MemoryIQ();
		packet.setTime(String.valueOf(machineBean.nCheckTime));
		packet.setTotal(String.valueOf(machineBean.nHostRam));
		packet.setPercent(String.valueOf(machineBean.nHostRAMUsage));
		packet.setType(IQ.Type.SET);
		return packet;
	}
	
	//获取主机手机号
	public static Packet PhoneIQ(F16 machineBean) {
		PhoneIQ packet = new PhoneIQ();
		packet.setTime(String.valueOf(machineBean.nCheckTime));
		packet.setNumber(machineBean.stbSIM);
		packet.setType(IQ.Type.SET);
		return packet;
	}
	
	//检测主机震动功能
	public static Packet ShakeIQ(F16 machineBean) {
		ShakeIQ packet = new ShakeIQ();
		packet.setResult("true");
		packet.setType(IQ.Type.SET);
		return packet;
	}
	
	//检测腕表闭合状态
	public static Packet CloseStateIQ(F16 machineBean) {
		CloseStateIQ packet = new CloseStateIQ();
		packet.setResult((machineBean.nBandState == 0) ? "false" : "true");
		packet.setType(IQ.Type.SET);
		return packet;
	}
	
	//检测腕/主机工作情况
	public static Packet WorkStateIQ(F16 machineBean, String setTypePhoneOrWatch) {
		WorkStateIQ packet = new WorkStateIQ();
		packet.setTypePhoneOrWatch(setTypePhoneOrWatch);
		packet.setResult((machineBean.nWristState == 0) ? "true" : "false");
		packet.setType(IQ.Type.SET);
		return packet;
	}
	
}