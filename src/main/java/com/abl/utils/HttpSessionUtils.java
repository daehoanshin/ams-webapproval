package com.abl.utils;

import javax.servlet.http.HttpSession;

import com.abl.domain.User;


public class HttpSessionUtils {
	public static final String USERID_SESSION_KEY = "rtcUserId";
	public static final String PASSWORD_SESSION_KEY = "rtcUserPwd";
	public static final String RTCID_SESSION_KEY = "rtc_id";
	
	public static boolean isLoginUser(HttpSession session) {
		Object sessionedUser = session.getAttribute(USERID_SESSION_KEY);
		if(sessionedUser == null) {
			return false;
		}
		return true;
	}
	public static String getUserFormSession(HttpSession session) {
		if(!isLoginUser(session)) {
			return null;
		}
		return (String) session.getAttribute(USERID_SESSION_KEY);
	}
	public static void setUserSession(HttpSession session, User loginUser, String wiid) {
		session.setAttribute(USERID_SESSION_KEY, loginUser.getUser_id());
		session.setAttribute(PASSWORD_SESSION_KEY, loginUser.getUser_pw());
		session.setAttribute(RTCID_SESSION_KEY, wiid);
	}
	
	public static void removeUserSession(HttpSession session) {
		session.removeAttribute(USERID_SESSION_KEY);
		session.removeAttribute(PASSWORD_SESSION_KEY);
		session.removeAttribute(RTCID_SESSION_KEY);
	}
	
}
