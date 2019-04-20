package com.acc.socketframework.socket;

import com.acc.socketframework.util.Constant;
import com.acc.socketframework.util.SocketUtil;

import java.io.OutputStream;
import java.net.Socket;
import java.util.TimerTask;

public class SocketClientF16 extends TimerTask{
	SocketUtil protocl = new SocketUtil();
	
	@Override
	public void run() {
		try(Socket socket = new Socket(Constant.socketHost, Constant.socketPort);OutputStream out = socket.getOutputStream();){		//与服务端建立连接
			String[] sendStr = protocl.autoMadeId();									//自动生成ID，发送的字符串
			for(String str: sendStr) {
				str = str.replaceAll("\\[", "").replaceAll("\\]", "");					//去掉两端的[]特殊子符
				byte[] message = protocl.hexStringToBytes(str);							//把字符串转化为字节数据
				out.write(message);
			}
			/*byte[] bf = new byte[128];
			socket.getInputStream().read(bf);
			for(byte b : bf){
				System.out.print(b + ", ");
			}*/
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}