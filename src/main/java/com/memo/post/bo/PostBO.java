package com.memo.post.bo;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.memo.common.FileManagerService;
import com.memo.post.dao.PostDAO;
import com.memo.post.model.Post;

@Service
public class PostBO {
	
//	private Logger logger = LoggerFactory.getLogger(PostBO.class);
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final int POST_MAX_SIZE = 3; // 페이징 (한페이지당 몇개 갖고올것인지)
	
	@Autowired
	private PostDAO postDAO;

	@Autowired
	private FileManagerService fileManagerService;

	// 글쓰기(추가)
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
	
	// 글 수정
	public void updatePost(int userId, String userLoginId,
			int postId, String subject, String content, MultipartFile file) {
		
		// 기존 글을 가져온다.(이미지가 교체될 떄 기존 이미지 제거를 위해)
		Post post = getPostByPostIdUserId(postId, userId); // userId까지 껴있으면 완전히 내 글을 가져오게됨.
		if(post == null) {
			// 수정할 글이 없음 (이상현상으로 로그 남기기- 레벨은 내가 잡기)
			logger.warn("[update post] 수정할 메모가 존재하지 않습니다. postId:{},userId:{}", postId,userId);
			return; 
		}
		
		// 수정글 있는 경우
		// 멀티파일이 비어있지 않다면 업로드 후 imagePath => 업로드가 성공하면 기존 이미지 제거, 그렇지 않으면 그대로 남김
		String imagePath = null;
		if(file != null) {
			// 업로드
			imagePath = fileManagerService.saveFile(userLoginId, file); // filemanager에서 null 파악해줌
			
			// 업로드 성공하면 기존 이미지 제거 => 업로드가 실패할 수 있으므로, 업로드가 성공한 후 제거
			// imagePath가 널이 아니고, 기존 글에 이미지 패스가 널이 아닐 경우
			if(imagePath != null && post.getImagePath() != null) {
				// 삭제할 이미지가 존재한다. -> 기존 이미지 제거
				fileManagerService.deleteFile(post.getImagePath());
			}
		}
		
		// db update
		postDAO.updatePostByPostIdUserId(postId, userId, subject, content, imagePath);
		
	}
	
	// 글 목록 갖고오기
	public List<Post> getPostListByuserId(int userId, Integer prevId, Integer nextId) {
		
		// 게시글 번호(postId) : 10 9 8 | 7 6 5 | 4 3 2 | 1
		// 만약 내가 4 3 2 페이지에 있을 때
		// 1) 이전 : 10 9 8 | 7 6 5 -> 정방향ASC 4보다 큰 3개 (5 6 7) => List reverse(가져온 데이터 뒤집기) => 7 6 5
		// 2) 다음 : 2보다 작은 3개 DESC
		// 3) 첫페이지(이전, 다음 없음) 무조건 DESC 3개 가져오면 됨
		
		String direction = null; // 방향(ASC이전, DESC다음)
		Integer standardId = null; // 기준 postId (첫페이지는 번호가 없을 수 있어서 integer)
		if (prevId != null) { 
			// 이전
			direction = "prev";
			standardId = prevId;
			List<Post> postList = postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
			Collections.reverse(postList); // 방금 가져온 리스트 뒤집기
			return postList;
		} else if (nextId != null) { 
			// 다음
			direction = "next";
			standardId = nextId;
		}
		// 첫페이지일 때 (페이징 x) standardId, direction 이 null
		// 다음일 때 (standardId, direction채워져서 넘어감)
		return postDAO.selectPostListByUserId(userId, direction, standardId, POST_MAX_SIZE);
	}
	
	// 페이징 이전 마지막 페이지 여부 (내 글의 정렬이기 때문 userId필요)
	public boolean isPrevLastPage(int prevId, int userId) { // 컨트롤러에 받아온 타입
		
		int maxPostId = postDAO.selectPostIdByUserIdSort(userId, "DESC"); // 다오 입장 필요한 파라미터 보내기
		
		return maxPostId == prevId ? true : false;
	}
	
	// 페이징 다음 마지막 페이지 여부 (내 글의 정렬이기 때문 userId필요)
	public boolean isNextLastPage(int nextId, int userId) {
		int minPostid = postDAO.selectPostIdByUserIdSort(userId, "ASC");
		
		return minPostid == nextId ? true: false;
	}
	
	// 글 상세 갖고오기
	public Post getPostByPostIdUserId(int postId, int userId) {
		return postDAO.selectPostByPostIdUserId(postId, userId);
	}
	
	// 글 삭제
	public int deletePostByPostIdUserId(int postId, int userId) {
		// 기존 글 가져오기
		Post post = getPostByPostIdUserId(postId, userId);
		if(post == null) {
			logger.warn("[글 삭제] post is null. postId:{}, userId:{}",postId,userId);
			return 0;
		}
		// 업로드된 이미지가 있으면 파일 삭제
		if(post.getImagePath() != null) {
			fileManagerService.deleteFile(post.getImagePath());
		}
		
		// DB delete
		return postDAO.deletePostByPostIdUserId(postId, userId);
		
	}
	
}
