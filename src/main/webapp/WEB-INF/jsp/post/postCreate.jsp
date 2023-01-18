<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="d-flex justify-content-center">
	<div class="w-50"> <!-- 브라우저의 50프로 -->
		<h1>글쓰기</h1>
		<!-- 제목 -->
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요.">
		
		<!-- 내용 -->
		<textarea class="form-control" id="content" placeholder="내용을 입력하세요." rows="15"></textarea>
		
		<!-- 파일 버튼 -->
		<div class="d-flex justify-content-end my-4">
			<input type="file" id="file" accept=".jpg, .png, .jpeg, .gif"> <!-- 허용할 확장자만 선택(편의성위해) -->
		</div>
		
		<!-- 버튼 영역 -->
		<div class="d-flex justify-content-between">
			<button type="button" id="postListBtn" class="btn btn-dark">목록</button>
			
			<div>
				<button type="button" class="btn btn-secondary" id="clearBtn">모두 지우기</button>
				<button type="button" id="postCreateBtn" class="btn btn-info">저장</button>
			</div>
		</div>
	</div>
</div>
<script>
	$(document).ready(function(){
		// 목록 버튼 클릭
		$('#postListBtn').on("click",function(){
			location.href ="/post/post_list_view";
		}); //->목록버튼 끝
		
		// 모두지우기 버튼클릭
		$('#clearBtn').on("click",function(){
			$('#subject').val(""); // 빈칸세팅
			$('#content').val("");
		}); //->모두지우기 끝
		
		// 저장버튼 클릭
		$('#postCreateBtn').on("click",function(){
			let subject = $('#subject').val().trim();
			let content = $('#content').val(); // 공백이 임의로 들어갔을수도 있기떄문에 trim생략
			
			if(subject == ''){
				alert("제목을 입력하세요.");
				return;
			}
			//console.log(content);
			
			let file = $('#file').val(); // C:\fakepath\자료구조.jpg 이런식으로 경로의 String값이 들어있음.(실질적 file아닌 파일명)
			//alert(file);
			
			// 파일이 업로드 된 경우에만 확장자 체크
			if(file != ''){ 
				// alert(file.split(".").pop().toLowerCase()); // 마지막에 있는 배열(확장자)을 뽑아낸다. / toLowerCase: 소문자로 뽑아내기
				let ext = file.split(".").pop().toLowerCase();
				if($.inArray(ext, ['jpg', 'jpeg', 'png', 'gif']) == -1) {
					alert("이미지 파일만 업로드 할 수 있습니다.");
					$('#file').val(""); // 파일을 비운다.
					return;
				}
			}
			
			// server - AJAX
			// 이미지를 업로드 할 때는 form태그가 있어야한다. (자바스크립트)
			let formData = new FormData();
			
			// append로 넣는 값은 폼태그의 name으로 넣는 것과 같다(request parameter)
			//				name     , validation 값
			formData.append("subject", subject);
			formData.append("content", content);
			formData.append("file", $('#file')[0].files[0]);
			
			// ajax 통신으로 formData에 있는 데이터 전송
			$.ajax({
				//request
				type:"POST"
				, url:"/post/create"
				, data:formData // 폼객체를 통째로
				, enctype:"multipart/form-data" // 파일 업로드를 위한 필수 설정
				, processData : false // 파일 업로드를 위한 필수 설정		
				, contentType : false // 파일 업로드를 위한 필수 설정
				
				//response
				,success:function(data){
					if(data.code == 1){
						// 성공
						alert("메모가 저장되었습니다.");
						location.href="/post/post_list_view" //get방식 동일, view화면 이동
					} else {
						// 실패(로직상 에러_ 아래의 에러와 내용이 다르게 하는게 좋음)
						alert(data.errorMessage);
					}
				}
				,error:function(e){
					// ajax 완전 실패
					alert("메모 저장에 실패했습니다.");
				}
			});//-> ajax end
		});//->저장버튼 끝
		
	});//->document end
</script>