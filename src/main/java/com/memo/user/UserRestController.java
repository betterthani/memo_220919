package com.memo.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.memo.common.EncryptUtils;
import com.memo.user.bo.UserBO;
import com.memo.user.model.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserRestController {
	
		@Autowired
		private UserBO userBO;
		
		/**
		 * 아이디 중복확인 API
		 * @param loginId
		 * @return
		 */
		@RequestMapping("/is_duplicated_id") // get,post 둘 다 될때 사용가능
		public Map<String, Object> isDuplicatedId(
				@RequestParam("loginId") String loginId) {
			
			Map<String,Object> result = new HashMap<>();
			boolean isDuplicated = false;
			try{
				isDuplicated = userBO.existLoginId(loginId);
			} catch (Exception e) {
				result.put("code",	500);
				result.put("errorMessaget", "중복 확인실패");
				return result;
			}
			if(isDuplicated) {
				// 중복일때
				result.put("code", 1);
				result.put("result", true);
			} else {
				// 중복 아니고 사용가능할 떄
				result.put("code", 1);
				result.put("result", false);
			}
			return result;
		}
		
		/**
		 * 회원가입 API
		 * @param loginId
		 * @param password
		 * @param name
		 * @param email
		 * @return
		 */
		@PostMapping("/sign_up")
		public Map<String, Object> signUp(
				@RequestParam("loginId") String loginId,
				@RequestParam("password") String password,
				@RequestParam("name") String name,
				@RequestParam("email") String email
				) {
			// 비밀번호 해싱(hashing) - md5:제일 보안 취약 방식 (보안강화를 위해 다른 알고리즘 찾아서 해도된다)
			String hashedPassword = EncryptUtils.md5(password);
			
			// db insert
			userBO.addUserSignUpByloginIdPasswordNameEmail(loginId, hashedPassword, name, email);

			Map<String, Object> result = new HashMap<>();
			result.put("code", 1);
			result.put("result", "성공");
			
			return result;
		}
		
		/**
		 * 로그인 API
		 * @param loginId
		 * @param password
		 * @param request
		 * @return
		 */
		@PostMapping("/sign_in")
		public Map<String, Object> signIn(
				@RequestParam("loginId") String loginId,
				@RequestParam("password") String password,
				HttpServletRequest request){
			
			// 비밀번호 해싱(첫 브레이크 포인트)
			String hashedPassword = EncryptUtils.md5(password);
			
			//db select (if문 조건 다 실행 후 두번째 브레이크 포인트)
			User user = userBO.getUserByLoginIdPassword(loginId, hashedPassword);
			
			Map<String,Object> result = new HashMap<>();
			if(user != null) {
				// 행이 있으면 로그인
				result.put("code", 1);
				result.put("result", "성공");
				
				// 세션에 유저 정보를 담는다.(로그인 상태 유지) - 모든곳에서 세션이 사용가능하다.(컨트롤러, view에 많이 사용) id, name,loginId정도 담는다.
				HttpSession session = request.getSession();
				// id는 반드시 필요하다(글쓸때 등 필요) 그래서 세션이 담아둔다. (password는 담지 않는다.) 
				session.setAttribute("userId", user.getId());
				session.setAttribute("userLoginId", user.getLoginId());
				session.setAttribute("userName", user.getName());
				
			} else {
				// 행이 없으면 로그인 실패
				result.put("code", 500);
				result.put("errorMessage", "존재하지 않는 사용자입니다.");
			}
			
			// return map
			return result;
		}
}
