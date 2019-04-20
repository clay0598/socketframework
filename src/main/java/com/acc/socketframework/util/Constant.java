package com.acc.socketframework.util;

/**
 * constant
 * 
 * @author wg
 * 
 */
public class Constant {
	public static final String fname = "F18";
	public static final String socketHost = "127.0.0.1";
	public static final int socketPort = 8899;								// socket端口
	//public static final int BUFSIZE = 256; 								// Size (in bytes) of I/O buffer
	public static final int BUFSIZE = 512; 									// Size (in bytes) of I/O buffer
	public static final int SINGLEBUFSIZE = 256; 							// Size (in bytes) of I/O buffer single
	public static final int CONSANT_ID_NUM = 100;									// 模拟生成的客户端数量
	
	public static final String HOST = "10.123.10.10"; 						// XMPP服务器地址
	public static final int PORT = 5222; 									// XMPP端口
	public static final String RESOURCE = "AndroidpnClient"; 				// XMPP资源名

	public static final String regist_fail = "0"; // regist fail,
	public static final String regist_success = "1"; // regist success,
	public static final String ServiceNoResponse = "2"; // service no response
	public static final String usernameIsExist = "3"; // username is exist
}
