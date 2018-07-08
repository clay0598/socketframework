package com.acc.socketframework.bean;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.acc.socketframework.util.DateUtil;

public class F16 {

	public F16() {
		this.gpsTime = DateUtil.getDateToString(new Date(), "yyyyMMddHHmmss");
	}

	public int nMsgID = 0;
	public int nSequence = 0;
	
	public int nDeviceID = 0;
	public int bGPSData = 0;
	public int bLBSData = 0;
	public int iLBSCount = 0;
	public String iLo = null;
	public String iLa = null;
	public String gpsTime = "";

	public int bBandBroken = 0;		//手环破拆
	public int Wristlowpower = 0;	//手环低电
	public int WristException = 0;	//手环设备异常
	public int Hostlowpower = 0;	//主机低电
	public int Wristoffline = 0;	//手环消失
	
	public int nCheckTime = 0;			//自检时间
	public int nHostCPU = 0;			//主机主频,MHz
	public int nHostCPUUsage = 0;		//主机cpu利用率，百分比
	public int nHostRam = 0;			//主机内存大小,kbyte
	public int nHostRAMUsage = 0;		//主机内存使用率，百分比
	public int nHostFlash = 0;			//主机闪存 容量，kbyte
	public int nHostFlashUsage = 0;		//主机闪存 使用量,百分比
	public int nHostPower = 0;			//主机当前电量，百分比
	public int nHostState = 0;			//主机工作状态，0正常，1异常
	public int nPairState = 0;			//主机配对状态,0 未配对，1已配对
	public int nBandState = 0;			//腕带闭合状态, 0破拆，1闭合
	public int nWristState = 0;			//腕带工作状态，0正常，1异常
	public int nSIMNumberLen = 0;		//手机号长度
	public String stbSIM = "";	//手机号,16进制BCD码,高位在前如：0x139542988630,6字节，手机号长为11，号码是13954298863
	public int nBandID = 0;								//配对腕带 id
	public int nBandPower = 0;							//腕带当前电量，百分比
	
	public List<Map<String, Object>> lbsList = null;

	public void setiLo(Double iLo) {
		this.iLo = String.valueOf(iLo);
	}

	public void setiLa(Double iLa) {
		this.iLa = String.valueOf(iLa);
	}

	public void setGpsTime(int gpsTime) {
		this.gpsTime = DateUtil.getTimeByMSeconds((long)gpsTime * 1000);
		//this.gpsTime = String.valueOf(gpsTime);
	}
}