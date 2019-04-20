package com.acc.socketframework.bean;

import java.util.List;

/**
 * 服务器向主机发送数据Bean类对象
 * 
 * @author Administrator
 */
public class Platform {

	/**
	 *消息头起始特征码,0xC5
	 */
	private int magicNumber;
	
	/**
	 *协议版本号
	 */
	private int version;
	
	/**
	 *消息传送方向,0x0C服务发给主机，0x03主机发给服务器
	 */
	private int direction;
	
	/**
	 *主机设备ID
	 */
	private int id;
	
	/**
	 *消息序列号，由发送者维护，每发送一个消息增加一，到65535后，回到0
	 */
	private int sequence;
	
	/**
	 *消息有效长度, Message-descriptor()的字节数
	 */
	private int length;
	
	/**
	 *校验和,从特征码开始到消息内容结束，所有值的和
	 */
	private int checkSum;
	
	/**
	 *消息ID(消息类型:心跳周期设置-0x02;时间设置-0x03;设置联系电话-0x04;主机手环配对-0x05;主机关机-0x07;设置主机低电量报警的阀值-0x08;主机停止工作-0x09;主机开始工作-0x0a)
	 */
	private int messageId;
	
	/**
	 *请求息的类型
	 */
	private int ackMessageId;
	
	/**
	 *请求消息的序列号
	 */
	private int ackSequence;
	
	/**
	 *节点数
	 */
	private int nodeCount;
	
	/**
	 *节点数据信息Bean对象
	 */
	private List<PlatformNode> nodeBeanList;
	
	/**
	 *时间（秒数）
	 */
	private int time;
	
	/**
	 *保留
	 */
	private int reserve;
	
	/**
	 * 电话号码数
	 */
	private int phoneCount;
	
	/**
	 *电话号码数据信息Bean对象
	 */
	private List<PlatformPhone> phoneBeanList;
	
	/**
	 *手环id
	 */
	private int wristId;
	
	/**
	 *百分比
	 */
	private int percent;
	
	
	/**
	 * 电文数据
	 */
	private byte[] pData;
	
	/**
	 * 电文长度
	 */
	private int plength;
	
	/**
	 *消息头起始特征码,0xC5数据取得
	 *
	 *@return 消息头起始特征码,0xC5数据
	 */
	public int getMagicNumber()
	{
	    return this.magicNumber;
	}

	/**
	 *消息头起始特征码,0xC5数据设定
	 *
	 *@param magicNumber
	 */
	public void setMagicNumber(int magicNumber)
	{
	    this.magicNumber = magicNumber;
	}

	/**
	 *协议版本号数据取得
	 *
	 *@return 协议版本号数据
	 */
	public int getVersion()
	{
	    return this.version;
	}

	/**
	 *协议版本号数据设定
	 *
	 *@param version
	 */
	public void setVersion(int version)
	{
	    this.version = version;
	}

	/**
	 *消息传送方向,0x0C服务发给主机，0x03主机发给服务器数据取得
	 *
	 *@return 消息传送方向,0x0C服务发给主机，0x03主机发给服务器数据
	 */
	public int getDirection()
	{
	    return this.direction;
	}

	/**
	 *消息传送方向,0x0C服务发给主机，0x03主机发给服务器数据设定
	 *
	 *@param direction
	 */
	public void setDirection(int direction)
	{
	    this.direction = direction;
	}

	/**
	 *主机设备ID数据取得
	 *
	 *@return 主机设备ID数据
	 */
	public int getId()
	{
	    return this.id;
	}

	/**
	 *主机设备ID数据设定
	 *
	 *@param id
	 */
	public void setId(int id)
	{
	    this.id = id;
	}

	/**
	 *消息序列号，由发送者维护，每发送一个消息增加一，到65535后，回到0数据取得
	 *
	 *@return 消息序列号，由发送者维护，每发送一个消息增加一，到65535后，回到0数据
	 */
	public int getSequence()
	{
	    return this.sequence;
	}

	/**
	 *消息序列号，由发送者维护，每发送一个消息增加一，到65535后，回到0数据设定
	 *
	 *@param sequence
	 */
	public void setSequence(int sequence)
	{
	    this.sequence = sequence;
	}

	/**
	 *消息有效长度, Message-descriptor()的字节数数据取得
	 *
	 *@return 消息有效长度, Message-descriptor()的字节数数据
	 */
	public int getLength()
	{
	    return this.length;
	}

	/**
	 *消息有效长度, Message-descriptor()的字节数数据设定
	 *
	 *@param length
	 */
	public void setLength(int length)
	{
	    this.length = length;
	}

	/**
	 *校验和,从特征码开始到消息内容结束，所有值的和数据取得
	 *
	 *@return 校验和,从特征码开始到消息内容结束，所有值的和数据
	 */
	public int getCheckSum()
	{
	    return this.checkSum;
	}

