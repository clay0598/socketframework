package com.acc.socketframework.iq.heart;

import com.acc.socketframework.util.DateUtil;
import org.jivesoftware.smack.packet.IQ;

import java.util.Date;

public class TimeIQ extends IQ {
	
	public static final String ELEMENT_NAME = "time";
	public static final String NAMESPACE = "dzjg:iq:time";
	private Date time;

	public String getChildElementXML() {
		StringBuffer buf = new StringBuffer();
		buf.append("<" + ELEMENT_NAME + " xmlns=\"" + NAMESPACE + "\">");
		buf.append("<value>").append(DateUtil.getDateToString(time, "yyyyMMddHHmmss")).append("</value>");
		buf.append(getExtensionsXML());
		buf.append("</" + ELEMENT_NAME + ">");
		return buf.toString();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}