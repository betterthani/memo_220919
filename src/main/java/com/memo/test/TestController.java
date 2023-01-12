package com.memo.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.memo.post.dao.PostDAO;

@Controller
public class TestController {
	
	@Autowired
	private PostDAO postDAO;
	
	// String return 점검
	@GetMapping("/test1")
	@ResponseBody
	public String test1() {
		return "hello world!";
	}
	
	// Map return 점검
	@GetMapping("/test2")
	@ResponseBody
	public Map<String, Object> test2() {
		Map<String,Object> result = new HashMap<>();
		result.put("키1", 1111);
		result.put("키2", 222);
		result.put("키3", 3333);
		
		return result;
	}
	
	// jsp 리턴 점검
	@GetMapping("/test3")
	public String test3() {
		return "test/test";
	}
	
	// DB 접속 정보
	@GetMapping("/test4")
	@ResponseBody
	public List<Map<String, Object>> test4(){
		return postDAO.selectPostListTest();
	}
}
