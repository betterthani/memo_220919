package com.memo.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.memo.user.bo.UserBO;
import com.memo.user.model.User;

@SpringBootTest
class UserControllerTest {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	UserBO userBO;
	
	// 단축키 : alt + shift + x , t
	@Test
	void contextLoads() {
		logger.info("########## logger test");
	}
	
	// src / main / 클래스  > new > Junit Test Case 테스트 파일 생성
	// @Test
	void test() {
													// id , pw
		User user = userBO.getUserByLoginIdPassword("test", "98f6bcd4621d373cade4e832627b4f6");
		assertNotNull(user);
		
		
	}
	
	@Transactional // rollback
	@Test
	void 유저추가테스트() {
		userBO.addUserSignUpByloginIdPasswordNameEmail("bbbb111","bbb111b","bb111bb","bbb@b111bbb"); // 유저insert문 테스트
	}
	
	@Test
	void null체크() {
		String a = null; // 비어있다.
		if(ObjectUtils.isEmpty(a)) { // null 체크
			logger.info("비어있다.");
		}else {
			logger.info("비어있지 않다.");
		}
		
		List<String> list = null; // true
		logger.info(ObjectUtils.isEmpty(list) + "");
	}

}
