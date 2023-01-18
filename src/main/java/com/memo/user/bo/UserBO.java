package com.memo.user.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.memo.user.dao.UserDAO;
import com.memo.user.model.User;

@Service
public class UserBO {
	
	@Autowired
	private UserDAO userDAO;
	
	// 중복확인
	public boolean existLoginId(String loginId) {
		return userDAO.existLoginId(loginId);
	}
	
	//회원가입
	public void addUserSignUpByloginIdPasswordNameEmail(String loginId, String password, String name, String email) {
		userDAO.insertUserSignUpByloginIdPasswordNameEmail(loginId, password, name, email);
	}
	
	// 로그인 
	public User getUserByLoginIdPassword(String loginId, String password) { // db의 가까운 영역이기때문에 hashing까진 필요없고 password면 된다.
		return userDAO.selectUserByLoginIdPassword(loginId, password);
	}
	

}
