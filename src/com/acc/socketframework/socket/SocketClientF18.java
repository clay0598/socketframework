package com.acc.socketframework.socket;

import java.io.OutputStream;
import java.net.Socket;
import java.util.TimerTask;

import com.acc.socketframework.util.Constant;
import com.acc.socketframework.util.SocketUtil;

public class SocketClientF18 extends TimerTask{
	SocketUtil protocl = new SocketUtil();
	
	@Override
	public void run() {
		try(Socket socket = new Socket(Constant.socketHost, Constant.socketPort);OutputStream out = socket.getOutputStream()) {	// 与服务端建立连接
			byte[] sendStr = protocl.makeDataF18().getBytes();
			out.write(sendStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}