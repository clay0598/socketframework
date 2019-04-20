package com.acc.socketframework.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.acc.socketframework.bean.F18;

public class DBUtil {
	private static Log logger = (Log) LogFactory.getLog(DBUtil.class);
	
	private static Connection getConn() {
	    String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://localhost:3306/f18";
	    String username = "root";
	    String password = "wangguan";
	    Connection conn = null;
	    try {
	        Class.forName(driver); //classLoader,加载对应驱动
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	    } catch (Exception e) {
	    	logger.error("连接数据库失败：" + e.getMessage());
	    }
	    return conn;
	}
	
	public static F18 lbsSelect(F18 position) {
	    Connection conn = getConn();
	    String sql = "select lng, lat, address from topwang_cell where mnc = " + position.getMnc() + " and lac = " + position.getLac() + " and cell = " + position.getCellId();
	    PreparedStatement pstmt = null;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        //int col = rs.getMetaData().getColumnCount();
	        while (rs.next()) {
		        position.setLng(new BigDecimal(rs.getString(1)));
		        position.setLat(new BigDecimal(rs.getString(2)));
		        position.setAddress(rs.getString(3));
	        }
	        logger.info("设备 " + position.getImei() + "获取离线lbs数据成功");
	    } catch (SQLException e) {
	    	logger.error("设备 " + position.getImei() + "获取离线lbs数据失败：" + e.getMessage());
	    }finally{
	        if(pstmt!= null) {
	        	try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
	        if(conn!= null) {
	        	try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	        }
	      }
	    return position;
	}
	
	public static int positionInsert(F18 position) {
	    int i = 0;
	    String sql = "INSERT INTO SB_POSITION(POSITION_ID, VERSION, IMEI, DEVICENAME, GPRS_FLAG, DEVICETIME, GPS_STATUS, LAT, LAT_NS, LNG, LNG_WE, "	
			+ "SATELLITE_BEIDOU, SATELLITE_GPS, SATELLITE_GLONASS, ACCURACY, SPEED, HEADING, ALTITUDE, MILEAGE, MCC, MNC, LAC, CELL_ID, "
			+ "GSM_SIGNAL, DIGITAL_INPUT, DIGITAL_OUTPUT, ANALOG_INPUT1, ANALOG_INPUT2, ANALOG_INPUT3, SENSOR1, SENSOR2, RFID, EXTERNAL, "
			+ "ELECTRICITY, ALARM, ADDRESS) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    try(Connection conn = getConn();PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql)) {
	        pstmt.setString(1, UUID.randomUUID().toString());
	        pstmt.setString(2, position.getVersion());
	        pstmt.setString(3, position.getImei());
	        pstmt.setString(4, position.getDevicename());
	        pstmt.setString(5, position.getGprsFlag());
	        pstmt.setObject(6, position.getDevicetime());
	        pstmt.setString(7, position.getGpsStatus());
	        pstmt.setBigDecimal(8, position.getLat());
	        pstmt.setString(9, position.getLatNs());
	        pstmt.setBigDecimal(10, position.getLng());
	        pstmt.setString(11, position.getLngWe());
	        pstmt.setString(12, position.getSatelliteBeidou());
	        pstmt.setString(13, position.getSatelliteGps());
	        pstmt.setString(14, position.getSatelliteGlonass());
	        pstmt.setString(15, position.getAccuracy());
	        pstmt.setString(16, position.getSpeed());
	        pstmt.setString(17, position.getHeading());
	        pstmt.setString(18, position.getAltitude());
	        pstmt.setString(19, position.getMileage());
	        pstmt.setString(20, position.getMcc());
	        pstmt.setString(21, position.getMnc());
	        pstmt.setString(22, position.getLac());
	        pstmt.setString(23, position.getCellId());
	        pstmt.setString(24, position.getGsmSignal());
	        pstmt.setString(25, position.getDigitalInput());
	        pstmt.setString(26, position.getDigitalOutput());
	        pstmt.setString(27, position.getAnalogInput1());
	        pstmt.setString(28, position.getAnalogInput2());
	        pstmt.setString(29, position.getAnalogInput3());
	        pstmt.setString(30, position.getSensor1());
	        pstmt.setString(31, position.getSensor2());
	        pstmt.setString(32, position.getRfid());
	        pstmt.setString(33, position.getExternal());
	        pstmt.setString(34, position.getElectricity());
	        pstmt.setString(35, position.getAlarm());
	        pstmt.setString(36, position.getAddress());
	        i = pstmt.executeUpdate();
	    } catch (SQLException e) {
	    	logger.error("设备 " + position.getImei() + "入库失败：" + e.getMessage());
	    }
	    return i;
	}
}