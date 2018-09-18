package com.abl.dao;

import org.springframework.stereotype.Repository;

import com.abl.domain.User;


@Repository("userDao")
public class UserDaoImpl implements UserDao{

	@Override
	public User getUserInfo(User paramDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateUserPw(User paramDto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertCheckinInfo(User paramDto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public User getCheckinInfo(User paramDto) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
