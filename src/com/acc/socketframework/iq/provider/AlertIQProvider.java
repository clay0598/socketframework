package com.acc.socketframework.iq.provider;

import java.io.IOException;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.acc.socketframework.iq.heart.AlertIQ;

public class AlertIQProvider implements IQProvider{  

	private static final String PREFERRED_ENCODING = "UTF-8";
	
	/***通过这个方法我们可以获取从服务器传来iq**/  
	
	public IQ parseIQ(XmlPullParser parser) throws Exception {  
		final StringBuilder sb = new StringBuilder();  
		try {
			int event = parser.getEventType();
			// get the content
			while (true) {
				switch (event) {
					case XmlPullParser.TEXT:
					// We must re-escape the xml so that the DOM won't throw an exception
					sb.append(StringUtils.escapeForXML(parser.getText()));
					break;
					case XmlPullParser.START_TAG:
					sb.append('<').append(parser.getName()).append('>');
					break;
					case XmlPullParser.END_TAG:  
					sb.append("</").append(parser.getName()).append('>');  
					break;  
					default:
				}
				if (event == XmlPullParser.END_TAG && "query".equals(parser.getName())) {
					break;  
				}
				event = parser.next(); 
			}
		} catch (XmlPullParserException e) {  
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		String xmlText = sb.toString();
		System.out.println(xmlText);
		return new AlertIQ();
	}
}  