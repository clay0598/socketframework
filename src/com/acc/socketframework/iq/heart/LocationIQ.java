package com.acc.socketframework.iq.heart;

import org.jivesoftware.smack.packet.IQ;

public class LocationIQ extends IQ {
	
	public static final String ELEMENT_NAME = "location";
	public static final String NAMESPACE = "dzjg:iq:location";

	private String time;
	private String X;
	private String Y;

	public String getChildElementXML() {
		StringBuffer buf = new StringBuffer();
		buf.append("<" + ELEMENT_NAME + " xmlns=\"" + NAMESPACE + "\">");
		//if (getType() == IQ.Type.SET) {
			buf.append("<X>").append(X).append("</X>");
			buf.append("<Y>").append(Y).append("</Y>");
			buf.append("<time>").append(time).append("</time>");
			buf.append(getExtensionsXML());
		//}
		buf.append("</" + ELEMENT_NAME + ">");
		return buf.toString();
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getX() {
		return X;
	}

	public void setX(String x) {
		X = x;
	}

	public String getY() {
		return Y;
	}

	public void setY(String y) {
		Y = y;
	}

}