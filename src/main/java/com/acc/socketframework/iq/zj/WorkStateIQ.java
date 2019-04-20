package com.acc.socketframework.iq.zj;

import org.jivesoftware.smack.packet.IQ;

public class WorkStateIQ extends IQ {
	
	public static final String ELEMENT_NAME = "workState";
	public static final String NAMESPACE = "dzjg:iq:" + ELEMENT_NAME;

	private String typePhoneOrWatch;
	private String result;

	public String getChildElementXML() {
		StringBuffer buf = new StringBuffer();
		buf.append("<" + ELEMENT_NAME + " xmlns=\"" + NAMESPACE + "\">");
		buf.append("<type>").append(typePhoneOrWatch).append("</type>");
		buf.append("<result>").append(result).append("</result>");
		buf.append(getExtensionsXML());
		buf.append("</" + ELEMENT_NAME + ">");
		return buf.toString();
	}

	public String getTypePhoneOrWatch() {
		return typePhoneOrWatch;
	}

	public void setTypePhoneOrWatch(String typePhoneOrWatch) {
		this.typePhoneOrWatch = typePhoneOrWatch;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}