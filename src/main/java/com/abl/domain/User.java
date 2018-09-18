package com.abl.domain;

import org.springframework.stereotype.Service;

@Service
public class User {
	private String user_id;
	private String user_nm;
	private String user_pw;
	private String rtc_id;
	private String workspace;
	private String list;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_nm() {
		return user_nm;
	}
	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}
	public String getUser_pw() {
		return user_pw;
	}
	public void setUser_pw(String user_pw) {
		this.user_pw = user_pw;
	}
	
	public boolean matchPassword(String newPassword) {
		if(newPassword == null) {
			return false;
		}
		return newPassword.equals(user_pw);
	}
	
	public String getRtc_id() {
		return rtc_id;
	}
	public void setRtc_id(String rtc_id) {
		this.rtc_id = rtc_id;
	}
	public String getWorkspace() {
		return workspace;
	}
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user_pw == null) ? 0 : user_pw.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (user_pw == null) {
			if (other.user_pw != null)
				return false;
		} else if (!user_pw.equals(other.user_pw))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", user_nm=" + user_nm + ", rtc_id=" + rtc_id + "]";
	}
	public boolean matchUserId(String sessionUserId) {
		if(sessionUserId == null) {
			return false;
		}
		return sessionUserId.equals(user_id);
	}
	
}
