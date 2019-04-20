package com.acc.socketframework.util;

import com.acc.socketframework.bean.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class SocketUtil {
	
	private static Log logger = (Log) LogFactory.getLog(SocketUtil.class);
	
	public String[] parsePositions(char[] pData, int recvMsgSize) {
		StringBuffer receivebytes = new StringBuffer();
		for (int i = 0; i < recvMsgSize; i++) {
    		receivebytes.append(pData[i]);
    	}
		String positionString = receivebytes.toString();
		logger.info("收到数据包：" + positionString);
    	String[] receiveArray = positionString.split("!");
		return receiveArray;
	}
	
	public F18 parsePosition(String positionString) {
		F18 position = new F18();
		String[] positionArray = positionString.split(",");
		
		String version = positionArray[0];
		String[] versionArray = version.split("\\$");
		if(versionArray.length < 2){
			return null;
		}
		
		position.setVersion(versionArray[1]);
		position.setImei(positionArray[1]);
		position.setDevicename(positionArray[2]);
		position.setGprsFlag(positionArray[3]);
		
		String date = positionArray[4];
        String time = positionArray[5];
        Date devicetime = DateUtil.add8Hours(20 + date.substring(4) + date.substring(2, 4) + date.substring(0, 2) + time);
        position.setDevicetime(devicetime);
		
		position.setGpsStatus(positionArray[6]);
		position.setLatNs(positionArray[8]);	
		position.setLngWe(positionArray[10]);
		
		position.setSatelliteBeidou(positionArray[11]);
		position.setSatelliteGps(positionArray[12]);
		position.setSatelliteGlonass(positionArray[13]);
		position.setAccuracy(positionArray[14]);
		position.setSpeed(positionArray[15]);
		position.setHeading(positionArray[16]);
		position.setAltitude(positionArray[17]);
		position.setMileage(positionArray[18]);
		position.setMcc(positionArray[19]);
		
		position.setMnc(positionArray[20]);
		position.setLac(String.valueOf(Integer.parseInt(positionArray[21], 16)));
		position.setCellId(String.valueOf(Integer.parseInt(positionArray[22], 16)));
		
		position.setGsmSignal(positionArray[23]);
		position.setDigitalInput(positionArray[24]);
		position.setDigitalOutput(positionArray[25]);
		position.setAnalogInput1(positionArray[26]);
		position.setAnalogInput2(positionArray[27]);
		position.setAnalogInput3(positionArray[28]);
		position.setSensor1(positionArray[29]);
		position.setSensor2(positionArray[30]);
		position.setRfid(positionArray[31]);
		position.setExternal(positionArray[32]);
		position.setElectricity(positionArray[33]);
		position.setAlarm(positionArray[34].replace(";", ""));
		
		String lat = positionArray[7];
		String lng = positionArray[9];
		if(lat.equals("0000.00000") && lng.equals("00000.00000")){				//未获取到经纬度
			position = DBUtil.lbsSelect(position);							//获取离线lbs数据
			if(position.getLat() == null && position.getLng() == null){			//未获取到离线lbs数据
				position = LbsAPI.apiGet(position);								//调用基站云接口
				if(position.getLat() == null && position.getLng() == null){		//未获取到基站云接口数据
					return null;
				}
			}
		}else{																	//获取到经纬度，度分格式——>度
			MathContext mc = new MathContext(3, RoundingMode.HALF_DOWN);
			
			BigDecimal lat_a = new BigDecimal(lat.substring(0, 2));
			BigDecimal lat_b = new BigDecimal(lat.substring(2, lat.length()));
			BigDecimal lat_final = lat_a.add(lat_b.divide(new BigDecimal(60), mc));
			position.setLat(lat_final);										
									
			BigDecimal lng_a = new BigDecimal(lng.substring(0, 3));
			BigDecimal lng_b = new BigDecimal(lng.substring(3, lng.length()));
			BigDecimal lng_final = lng_a.add(lng_b.divide(new BigDecimal(60), mc));
			position.setLng(lng_final);										
		}
		
		return position;
	}

	public int GetInt1byte(byte ndata1) {
		int rel_1 = ndata1;
		rel_1 &= 0xff;
		int u32Data = rel_1;

		return u32Data;
	}

	public int GetInt2byte(byte ndata1, byte ndata2) {
		int rel_1 = ndata1;
		rel_1 &= 0xff;
		int rel_2 = ndata2;
		rel_2 &= 0xff;

		int u32Data = (rel_1 << 8) + rel_2;

		return u32Data;
	}

	public int GetInt4byte(byte ndata1, byte ndata2, byte ndata3, byte ndata4) {
		int rel_1 = ndata1;
		rel_1 &= 0xff;
		int rel_2 = ndata2;
		rel_2 &= 0xff;
		int rel_3 = ndata3;
		rel_3 &= 0xff;
		int rel_4 = ndata4;
		rel_4 &= 0xff;
		int u32Data = (rel_1 << 24) + (rel_2 << 16) + (rel_3 << 8) + rel_4;

		return u32Data;
	}

	public double GetGPS4byte(byte ndata1, byte ndata2, byte ndata3, byte ndata4){
		int rel_1 = ndata1;
		rel_1 &= 0x7f;
		int rel_2 = ndata2;
		rel_2 &= 0xff;
		int rel_3 = ndata3;
		rel_3 &=  0xff;
		int rel_4 = ndata4;
		rel_4 &= 0xff;
		int u32Data = (rel_1 << 24) +( rel_2 << 16) + (rel_3 << 8) + rel_4;
		double d32Data = (double)(u32Data % 100000) / 1000.0 / 60.0 + (double)(u32Data / 100000);
		if((ndata1 & 0x80) != 0){
			d32Data = 0 - d32Data;
		}
		return d32Data;
	}
	
	/**
	 * 把十六进制字符串转化为byte字节数组
	 * 
	 * @param hexString 十六进制字符串
	 * @return byte字节数组
	 */
	public byte[] hexStringToBytes(String hexString){
		//十六进制字符串判断
		if(hexString == null || hexString.length() == 0)
		{
			return null;
		}
		
		//把十六进制字符转化为大写字母
		hexString = hexString.toUpperCase();
		
		//因为中间有空格，故/3 最后一个不足3个 也加 1;
		int length = hexString.length() / 3 + 1;
		
		//分解十六进制字母
		char[] hexChars = hexString.toCharArray();
		
		//返回对象声明
		byte[] d = new byte[1024];
		int index = 0;
		
		//循环字符数组，取每一个字母，把其转化为byte字符
		for(int i=0; i<length; i++)
		{
			//字符位置跳转(每次跳转是3的整数倍)
			int pos = i*3;
			
			//高4为左移4为在或低四位,组成一个8为的byte字节数据
			d[index ++] = (byte)(charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos+1]));
		}
		
		return d;
	}
	
	//取字符
	private byte charToByte(char c){
		return (byte)"0123456789ABCDEF".indexOf(c);
	} 
	
	public List<byte[]> getReceiveMsg(byte[] pData) {
		List<byte[]> resultList = new ArrayList<byte[]>();	//返回结果对象
		
		//正确电文的开始标识
		byte[] header = new byte[2];
		header[0] = (byte)(0xC5);
		header[1] = (byte)(0x13);		
		
		int nOffset = 0;
		int nSubOffset = 0;
		
		for(int i = 0; i < pData.length; i++) {
			nOffset++;
			
			if(pData[i] == header[0]) {				//判断电文开头 0xC5
				//byte[] subByte = new byte[128];		//字串
				byte[] subByte = new byte[Constant.SINGLEBUFSIZE];		//字串
				
				subByte[nSubOffset] = pData[i];
				nSubOffset += 1;
				
				if(pData[i+1] == header[1]) {
					subByte[nSubOffset] = pData[i+1];
					nSubOffset += 1;
					
					while((nOffset + nSubOffset) <= pData.length) {
						if(nSubOffset < 6) {		//主机ID					
							subByte[nSubOffset] = pData[i+nSubOffset];
							nSubOffset += 1;
						} else if(nSubOffset < 8) {	//sequence
							subByte[nSubOffset] = pData[i+nSubOffset];
							nSubOffset += 1;
						} else if(nSubOffset == 8) {	//消息长度
							//消息长度取得
							int nMsgLen = GetInt1byte(pData[i+nSubOffset]);
							
							subByte[nSubOffset] = pData[i+nSubOffset];
							
							boolean msgFlag = false;
							if((nOffset + nSubOffset + nMsgLen) <= pData.length) {
								int nMesOffset = i+nSubOffset;
								for(int j = 1; j <= nMsgLen; j++) {
									if(j == nMsgLen) {
										msgFlag = true;
										nSubOffset += 1;
										subByte[nSubOffset] = pData[nMesOffset+j];
									} else {
										nSubOffset += 1;
										subByte[nSubOffset] = pData[nMesOffset+j];
									}
								}
							} else {
								subByte = null;
								i = nOffset + nSubOffset - 1;
								nOffset = nOffset + nSubOffset;
								nSubOffset = 0;
								break;
							}
							
							if(msgFlag) {
								//判断长度,取最后两位的 检验和
								if((nOffset + nSubOffset + 2) <= pData.length) {
									//得到校验和
									int checksumRec = GetInt2byte(pData[nOffset + nSubOffset], pData[nOffset + nSubOffset + 1]);
									
									int nCheckSum = 0;
									for(int iCheck = 0; iCheck < subByte.length; iCheck++) {
										int checksumTmp = GetInt1byte(subByte[iCheck]);
										nCheckSum += checksumTmp;
									}
									nCheckSum &= 0x0000ffff;
									
									//校验和与电文中数字相加和 相等，说明是一个正确电文
									if (nCheckSum == checksumRec) {
										nSubOffset += 1;
										subByte[nSubOffset] = pData[nOffset + nSubOffset -1];
										
										nSubOffset += 1;
										subByte[nSubOffset] = pData[nOffset + nSubOffset -1];
										
										//添加此子电文
										resultList.add(subByte);
										
										i = nOffset + nSubOffset - 1;
										nOffset = nOffset + nSubOffset;
										nSubOffset = 0;
										break;
									} else {
										subByte = null;
										i = nOffset + nSubOffset - 1;
										nOffset = nOffset + nSubOffset;
										nSubOffset = 0;
										break;
									}
								} else {
									subByte = null;
									i = nOffset + nSubOffset - 1;
									nOffset = nOffset + nSubOffset;
									nSubOffset = 0;
									break;
								}
							} else {
								subByte = null;
								i = nOffset + nSubOffset - 1;
								nOffset = nOffset + nSubOffset;
								nSubOffset = 0;
								break;
							}
						}
					}
				} else {
					subByte = null;
					i = nOffset + nSubOffset - 1;
				}
			}			
		}
		header = null;
		return resultList;
	}
	
	public F16 ParseDataEx(byte[] pData, int nLength) {
		F16 nPos = new F16();
		nPos.bGPSData = 0;
		
		// 每个数据包至少帧头是9个字节
		if (nLength <= 9){
			return null;
		}
		
		logger.info("----------------------------------");
		
		// 帧头特征包
		int nFrameHead = pData[0];
		nFrameHead &= 0xff;
		//logger.info("帧头特征包:" + nFrameHead);
		if (nFrameHead != 0xC5){
			logger.error("帧头特征包有误:" + nFrameHead);
			return null;
		}

		int noffset = 1;
		int nVersion = pData[noffset];
		nVersion &= 0xf0; // 高4位是协议版本号
		nVersion = nVersion >> 4;
		//logger.info("协议版本号:" + nVersion);

		int nDircetion = pData[noffset];
		nDircetion &= 0x0f; // 低4位 数据包发送方向
		//logger.info("低4位 数据包发送方向:" + nDircetion);
		noffset += 1;
		
		nPos.nDeviceID = GetInt4byte(pData[noffset], pData[noffset + 1], pData[noffset + 2], pData[noffset + 3]);
		logger.info("设备号:" + nPos.nDeviceID);
		noffset += 4;

		// 数据包流水号
		int nSequence = GetInt2byte(pData[noffset], pData[noffset + 1]);
		//logger.info("数据包流水号:" + nSequence);
		nPos.nSequence = nSequence;
		noffset += 2;

		// 数据包中负载消息的长度
		int nMsgLen = GetInt1byte(pData[noffset]);
		//logger.info("数据包中负载消息的长度:" + nMsgLen);
		if(nMsgLen > 118 ){
			logger.error("数据包中负载消息的长度过长:" + nMsgLen);
			return null;
		}
		noffset += 1;

		int checksumRec = GetInt2byte(pData[9+nMsgLen], pData[10+nMsgLen]);
		//logger.info("check rec:" + checksumRec);
		//校验计算
		int nCheckSum = 0;
		for(int iCheck = 0; iCheck < 9+nMsgLen; iCheck++){
			int checksumTmp = GetInt1byte(pData[iCheck]);
			nCheckSum += checksumTmp;
		}
		nCheckSum &= 0x0000ffff;
		//logger.info("check sum:" + nCheckSum);
		if (nCheckSum != checksumRec){
			logger.error("包长度错误");
			return null;
		}

		// 消息码值
		int nMsgID = GetInt1byte(pData[noffset]);
		//logger.info("消息码值:" + nMsgID);
		nPos.nMsgID = nMsgID;
		noffset += 1;

		// 心跳包
		if (nMsgID == 0x01) {
			logger.info("消息类型:心跳");
			
			// 手环警报消息
			nPos.bBandBroken = ((pData[noffset] >> 7) & 0x01);// 手环破拆
			nPos.Wristlowpower = ((pData[noffset] >> 6) & 0x01); // 手环低电
			nPos.WristException = ((pData[noffset] >> 5) & 0x01);// 手环设备异常
			noffset += 1;

			// 主机警报
			nPos.Hostlowpower = ((pData[noffset] >> 7) & 0x01); // 主机低电
			nPos.Wristoffline = ((pData[noffset] >> 6) & 0x01);	// 手环消失

			StringBuffer alertMessage = new StringBuffer();
			if(nPos.bBandBroken == 1) {
				alertMessage.append("手环破拆,");
			}
			if(nPos.Wristlowpower == 1) {
				alertMessage.append("手环低电,");
			}
			if(nPos.WristException == 1) {
				alertMessage.append("手环设备异常,");
			}
			if(nPos.Hostlowpower == 1) {
				alertMessage.append("主机低电,");
			}
			if(nPos.Wristoffline == 1) {
				alertMessage.append("手环消失,");
			}
			if(alertMessage.length() > 0) {
				alertMessage.insert(0, "报警信息:");
				logger.info(alertMessage.substring(0, alertMessage.length() - 1));
			}
			//logger.info("报警信息:" + nPos.bBandBroken + "(手环破拆)," + nPos.Wristlowpower + "(手环低电)," + nPos.WristException + "(手环设备异常)," + nPos.Hostlowpower + "(主机低电)," + nPos.Wristoffline + "(手环消失)");
			
			nPos.bGPSData = ((pData[noffset] >> 1) & 0x01);		// 数据包是否带有GPS定位数据
			nPos.bLBSData = pData[noffset] & 0x01;				// 数据包是否带有基站信息
			noffset += 1;
			
			// 当有手环消失报警时，提取手环消失时长
			if (nPos.Wristoffline == 1) {
				// 提供时长
				int nWristOffTime = GetInt2byte(pData[noffset], pData[noffset + 1]);
				logger.info("当有手环消失报警时，提取手环消失时长:" + nWristOffTime);
				noffset += 2;
			}

			// 带有GPS定位信息时，提取经纬度信息
			if (nPos.bGPSData == 1) {
				nPos.setiLo(GetGPS4byte(pData[noffset], pData[noffset + 1], pData[noffset + 2], pData[noffset + 3]));
				noffset += 4;
				nPos.setiLa(GetGPS4byte(pData[noffset], pData[noffset + 1], pData[noffset + 2], pData[noffset + 3]));
				noffset += 4;
				int gpsTime = GetInt4byte(pData[noffset], pData[noffset + 1], pData[noffset + 2], pData[noffset + 3]);
				nPos.setGpsTime(gpsTime);
				noffset += 4;
				logger.info("GPS定位信息:" + nPos.iLo + "," + nPos.iLa + "," + nPos.gpsTime);
			} 

			// 带有基站信息时，提供基站信息，最多7个
			if (nPos.bLBSData == 1) {
				int nLBSCount = GetInt1byte(pData[noffset]);
				logger.info("基站数:" + nLBSCount);
				if(nLBSCount <= 7){
					noffset += 1;
					noffset += 4;
					List<Map<String, Object>> lbsList = new ArrayList();
					StringBuffer message = new StringBuffer();
					for (int nIndex = 0; nIndex < nLBSCount; nIndex++) {
						Map<String, Object> map = new HashMap();
						// 基站号
						int nCellID = GetInt2byte(pData[noffset], pData[noffset + 1]);
						noffset += 2;
						map.put("nCellID", nCellID);
						message.append("基站号:" + nCellID + ",");
						// 小区号
						int nZoneID = GetInt2byte(pData[noffset], pData[noffset + 1]);
						noffset += 2;
						map.put("nZoneID", nZoneID);
						message.append("小区号:" + nZoneID + ",");
						// 信号强度
						int nQuality = GetInt2byte(pData[noffset], pData[noffset + 1]);
						noffset += 2;
						map.put("nQuality", nQuality);
						message.append("信号强度:" + nQuality);
						lbsList.add(map);
						logger.info(message);
						message.setLength(0);
					}
					nPos.lbsList = lbsList;
				}
			}
		}else if(nMsgID == 0x00){	 //主机向后台发送的一般性确认消息
			logger.info("消息类型:应答");
            int nAckID = GetInt1byte(pData[noffset]); noffset += 1;
            int nAckSeq = GetInt2byte(pData[noffset], pData[noffset+1]); noffset += 1;
        	//return null;
        }else if(nMsgID == 0x06){	//配对成功消息
        	logger.info("消息类型:配对成功");
	        int iCmdID = 0x06;
	        int nPairedBandID = GetInt4byte(pData[noffset], pData[noffset + 1], pData[noffset + 2], pData[noffset + 3]); 
	        noffset += 4;
	    }else if(nMsgID == 0x0b){	//主机向后台发送的自检查询结果
	    	logger.info("消息类型:自检查询结果");
	    	
	    	nPos.nCheckTime = GetInt4byte(pData[noffset], pData[noffset + 1], pData[noffset + 2], pData[noffset + 3]);
	    	logger.info("自检时间:" + nPos.nCheckTime);
        	noffset += 4;
        	
        	nPos.nHostCPU = GetInt2byte(pData[noffset], pData[noffset + 1]); 
        	logger.info("主机主频,MHz:" + nPos.nHostCPU);
        	noffset += 2;
        	
        	nPos.nHostCPUUsage = GetInt1byte(pData[noffset]); 
        	logger.info("主机cpu利用率，百分比:" + nPos.nHostCPUUsage);
        	noffset += 1;
        	
        	nPos.nHostRam = GetInt2byte(pData[noffset], pData[noffset + 1]); 
        	logger.info("主机内存大小,kbyte:" + nPos.nHostRam);
        	noffset += 2;
        	
        	nPos.nHostRAMUsage = GetInt1byte(pData[noffset]); 
        	logger.info("主机内存使用率，百分比:" + nPos.nHostRAMUsage);
        	noffset += 1;
        	
        	nPos.nHostFlash = GetInt2byte(pData[noffset], pData[noffset + 1]); 
        	logger.info("主机闪存 容量，kbyte:" + nPos.nHostFlash);
        	noffset += 2;
        	
        	nPos.nHostFlashUsage = GetInt1byte(pData[noffset]); 
        	logger.info("主机闪存 使用量,百分比:" + nPos.nHostFlashUsage);
        	noffset += 1;
        	
        	nPos.nHostPower = GetInt1byte(pData[noffset]); 
        	logger.info("主机当前电量，百分比:" + nPos.nHostPower);
        	noffset += 1;

        	nPos.nHostState =  ((pData[noffset] >> 7 ) & 0x01);
        	logger.info("主机工作状态，0正常，1异常:" + nPos.nHostState);
        	
        	nPos.nPairState =  ((pData[noffset] >> 6 ) & 0x01);
        	logger.info("主机配对状态,0 未配对，1已配对:" + nPos.nPairState);
        	
        	nPos.nBandState =  ((pData[noffset] >> 5 ) & 0x01);
        	logger.info("腕带闭合状态, 0破拆，1闭合:" + nPos.nBandState);
        	
        	nPos.nWristState = ((pData[noffset] >> 4 ) & 0x01);
        	logger.info("腕带工作状态，0正常，1异常:" + nPos.nWristState);
        	
        	nPos.nSIMNumberLen = (pData[noffset] & 0x0f);
        	logger.info("手机号长度:" + nPos.nSIMNumberLen);
        	noffset += 1;

            StringBuilder stbSIM = new StringBuilder();
            int SIMLen = (nPos.nSIMNumberLen + 1) /2;
            //SIM number
            for(int i = 0; i < SIMLen; i++){
                int nHN = (int)((pData[noffset + i]>> 4) & 0x0f) ;
               // stbSIM.Append(nHN.ToString());-------------------------------------
                int nLN = (int)(pData[noffset + i] & 0x0f);
               // stbSIM.Append(nLN.ToString());-------------------------------------
            }
            if(stbSIM.length() > nPos.nSIMNumberLen){
            	stbSIM.deleteCharAt((int)nPos.nSIMNumberLen);
            }
            logger.info("手机号,16进制BCD码,高位在前如：0x139542988630,6字节，手机号长为11，号码是13954298863:" + (nPos.stbSIM = stbSIM.toString()));
            noffset += SIMLen;
            
            nPos.nBandID = GetInt4byte(pData[noffset], pData[noffset + 1], pData[noffset + 2], pData[noffset + 3]);
            logger.info("配对腕带 id:" + nPos.nBandID);
            noffset += 4;
            
            nPos.nBandPower = GetInt1byte(pData[noffset]);
            logger.info("腕带当前电量，百分比:" + nPos.nBandPower);
            noffset += 1;
        }
		
		return nPos;
	}

	// //////////////
	// Message-descriptor() 位数 描述
	// {
	// Message ID 8 0x02
	// Node-count 8 节点数
	// for(i=0;i<Node-count;i++)
	// {
	// Start-time 开始时间
	// End-time 结束时间
	// period 8 心跳周期，分钟
	// }

	public int BuildHeartbeatParameter(byte[] pData) {
		int nMsgLen = 0;
		int nOffset = 0;

		pData[nOffset] = (byte) (0x02);
		nMsgLen += 1;
		nOffset += 1;
		// 待设定参数
		int nNodeCount = 5;
		pData[nOffset] = (byte) (nNodeCount);
		nMsgLen += 1;
		nOffset += 1;

		for (int i = 0; i < nNodeCount; i++) {
			int nStartTime = 8;
			int nEndTime = 18;
			int nFreq = 15;
			pData[nOffset] = (byte) ((nStartTime & 0xff000000) >> 24);
			pData[nOffset + 1] = (byte) ((nStartTime & 0x00ff0000) >> 16);
			pData[nOffset + 2] = (byte) ((nStartTime & 0x0000ff00) >> 8);
			pData[nOffset + 3] = (byte) ((nStartTime & 0x000000ff) >> 0);
			nMsgLen += 4;
			nOffset += 4;

			pData[nOffset] = (byte) ((nEndTime & 0xff000000) >> 24);
			pData[nOffset + 1] = (byte) ((nEndTime & 0x00ff0000) >> 16);
			pData[nOffset + 2] = (byte) ((nEndTime & 0x0000ff00) >> 8);
			pData[nOffset + 3] = (byte) ((nEndTime & 0x000000ff) >> 0);
			nMsgLen += 4;
			nOffset += 4;

			pData[nOffset] = (byte) (nFreq);
			nMsgLen += 1;
			nOffset += 1;

		}

		return nMsgLen;
	}
	
	//普通的回复确认消息,不带设置或查询指令
    public int BuildResponse(byte[] pData, int AckMsgID, int AckSequecne) {
    	int nOffset = 0;
        int nBufferLen = 0;
        int nMsgLen = 0;

        nBufferLen += 1;

        pData[nOffset] = (byte)(0xC5);
        nOffset += 1;
        nBufferLen += 1;
        pData[nOffset] = 0x1C;

        nOffset += 4; //ID留空
        nBufferLen += 4;

        pData[nOffset] = (byte)((AckSequecne & 0xff00) >> 8);
        pData[nOffset + 1] = (byte)((AckSequecne & 0x00ff));
        nOffset += 3;
        nBufferLen += 3;

        //msg
        pData[nOffset] = 0x00;
        nOffset += 1;
        nMsgLen += 1;

        pData[nOffset] = (byte)((AckMsgID & 0x00ff));
        nOffset += 1;
        nMsgLen += 1;

        pData[nOffset] = (byte)((AckSequecne & 0xff00) >> 8);
        pData[nOffset + 1] = (byte)((AckSequecne & 0x00ff));
        nOffset += 2;
        nMsgLen += 2;

        pData[nOffset] = 0x00;
        nOffset += 1;
        nMsgLen += 1;

         pData[8] = (byte)(nMsgLen);

         nBufferLen += nMsgLen;

        //crc
         BuildCheckCode(pData,nBufferLen);
         nBufferLen += 2;

        return nBufferLen;
    }

	//计算校验码，输入为待校验的缓存数据，已经有效长度，计算后的校验码直接附加在缓存的最后2个字节，
	//返回整体长度
	public int BuildCheckCode(byte[] pData, int nLength){
		int nBufferLen = 0;
		int nCheckCode = 0;
		int nTmp = 0;
		for(int i = 0; i < nLength; i++){
			nTmp = pData[i];
			nTmp &= 0xff;
			nCheckCode += nTmp;
		}
		
		pData[nLength] = (byte)((nCheckCode >> 8) & 0xff);
		pData[nLength+1] = (byte)(nCheckCode & 0xff);
		
		return nBufferLen;
		
	}
	
	//时间设置
    public int BuildTimeSet(byte[] pData,int time) {
    	int nOffset = 0;
        int nBufferLen = 0;
        int nMsgLen = 0;

        /*nBufferLen += 1;

        pData[nOffset] = (byte)(0xC5);
        nOffset += 1;
        nBufferLen += 1;
        pData[nOffset] = 0x1C;

        nOffset += 4; //ID留空
        nBufferLen += 4;

        pData[nOffset] = (byte)((AckSequecne & 0xff00) >> 8);
        pData[nOffset + 1] = (byte)((AckSequecne & 0x00ff));
        nOffset += 3;
        nBufferLen += 3;

        //msg
        pData[nOffset] = 0x00;
        nOffset += 1;
        nMsgLen += 1;

        pData[nOffset] = (byte)((AckMsgID & 0x00ff));
        nOffset += 1;
        nMsgLen += 1;

        pData[nOffset] = (byte)((AckSequecne & 0xff00) >> 8);
        pData[nOffset + 1] = (byte)((AckSequecne & 0x00ff));
        nOffset += 2;
        nMsgLen += 2;

        pData[nOffset] = 0x00;
        nOffset += 1;
        nMsgLen += 1;

         pData[8] = (byte)(nMsgLen);

         nBufferLen += nMsgLen;

        //crc
         BuildCheckCode(pData,nBufferLen);
         nBufferLen += 2;*/

        return nBufferLen;
    }

    /**
     * 服务器向主机发送数据电文生成
     * 
     * @param pData 电文数据对象
     * @param messageBean 服务器发送数据存放对象
     * @return 向主机发送数据电文
     */
    public int buildHostResponse(byte[] pData, Platform messageBean) {
    	//当前位置对象
    	int nOffset = 0;
    	
    	//当前长度对象
        int nBufferLen = 0;
        
        //消息长度对象
        int nMsgLen = 0;

        nBufferLen += 1;
        
        //消息头起始特征码,0xC5
        pData[nOffset] = (byte)(0xC5);
        nOffset += 1;
        nBufferLen += 1;
        
        //0x1C:包括两个部分，协议版本号,当前为0x01(高四位 1);消息传送方向,0x0C服务发给主机(低四位 C)
        pData[nOffset] = 0x1C;
        nOffset += 1;
        nBufferLen += 1;
        
        //ID留空  TODO;
        int id = messageBean.getId();
        
        //高8位中的高四位
        pData[nOffset] = (byte) ((id >> 24) & 0xff);
        nOffset += 1;
        nBufferLen += 1;
        
        //高8位中的低四位
        pData[nOffset] = (byte) ((id >> 16) & 0xff);
        nOffset += 1; 
        nBufferLen += 1;
        //低8位中的高四位
        
        pData[nOffset] = (byte) ((id >> 8) & 0xff);
        nOffset += 1; 
        nBufferLen += 1;
        
        //低8位中的低四位
        pData[nOffset] = (byte) (id & 0xff);
        nOffset += 1; 
        nBufferLen += 1;
        
        //消息序列号(Sequence)
        int sequence = messageBean.getSequence();
        
        //消息高8位
        pData[nOffset] = (byte)((sequence & 0xff00) >> 8);
        nOffset += 1; 
        nBufferLen += 1;
        
        //消息低8位      
        pData[nOffset] = (byte)(sequence & 0x00ff);
        nBufferLen += 1;
        
        //上面代码怎么加3呀 TODO;
        
        //消息ID取得
        int messageId = messageBean.getMessageId();
        
        //此处跳过了：消息有效长度, 1字节数,再生成1位
        
        nOffset += 2; 
        
        //消息ID设定
        byte mid = (byte) (messageId & 0xff);
        pData[nOffset] = mid;
        
        nMsgLen += 1;
        
        /**
         * 各分支判断
         * 应答消息:0x00
         * 心跳周期设置:0x02
         * 时间设置:0x03
         * 设置联系电话:0x04
         * 主机手环配对:0x05
         * 主机关机:0x07
         * 设置主机低电量报警的阀值:0x08
         * 主机停止工作:0x09
         * 主机开始工作:0x0a
         */
        
        //应答消息判断：空指令，只用于应答
        if(0x00 == mid) {
            //请求息的类型
            int ackMessageId = messageBean.getAckMessageId();
            
            nOffset += 1; 
            nMsgLen += 1; 
            pData[nOffset] = (byte)(ackMessageId & 0xff);
            
            //请求消息的序列号
            int ackSequence = messageBean.getAckSequence();
            
            nOffset += 1; 
            nMsgLen += 1;
            //请求消息的序列号的高4位
            pData[nOffset] = (byte)((ackSequence >> 8) & 0xff);
            
            nOffset += 1; 
            nMsgLen += 1;
            //请求消息的序列号的低4位
            pData[nOffset] = (byte)(ackSequence & 0xff);
        }
        //心跳周期设置
        else if(0x02 == mid) {
        	//节点数取得
        	int nodeCount = messageBean.getNodeCount();
        	
        	nOffset += 1; 
            nMsgLen += 1; 
            pData[nOffset] = (byte)(nodeCount & 0xff);
            
            //设置开始时间，结束时间，心跳周期
            for(int i=0;i<nodeCount;i++) {
            	//取得节点数据信息
            	PlatformNode nodeBean = (PlatformNode)messageBean.getNodeBeanList().get(i);
            	
            	//开始时间取得
            	String startTime = nodeBean.getStartTime();
            	
            	//开始时间高8位设置
            	nOffset += 1; 
                nMsgLen += 1; 
                pData[nOffset] = (byte)((Integer.parseInt(startTime.substring(0, 1)) & 0xff) << 4);
                pData[nOffset] |= (byte)(Integer.parseInt(startTime.substring(1, 2)) & 0xff);
                
                //开始时间低8位设置
            	nOffset += 1; 
                nMsgLen += 1; 
                pData[nOffset] = (byte)((Integer.parseInt(startTime.substring(2,3)) & 0xff) << 4);
                pData[nOffset] |= (byte)(Integer.parseInt(startTime.substring(3,4)) & 0xff);
                
                //结束时间取得
                String endTime = nodeBean.getEndTime();
                
                //结束时间高8位设置
                nOffset += 1; 
                nMsgLen += 1; 
                pData[nOffset] =(byte)((Integer.parseInt(endTime.substring(0, 1)) & 0xff) << 4);
                pData[nOffset] |=(byte)(Integer.parseInt(endTime.substring(1, 2)) & 0xff);
                
                //结束时间低8为设置
                nOffset += 1; 
                nMsgLen += 1; 
                pData[nOffset] =(byte)((Integer.parseInt(endTime.substring(2,3)) & 0xff) << 4);
                pData[nOffset] |=(byte)(Integer.parseInt(endTime.substring(3,4)) & 0xff);
                
                //心跳周期设置
                int period = nodeBean.getPeriod();
                
                //设置心跳周期
                nOffset += 1; 
                nMsgLen += 1;
                pData[nOffset] =(byte)(period & 0xff);
            }
            
        }
        //时间设置
        else if(0x03 == mid) {
        	//从 1970.1.1 0:0:0 到当前所经过的秒数 TODO;
        	int time = messageBean.getTime();
        	//高8位中的高四位
        	nOffset += 1;
        	nMsgLen += 1;
            pData[nOffset] = (byte) ((time & 0xff000000) >> 24);
            
            //高8位中的低四位
        	nOffset += 1;
        	nMsgLen += 1;
            pData[nOffset] = (byte) ((time >> 16) & 0xff);
            
            //低8位中的高四位
        	nOffset += 1;
        	nMsgLen += 1;
            pData[nOffset] = (byte) ((time >> 8) & 0xff);
            
            //低8位中的低四位
        	nOffset += 1;
        	nMsgLen += 1;
            pData[nOffset] = (byte) (time & 0xff);           
        }
        //设置联系电话 TODO;
        else if(0x04 == mid) {
        	//电话号码数取得
        	int count = messageBean.getPhoneCount();
        	
        	nOffset += 1;
        	nMsgLen += 1;
            pData[nOffset] = (byte) (count & 0x0f); 
            
            //循环添加电话号码
            for(int i=0; i<count; i++) {
            	//电话信息取得
            	PlatformPhone PhoneBean = (PlatformPhone)messageBean.getPhoneBeanList().get(i);
            	
            	//电话号码长度取得
            	int length = PhoneBean.getLength();
            	
            	//高3为保留，低5位为电话号码长度 TODO;
            	nOffset += 1;
            	nMsgLen += 1;
                pData[nOffset] = (byte) (length & 0x1f);
                
                //电话号码
                String phoneNumber = PhoneBean.getPhoneNumber();
                
                //低位补0
                if((phoneNumber.length() % 2) == 1) {
                	phoneNumber = phoneNumber.concat("0");
                }
            	
                //取得电话号码长度
                int len = phoneNumber.length();
                
                //循环存放电话号码
                for(int j=0; j<len; j++) {
                	//偶数位
                	if(j % 2 == 0) {
                		 nOffset += 1;
                         nMsgLen += 1; 
                		pData[nOffset] = (byte)((Integer.parseInt(phoneNumber.substring(j, j+1)) & 0xff) << 4);
                	}
                	//奇数为
                	else if(j % 2 == 1) {
                		pData[nOffset] |= (byte)(Integer.parseInt(phoneNumber.substring(j, j+1)) & 0xff);
                	}
                }
            }
        }
        //主机手环配对
        else if(0x05 == mid) {
        	//手环id取得
        	int wristId = messageBean.getWristId();
        	
        	//高8位中的高四位
        	nOffset += 1;
        	nMsgLen += 1;
            pData[nOffset] = (byte) ((wristId >> 24) & 0xff);
            
            //高8位中的低四位
        	nOffset += 1;
        	nMsgLen += 1;
            pData[nOffset] = (byte) ((wristId >> 16) & 0xff);
            
            //低8位中的高四位
        	nOffset += 1;
        	nMsgLen += 1;
            pData[nOffset] = (byte) ((wristId >> 8) & 0xff);
            
            //低8位中的低四位
        	nOffset += 1;
        	nMsgLen += 1;
            pData[nOffset] = (byte) (wristId & 0xff); 
        }
        //主机关机
        else if(0x07 == mid) {
        	//什么也不用做，因为什么也没有发
        }
        //设置主机低电量报警的阀值
        else if(0x08 == mid) {
        	//百分比取得
        	int percent = messageBean.getPercent();
        	
        	nOffset += 1;
        	nMsgLen += 1;
            pData[nOffset] = (byte) (percent & 0xff); 
        }
        //主机停止工作
        else if(0x09 == mid) {
        	//什么也不用做，因为消息内容为空
        }
        //主机开始工作
        else if(0x0a == mid) {
        	//什么也不用做，因为消息内容为空
        }
        //设备自检信息
        else if(0x0b == mid) {
        	//什么也不用做，因为消息内容为空
        }
        //主机振动检测
        else if(0x0c == mid) {
        	//什么也不用做，因为消息内容为空
        }
        //其他
        else {
        }
        
        //消息有效长度
        pData[8] = (byte)(nMsgLen);

        nBufferLen += nMsgLen;

        //crc
        BuildCheckCode(pData,nBufferLen);
        nBufferLen += 2;

       return nBufferLen;
    }
    
    /**
	 * 自动生成机器ID
	 */
	public String[] autoMadeId() {
		String[] strArr = new String[Constant.CONSANT_ID_NUM];
		strArr[0] = "[c5 13 00 0f 42 a4 00 01 0f 01 00 02 00 b9 20 c2 00 30 00 94 01 12 c8 10 05 2a]";

		for (int i = 1; i < strArr.length; i++) {
			StringBuffer sb = new StringBuffer();	// 拼装结果保存对象声明
			sb.append("[c5 13 ");

			int bNum = GetInt4byte((byte) 0x00, (byte) 0x0f, (byte) 0x42, (byte) 0xa4);	// 得到基数 1000100
			bNum = bNum + i;
			char[] chr = Integer.toHexString(bNum).toCharArray();
			char[] cr = new char[8];
			for (int j = 0; j < 8; j++) {
				if (j < chr.length) {
					cr[j] = chr[chr.length - j - 1];
				} else {
					cr[j] = 48;
				}
			}
			sb.append(String.valueOf(cr[7])).append(String.valueOf(cr[6])).append(" ").append(String.valueOf(cr[5]))
					.append(String.valueOf(cr[4])).append(" ").append(String.valueOf(cr[3]))
					.append(String.valueOf(cr[2])).append(" ").append(String.valueOf(cr[1]))
					.append(String.valueOf(cr[0])).append(" ");
			sb.append("00 01 0f 01 00 02 00 b9 20 c2 00 30 00 94 01 12 c8 10 ");

			int bsNum = GetInt2byte((byte) 0x05, (byte) 0x2a);		// 得到基数
			bsNum = bsNum + i;
			char[] chrs = Integer.toHexString(bsNum).toCharArray();
			char[] crs = new char[4];
			for (int k = 0; k < 4; k++) {
				if (k < chrs.length) {
					crs[k] = chrs[chrs.length - k - 1];
				} else {
					crs[k] = 48;
				}
			}
			sb.append(crs[3]).append(crs[2]).append(" ").append(crs[1]).append(crs[0]).append("]");

			strArr[i] = sb.toString();
			System.out.println("第" + String.valueOf(i + 1) + "条数据：" + sb.toString());
		}
		return strArr;
	}
	
	public String makeDataF18() {
		String lng = String.format("%.3f", Math.random() * 40 + 85);
		String lat = String.format("%.3f", Math.random() * 30 + 21);
		String[] datetime = DateUtil.getDateToString(DateUtil.minus8Hours(new Date()), "yyyyMMdd HHmmss").split(" ");
		String date = datetime[0].substring(6, 8) + datetime[0].substring(4, 6) + datetime[0].substring(2, 4);
		String time = datetime[1];
		return "$MGV001,123456789012345,accellence1,R," + date + "," + time + ",A," + lat + ",N," + lng + ",E,00,03,00,1.20,0.462,356.23,137.9,1.5,460,07,262C,0F54,25,0000,0000,0,0,0,28.5,28.3,,10,100,Timer;!";
	}
}
