package com.abl.dao;

import com.abl.domain.User;

public interface UserDao {
	
	public User getUserInfo(User paramDto);
	
	public int updateUserPw(User paramDto);
	
	public int insertCheckinInfo(User paramDto);
	
	public User getCheckinInfo(User paramDto);

}
