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
			<button type="button" id="postDeleteBtn" class="btn btn-secondary" data-post-id="${post.id}">삭제</button>
			
			<div>
				<a class="btn btn-dark" id="postListBtn" href="/post/post_list_view">목록으로</a>
				<button type="button" id="postUpdateBtn" class="btn btn-primary" data-post-id="${post.id}">수정</button>
			</div>
		</div>
	</div>
</div>
<script>
	$(document).ready(function(){
		// 수정버튼 클릭
		$('#postUpdateBtn').on('click',function(){
			//alert(111);
			// validation
			let subject = $('#subject').val().trim();
			if(subject == ''){
				alert("제목을 입력해주세요.");
				return;
			}
			
			let content = $('#content').val();
			console.log(content);
			
			let file = $('#file').val(); // 파일이 아닌 임시 경로 갖고옴 (C:\fakepath\ddd.jpg)
			console.log(file);
			
			// 파일이 업로드 된 경우 확장자 체크
			if(file != '') {
				let ext = file.split(".").pop().toLowerCase();
				if($.inArray(ext, ['jpg', 'jpeg', 'png', 'gif']) == -1) {
					alert("이미지 파일만 업로드 할 수 있습니다.");
					$('#file').val(""); // 파일을 비운다.
					return;
				}
			}
			
			let postId = $(this).data('post-id');
			
			
			// 폼태그를 자바스크립트에서 만든다.
			let formData = new FormData();
			formData.append("postId",postId); // "key명", validation 변수명
			formData.append("subject",subject);
			formData.append("content",content);
			formData.append("file",$('#file')[0].files[0]);
			
			// AJAX => 서버 통신
			$.ajax({
				//request
				type:"PUT"
				, url:"/post/update"
				, data:formData
				, enctype:"multipart/form-data" // 파일 업로드를 위한 필수 설정
				, processData:false // 파일 업로드를 위한 필수 설정
				, contentType:false // 파일 업로드를 위한 필수 설정
				// 스트링으로 보내는게 아니고 리퀘스트타입에 파일까지 담아 보낸다는 뜻
				
				//response
				,success:function(data){
					if(data.code == 1){
						alert("메모가 수정되었습니다.");
						document.location.reload();
					} else {
						alert(data.errorMessage);
					}
				}
				,error:function(e){
					alert("메모가 수정되는데 실패했습니다.");
				}
			});
			
			
		});// 수정버튼 끝
		
		// 글 삭제
		$('#postDeleteBtn').on('click',function(){
			//alert(1111);
			let postId = $(this).data('post-id');
			//alert(postId);
			
			$.ajax({
				//request
				type:"DELETE"
				,url:"/post/delete"
				,data:{"postId":postId}
			
				//response
				,success:function(data){
					if(data.result == '성공'){
						alert("삭제 되었습니다.");
						location.href="/post/post_list_view";
					} else {
						alert(data.errorMessage);
					}
				}
				,error:function(e){
					alert("메모를 삭제하는데 실패했습니다." + e);
				}
			});
		});
	});//->document end
</script>