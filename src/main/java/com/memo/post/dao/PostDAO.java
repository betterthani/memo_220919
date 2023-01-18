package com.memo.post.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.memo.post.model.Post;

@Repository
public interface PostDAO {
	
	// test
	public List<Map<String,Object>> selectPostListTest();
	
	// 글쓰기
	public int insertPost(
			@Param("userId") int userId,
			@Param("userLoginId") String userLoginId,
			@Param("subject") String subject,
			@Param("content") String content,
			@Param("imagePath") String imagePath);
	
	public List<Post> selectPostListByuserId(int userId);

	
}
