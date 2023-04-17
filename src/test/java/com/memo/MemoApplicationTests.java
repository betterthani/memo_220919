package com.memo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemoApplicationTests {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	// 단축키 : alt + shift+ x , t
	@Test
	void contextLoads() {
		logger.info("############### logger test");

	}

	@Test
	void 더하기테스트() {
		logger.info("$$$$$$$$$$$$$$ 더하기 테스트 시작");
		int a = 10;
		int b = 20;
		// 정답, 리턴값 -> 정규식도 가능
		assertEquals(30, a + b);
	}

}
