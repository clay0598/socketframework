package com.acc.socketframework.iq.zj;

import org.jivesoftware.smack.packet.IQ;

public class MemoryIQ extends IQ {
	
	public static final String ELEMENT_NAME = "memory";
	public static final String NAMESPACE = "dzjg:iq:" + ELEMENT_NAME;

	private String time;
	private String total;
	private String percent;

	public String getChildElementXML() {
		StringBuffer buf = new StringBuffer();
		buf.append("<" + ELEMENT_NAME + " xmlns=\"" + NAMESPACE + "\">");
		buf.append("<time>").append(time).append("</time>");
		buf.append("<total>").append(total).append("</total>");
		buf.append("<percent>").append(percent).append("</percent>");
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

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

}