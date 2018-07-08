package com.acc.socketframework.iq.set;

import org.jivesoftware.smack.packet.IQ;

public class SetIQ extends IQ {
	
	private String ELEMENT_NAME = "";
	private String NAMESPACE = "";
	
	public SetIQ(String ELEMENT_NAME) {
		this.ELEMENT_NAME = ELEMENT_NAME;
		this.NAMESPACE = "dzjg:iq:" + ELEMENT_NAME;
	}

	public String getChildElementXML() {
		StringBuffer buf = new StringBuffer();
		buf.append("<" + ELEMENT_NAME + " xmlns=\"" + NAMESPACE + "\">");
		buf.append(getExtensionsXML());
		buf.append("</" + ELEMENT_NAME + ">");
		return buf.toString();
	}

}