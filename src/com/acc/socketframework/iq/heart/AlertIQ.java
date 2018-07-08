package com.acc.socketframework.iq.heart;

import org.jivesoftware.smack.packet.IQ;

public class AlertIQ extends IQ {

	public static final String ELEMENT_NAME = "alert";
	public static final String NAMESPACE = "dzjg:iq:alert";

	private String alertType;
	private String alertTime;
	private String X;
	private String Y;
	private String ids;

	public String getChildElementXML() {
		StringBuffer buf = new StringBuffer();
		buf.append("<" + ELEMENT_NAME + " xmlns=\"" + NAMESPACE + "\">");
		//if (getType() == IQ.Type.SET) {
			buf.append("<alertType>").append(alertType).append("</alertType>");
			buf.append("<alertTime>").append(alertTime).append("</alertTime>");
			buf.append("<X>").append(X).append("</X>");
			buf.append("<Y>").append(Y).append("</Y>");

			if (ids != null && !ids.equals("")) {
				buf.append("<IDS>");
				for (String id : ids.split(";")) {
					buf.append("<ID>").append(id).append("</ID>");
				}
				buf.append("</IDS>");
			}

			buf.append(getExtensionsXML());
		//}
		buf.append("</" + ELEMENT_NAME + ">");
		return buf.toString();
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	public String getAlertTime() {
		return alertTime;
	}

	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
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

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

}