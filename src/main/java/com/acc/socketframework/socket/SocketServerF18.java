package com.acc.socketframework.socket;

import com.acc.socketframework.bean.F18;
import com.acc.socketframework.util.Constant;
import com.acc.socketframework.util.DBUtil;
import com.acc.socketframework.util.SocketUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.net.Socket;

/**
 * 用来处理Socket请求
 */
public class SocketServerF18 implements Runnable {
	private static Log logger = (Log) LogFactory.getLog(SocketServerF18.class);
	SocketUtil protocl = new SocketUtil();
	private InputStream in;
	private OutputStream out;
	BufferedReader br;

	public SocketServerF18(Socket socket) throws IOException {
		in = socket.getInputStream();
		out = socket.getOutputStream();
		br = new BufferedReader(new InputStreamReader(in));
	}

	public void run() {
		int recvMsgSize;
		char[] echoBuffer = new char[Constant.BUFSIZE];
	    try {
	    	while ((recvMsgSize = br.read(echoBuffer)) != -1) {
				handleData(echoBuffer, recvMsgSize);
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
	private void handleData(char[] echoBuffer, int recvMsgSize) {
		String[] receiveArray = protocl.parsePositions(echoBuffer, recvMsgSize);
		for(int i = 0, len = receiveArray.length; i < len; i++){
			F18 position = protocl.parsePosition(receiveArray[i]);						//解析成消息实体
			if(position != null){														//格式正确
				DBUtil.positionInsert(position);
			}else{
				logger.error("数据包格式不正确。");
			}
		}		
	}
	
}