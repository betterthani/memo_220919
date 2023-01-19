package com.memo.post.bo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {

	@Autowired
	private PostDAO postDAO;

	@Autowired
	private FileManagerService fileManagerService;

	// 글쓰기
	public int addPost(int userId, String userLoginId, String subject, String content, MultipartFile file) {
		
		// 파일 업로드_서버(내 컴퓨터) -> 경로
		String imagePath = null;
		if(file != null) {
			// 파일이 있을 때만 업로드 => 이미지 경로를 얻어냄.
			imagePath = fileManagerService.saveFile(userLoginId, file);
		}
		// dao insert
		return postDAO.insertPost(userId, userLoginId, subject, content, imagePath);
	}
	
	// 글 목록 갖고오기
	public List<Post> getPostListByuserId(int userId) {
		return postDAO.selectPostListByuserId(userId);
	}
	
	// 글 상세 갖고오기
	public Post getPostByPostIdUserId(int postId, int userId) {
		return postDAO.selectPostByPostIdUserId(postId, userId);
	}
	
}
