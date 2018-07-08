package com.acc.socketframework;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.acc.socketframework.socket.SocketClientF18;
import com.acc.socketframework.socket.SocketServerF18;
import com.acc.socketframework.util.Constant;
import com.acc.socketframework.xmpp.SendMsg;

public class Startup {
	private static Log logger = (Log) LogFactory.getLog(Startup.class);
	
	public static void main(String[] args) {
		Timer timer = new Timer();
    	timer.schedule(new SendMsg(), 1000, 1000);						//启动轮询任务队列
		timer.scheduleAtFixedRate(new SocketClientF18(), 0, 1);			//启动客户端
		try(final ServerSocket serverSocket = new ServerSocket(Constant.socketPort)) {	
			Socket socket = null;
			ExecutorService executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), 50, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10000));
			while (true) {
				socket = serverSocket.accept();								//server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
				//new Thread(new SocketServerF18(socket)).start(); 			//同步阻塞式IO
				executor.execute(new SocketServerF18(socket));				//伪异步IO
				System.gc();
			}
		} catch (IOException e) {
			logger.error("ServerSocket连接异常：" + e.getStackTrace());
		}
	}
}