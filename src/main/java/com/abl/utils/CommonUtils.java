package com.abl.utils;

import java.text.SimpleDateFormat;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtils {
	protected static Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	
	protected static Properties properties;
	
	public static void setProperties(Properties properties) {
		CommonUtils.properties = properties;
	}
	
	public static String[] replaceComma(String lockListStr) {
		return lockListStr.split(",");
	}

	public static String getDate(Long lstUpdDate) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd / HH:mm:ss");
		return sf.format(lstUpdDate);
	}
	
	public static String replacePath(String path) {
		if (path.contains("\\")) {
			return path.replaceAll("\\\\", "#");
		} else if (path.contains("//")) {
			return path.replaceAll("\\\\", "\\\\");
		}
		return path.replace("#", "\\\\");
	}

	public static String getDepth(String path) {
		String[] splitPath = path.split("#");

		return String.valueOf(splitPath.length - 1);
	}
}