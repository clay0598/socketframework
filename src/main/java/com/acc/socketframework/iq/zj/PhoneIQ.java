package com.acc.socketframework.iq.zj;

import org.jivesoftware.smack.packet.IQ;

public class PhoneIQ extends IQ {
	
	public static final String ELEMENT_NAME = "phone";
	public static final String NAMESPACE = "dzjg:iq:" + ELEMENT_NAME;

	private String time;
	private String number;

	public String getChildElementXML() {
		StringBuffer buf = new StringBuffer();
		buf.append("<" + ELEMENT_NAME + " xmlns=\"" + NAMESPACE + "\">");
		buf.append("<time>").append(time).append("</time>");
		buf.append("<number>").append(number).append("</number>");
		buf.append(getExtensionsXML());
		buf.append("</" + ELEMENT_NAME + ">");
		return buf.toString();
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}