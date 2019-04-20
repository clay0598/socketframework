package com.acc.socketframework.util;

/**
 * @(#)jizhanyun.com for java
 *
 * 基站云平台接口演示FOR JAVA
 *
 * @基站云
 * @version 1.00
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.acc.socketframework.bean.F18;

public class LbsAPI {
	private static Log logger = (Log) LogFactory.getLog(LbsAPI.class);
    
    //public static String apikey    = "1AFF2C6D9FFF0825DA88B864A3D1E034"; //apikey 基站云平台获取
    //public static String mnc       = "0";                                //mnc
    //public static String lac       = "20857";                            //lac
    //public static String cell      = "59051";                            //cell
    
    public static String apikey    = "0B0AD3F20A9243A9F91E2D024ACB7B68"; 								//apikey 基站云平台获取
    public static String mnc       = "00";                                								//mnc
    public static String lac       = String.valueOf(Integer.parseInt("1047",16));                       //lac
    public static String cell      = String.valueOf(Integer.parseInt("D8B0",16));                       //cell

    public static F18 apiGet(F18 position){
    	String data = getWebData("http://www.jizhanyun.com/api/?mnc=" + position.getMnc() + "&lac=" + position.getLac() + "&cell=" + position.getCellId() + "&ishex=10&type=1&apikey="+apikey);
        String code="";
        String about="";
        String lng="";
        String lat="";
        String address="";
        if(data!=""){
            String[] arr = data.split("\\|");
            code = arr[0];
            if(code.equals("001")){
                lng = arr[2];
                lat = arr[3];
                address = arr[6];
                position.setLng(new BigDecimal(lng));
                position.setLat(new BigDecimal(lat));
                position.setAddress(address);
                logger.info("设备 " + position.getImei() + "调用基站云接口成功");
            }else{
                about = arr[1];
                logger.error("设备 " + position.getImei() + "调用基站云接口失败：" + about);
            }
        }
    	return position;
    }
    
    public static String getWebData(String domain) {
        StringBuffer sb = new StringBuffer();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader in = null;
        try {
            java.net.URL url = new java.net.URL(domain);
            is = url.openStream();
            isr = new InputStreamReader(is,"utf-8");
            in = new BufferedReader(isr);
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\n");
            }
            in.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(in!=null){
                    in.close();
                    in=null;
                }
                if(isr!=null){
                    isr.close();
                    isr=null;
                }
                if(is!=null){
                    is.close();
                    is=null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        String data=getWebData("http://www.jizhanyun.com/api/?mnc="+mnc+"&lac="+lac+"&cell="+cell+"&ishex=10&type=1&apikey="+apikey);
        String code="";
        String about="";
        String lng="";
        String lat="";
        String glng="";
        String glat="";
        String address="";
        if(data!=""){
            String[] arr=data.split("\\|");
            code=arr[0];
            if(code.equals("001")){
                lng=arr[2];
                lat=arr[3];
                glng=arr[4];
                glat=arr[5];
                address=arr[6];
            }else{
                about=arr[1];
            }
        }
        System.out.println("状态："+code);
        System.out.println("说明："+about);
        System.out.println("经度："+lng);
        System.out.println("纬度："+lat);
        System.out.println("谷歌地图经度："+glng);
        System.out.println("谷歌地图经度："+glat);
        System.out.println("地址："+address);
    }
}
