package com.acc.socketframework.iq.provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import com.acc.socketframework.bean.F16;
import com.acc.socketframework.bean.Platform;
import com.acc.socketframework.factory.DataFactory;
import com.acc.socketframework.util.SocketUtil;
import com.acc.socketframework.util.XMPPUtil;

public class SelfIQProvider implements IQProvider {

	//private static final String PREFERRED_ENCODING = "UTF-8";
	private F16 position;
	private XMPPConnection conn;
	SocketUtil protocl = new SocketUtil();
	private static Log logger = (Log) LogFactory.getLog(SelfIQProvider.class);
	//private String iqString = "";

	/*** 通过这个方法我们可以获取从服务器传来iq **/
	public SelfIQProvider(F16 position, XMPPConnection conn) {
		this.position = position;
		this.conn = conn;
	}
	
	@Override
	public IQ parseIQ(XmlPullParser parser) {
		StringBuffer iqString = new StringBuffer("<iq>" + parser.getText());
		while (true) {
			int n = 0;
			try {
				n = parser.next();
			} catch (Exception e) {
				logger.error("监听iq时出现异常:" + e.getMessage());
				conn.disconnect();
				conn = null;
				return null;
				//continue;
			} 
			iqString.append(parser.getText().replaceAll("</stream:stream>null", "").replaceAll("</stream:stream>", ""));
			/*if (n == XmlPullParser.START_TAG) {
				if ("iq".equals(parser.getName())) {
				}
				if ("value".equals(parser.getName())) {
					System.out.println(parser.nextText());// 我们要的东西在这里;张三 李四王五可以根据业务模型自己开发不同的解析工具类返回相应的实体
				}
			} else */
			if (n == XmlPullParser.END_TAG) {
				if("iq".equals(parser.getName())){
					//List<PlatformBean> messageBeanList = XMPPTool.parseIQString(iqString.toString(), position, conn);
					
					Platform platformBean = XMPPUtil.parseIQString(iqString.toString(), position, conn);
					
					iqString.setLength(0);
					
					if(platformBean == null) {
						continue;
					}
					
					//for(PlatformBean messageBean: messageBeanList){
						byte[] pData = new byte[64];
						int returnlength = protocl.buildHostResponse(pData, platformBean);
						//out.write(pData, 0, returnlength);					//回复主机
						//Thread.sleep(1000);
						
						//保存数据到数据队列中
						DataFactory.setPlaform(position.nDeviceID, pData, returnlength);
						pData = null;
					//}
				}
			}
		}
	}

}
