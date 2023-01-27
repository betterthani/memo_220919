package com.memo.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.memo.post.bo.PostBO;
import com.memo.post.model.Post;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@RestController
public class PostRestController {
	
	@Autowired
	private PostBO postBO;
	
	/**
	 * 글쓰기 API
	 * @param subject
	 * @param content
	 * @param file
	 * @param session
	 * @return
	 */
	@PostMapping("/create")
	public Map<String, Object> create(
			@RequestParam("subject") String subject,
			@RequestParam(value= "content", required = false) String content,
			@RequestParam(value= "file", required = false) MultipartFile file,
			HttpSession session) {
		
		int userId = (int)session.getAttribute("userId"); // 가져올때 get (로그인 안 된 사람들어올시 error나오게 int아니면 Integer)
		String userLoginId = (String)session.getAttribute("userLoginId");
		Map<String, Object> result = new HashMap<>();
		
		// db insert
		int rowCount = postBO.addPost(userId, userLoginId, subject, content, file);
		if(rowCount > 0) {
			result.put("code", 1);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("errorMessage", "메모 저장에 실패했습니다. 관리자에게 문의하세요.");
		}
		
		return result;
	}
	
	/**
	 * 글 수정API
	 * @param postId
	 * @param subject
	 * @param content
	 * @param file
	 * @param session
	 * @return
	 */
	@PutMapping("/update")
	public Map<String, Object> update(
			@RequestParam("postId") int postId,
			@RequestParam("subject") String subject,
			@RequestParam(value="content", required = false) String content,
			@RequestParam(value="file", required = false) MultipartFile file,
			HttpSession session) {
		
		int userId = (int)session.getAttribute("usreId");
		String userLoginId = (String)session.getAttribute("userLoginId");
		
		//update db
		postBO.updatePost(userId, userLoginId, postId, subject, content, file);
		
		Map<String, Object> result = new HashMap<>();
		result.put("code", 1);
		result.put("result", "성공");
		
		return result;
	}
	
	// 글삭제 API
	@DeleteMapping("/delete")
	public Map<String, Object> delete(
			@RequestParam("postId") int postId,
			HttpSession session){
		
		// 삭제할때는 로그인된 아이디의 포스트 번호가 맞는지 확인을 위해 user의 정보를 꺼내는 것이 좋다.
		int userId = (int)session.getAttribute("userId");
		int rowCount = postBO.deletePostByPostIdUserId(postId, userId);
		Map<String, Object> result = new HashMap<>();
		if(rowCount > 0) {
			result.put("code", 1);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("errorMessaget", "메모 삭제에 실패했습니다. 관리자에게 문의해주세요.");
		}
		
		return result;
		
	}
	
}
