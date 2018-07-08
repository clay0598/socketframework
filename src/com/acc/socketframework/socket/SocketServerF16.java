package com.acc.socketframework.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.acc.socketframework.bean.F16;
import com.acc.socketframework.bean.Platform;
import com.acc.socketframework.util.Constant;
import com.acc.socketframework.util.SocketUtil;
import com.acc.socketframework.xmpp.SendMsg;
import com.acc.socketframework.xmpp.XMPPSender;

/**
 * 用来处理Socket请求
 */
public class SocketServerF16 implements Runnable {
	private static Log logger = (Log) LogFactory.getLog(SocketServerF16.class);
	SocketUtil protocl = new SocketUtil();
	private InputStream in;
	private OutputStream out;

	public SocketServerF16(Socket socket) throws IOException {
		in = socket.getInputStream();
		out = socket.getOutputStream();
	}

	public void run() {
		byte[] echoBuffer = new byte[Constant.BUFSIZE]; 					// Receive Buffer
	    try {
			while ((in.read(echoBuffer)) != -1) {							// Receive until client closes connection, indicated by -1
				handleData(echoBuffer);
			}
		} catch (IOException e) {
			logger.error("接收设备数据时出现异常:" + e.getMessage());
		} finally {
			echoBuffer = null;
		}
	}
	
	/**
	 * 解析报文并处理业务逻辑
	 * @return 
	 * 
	 */
	private void handleData(byte[] echoBuffer) {
		List<byte[]> list = protocl.getReceiveMsg(echoBuffer);
		for(int k = 0, len = list.size(); k < len; k++){
			byte[] buf = list.get(k);
			F16 position = protocl.ParseDataEx(buf, Constant.SINGLEBUFSIZE);						//解析成消息实体
			buf = null;
			if(position.nMsgID == 0x01){															//心跳消息
				XMPPSender.sendHeartInfo(position, out);
			}else if(position.nMsgID == 0x0b){														//自检信息
				XMPPSender.sendZjxx(position, out);
				
				/*回复主机的应答消息*/
				byte[] pData = new byte[64];
				Platform messageBean = new Platform();										//每一个节点是一个MessageBean
				messageBean.setDirection(0x0C);														//消息传送方向
				messageBean.setId(position.nDeviceID);												//主机设备ID
				messageBean.setSequence(position.nSequence);										//消息序列号
				messageBean.setMessageId(0x00);
				messageBean.setAckMessageId(position.nMsgID);
				messageBean.setAckSequence(position.nSequence);
				int returnlength = protocl.buildHostResponse(pData, messageBean);
				/*for (int i = 0; i < returnlength; i++) {
					WriteLog.writeSingleLine(Integer.toHexString(pData[i] & 0xff) + ", ");
				}*/
				try {
					out.write(pData, 0, returnlength);
					logger.info("回复设备的应答消息成功");
				} catch (IOException e) {
					logger.error("回复设备的应答消息时出现异常:" + e.getMessage());
				}
				pData = null;
			}
			
			if(k == (len -1)) {	//最后一条数据时，执行发送电文操作
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					logger.error("等待向设备发送数据时线程异常:" + e1.getMessage());
				}

				Queue<Platform> queue = SendMsg.getPlatformData(position.nDeviceID);
				Platform platformBean;
				while((platformBean = queue.poll()) != null) {
					try {
						platformBean.setSequence(position.nSequence);										//消息序列号
						
						out.write(platformBean.getPData(), 0, platformBean.getPlength());
						logger.info("向设备 " + position.nDeviceID + " 发送数据成功");
						Thread.sleep(1000);
					} catch (Exception e) {
						logger.error("向设备 " + position.nDeviceID + " 发送数据时出现异常:" + e.getMessage());
						//break;
					}					
		        }
				queue = null;
				platformBean = null;
			}
		}		
	}
	
}