	/**
	 *校验和,从特征码开始到消息内容结束，所有值的和数据设定
	 *
	 *@param checkSum
	 */
	public void setCheckSum(int checkSum)
	{
	    this.checkSum = checkSum;
	}

	/**
	 *消息ID(消息类型:心跳周期设置-0x02;时间设置-0x03;设置联系电话-0x04;主机手环配对-0x05;主机关机-0x07;设置主机低电量报警的阀值-0x08;主机停止工作-0x09;主机开始工作-0x0a)数据取得
	 *
	 *@return 消息ID(消息类型:心跳周期设置-0x02;时间设置-0x03;设置联系电话-0x04;主机手环配对-0x05;主机关机-0x07;设置主机低电量报警的阀值-0x08;主机停止工作-0x09;主机开始工作-0x0a)数据
	 */
	public int getMessageId()
	{
	    return this.messageId;
	}

	/**
	 *消息ID(消息类型:心跳周期设置-0x02;时间设置-0x03;设置联系电话-0x04;主机手环配对-0x05;主机关机-0x07;设置主机低电量报警的阀值-0x08;主机停止工作-0x09;主机开始工作-0x0a)数据设定
	 *
	 *@param messageId
	 */
	public void setMessageId(int messageId)
	{
	    this.messageId = messageId;
	}

	/**
	 *请求息的类型数据取得
	 *
	 *@return 请求息的类型数据
	 */
	public int getAckMessageId()
	{
	    return this.ackMessageId;
	}

	/**
	 *请求息的类型数据设定
	 *
	 *@param ackMessageType
	 */
	public void setAckMessageId(int ackMessageId)
	{
	    this.ackMessageId = ackMessageId;
	}

	/**
	 *请求消息的序列号数据取得
	 *
	 *@return 请求消息的序列号数据
	 */
	public int getAckSequence()
	{
	    return this.ackSequence;
	}

	/**
	 *请求消息的序列号数据设定
	 *
	 *@param ackSequence
	 */
	public void setAckSequence(int ackSequence)
	{
	    this.ackSequence = ackSequence;
	}

	/**
	 *节点数数据取得
	 *
	 *@return 节点数数据
	 */
	public int getNodeCount()
	{
	    return this.nodeCount;
	}

	/**
	 *节点数数据设定
	 *
	 *@param nodeCount
	 */
	public void setNodeCount(int nodeCount)
	{
	    this.nodeCount = nodeCount;
	}

	/**
	 *节点数据信息Bean对象数据取得
	 *
	 *@return 节点数据信息Bean对象数据
	 */
	public List<PlatformNode> getNodeBeanList()
	{
	    return this.nodeBeanList;
	}

	/**
	 *节点数据信息Bean对象数据设定
	 *
	 *@param nodeBean
	 */
	public void setNodeBeanList(List<PlatformNode> nodeBeanList)
	{
	    this.nodeBeanList = nodeBeanList;
	}

	/**
	 *时间（秒数）数据取得
	 *
	 *@return 时间（秒数）数据
	 */
	public int getTime()
	{
	    return this.time;
	}

	/**
	 *时间（秒数）数据设定
	 *
	 *@param time
	 */
	public void setTime(int time)
	{
	    this.time = time;
	}

	/**
	 *保留数据取得
	 *
	 *@return 保留数据
	 */
	public int getReserve()
	{
	    return this.reserve;
	}

	/**
	 *保留数据设定
	 *
	 *@param reserve
	 */
	public void setReserve(int reserve)
	{
	    this.reserve = reserve;
	}

	/**
	 *电话号码数据信息Bean对象数据取得
	 *
	 *@return 电话号码数据信息Bean对象数据
	 */
	public List<PlatformPhone> getPhoneBeanList()
	{
	    return this.phoneBeanList;
	}

	/**
	 *电话号码数据信息Bean对象数据设定
	 *
	 *@param phoneBeanList
	 */
	public void setPhoneBeanList(List<PlatformPhone> phoneBeanList)
	{
	    this.phoneBeanList = phoneBeanList;
	}

	/**
	 *手环id数据取得
	 *
	 *@return 手环id数据
	 */
	public int getWristId()
	{
	    return this.wristId;
	}

	/**
	 *手环id数据设定
	 *
	 *@param wristId
	 */
	public void setWristId(int wristId)
	{
	    this.wristId = wristId;
	}

	/**
	 *百分比数据取得
	 *
	 *@return 百分比数据
	 */
	public int getPercent()
	{
	    return this.percent;
	}

	/**
	 *百分比数据设定
	 *
	 *@param percent
	 */
	public void setPercent(int percent)
	{
	    this.percent = percent;
	}

	public int getPhoneCount() {
		return phoneCount;
	}

	public void setPhoneCount(int phoneCount) {
		this.phoneCount = phoneCount;
	}

	public byte[] getPData() {
		return pData;
	}

	public void setPData(byte[] data) {
		pData = data;
	}

	public int getPlength() {
		return plength;
	}

	public void setPlength(int plength) {
		this.plength = plength;
	}

}
