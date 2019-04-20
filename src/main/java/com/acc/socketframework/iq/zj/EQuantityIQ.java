package com.acc.socketframework.iq.zj;

import org.jivesoftware.smack.packet.IQ;

public class EQuantityIQ extends IQ {
	
	public static final String ELEMENT_NAME = "eQuantity";
	public static final String NAMESPACE = "dzjg:iq:" + ELEMENT_NAME;

	private String typePhoneOrWatch;
	private String value;

	public String getChildElementXML() {
		StringBuffer buf = new StringBuffer();
		buf.append("<" + ELEMENT_NAME + " xmlns=\"" + NAMESPACE + "\">");
		buf.append("<type>").append(typePhoneOrWatch).append("</type>");
		buf.append("<value>").append(value).append("</value>");
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}