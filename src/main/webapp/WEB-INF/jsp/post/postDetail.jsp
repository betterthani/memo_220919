<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="d-flex justify-content-center">
	<div class="w-50"> <!-- 브라우저의 50프로 -->
		<h1>글 상세/수정</h1>
		<!-- 제목 -->
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요." value="${post.subject}">
		
		<!-- 내용 -->
		<textarea class="form-control" id="content" placeholder="내용을 입력하세요." rows="15">${post.content}</textarea>
		
		<%--이미지가 있을 떄만 이미지 영역 추가 --%>
		<c:if test="${not empty post.imagePath}">
		<div class="mt-3">
			<img alt="업로드 이미지" src="${post.imagePath}" width="300">
		</div>
		</c:if>
		
		<!-- 파일 버튼 -->
		<div class="d-flex justify-content-end my-4">
			<input type="file" id="file" accept=".jpg, .png, .jpeg, .gif"> <!-- 허용할 확장자만 선택(편의성위해) -->
		</div>
		
		<!-- 버튼 영역 -->
		<div class="d-flex justify-content-between">
			<button type="button" id="postDeleteBtn" class="btn btn-secondary">삭제</button>
			
			<div>
				<a class="btn btn-dark" id="postListBtn" href="/post/post_list_view">목록으로</a>
				<button type="button" id="postUpdateBtn" class="btn btn-primary">수정</button>
			</div>
		</div>
	</div>
</div>