<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="d-flex justify-content-center">
	<div class="sign-up-box">
		<h1 class="mb-4">회원가입</h1>
		<form id="signUpForm" method="post" action="/user/sign_up">
			<table class="sign-up-table table table-bordered">
				<tr>
					<th>* 아이디(4자 이상)<br></th>
					<td>
						<%-- 인풋박스 옆에 중복확인을 붙이기 위해 div를 하나 더 만들고 d-flex --%>
						<div class="d-flex">
							<input type="text" id="loginId" name="loginId"
								class="form-control" placeholder="아이디를 입력하세요.">
							<button type="button" id="loginIdCheckBtn" class="btn btn-success">중복확인</button>
							<br>
						</div> <%-- 아이디 체크 결과 --%> <%-- d-none 클래스: display none (보이지 않게) --%>
						<div id="idCheckLength" class="small text-danger d-none">ID를
							4자 이상 입력해주세요.</div>
						<div id="idCheckDuplicated" class="small text-danger d-none">이미
							사용중인 ID입니다.</div>
						<div id="idCheckOk" class="small text-success d-none">사용 가능한
							ID 입니다.</div>
					</td>
				</tr>
				<tr>
					<th>* 비밀번호</th>
					<td><input type="password" id="password" name="password"
						class="form-control" placeholder="비밀번호를 입력하세요."></td>
				</tr>
				<tr>
					<th>* 비밀번호 확인</th>
					<td><input type="password" id="confirmPassword"
						class="form-control" placeholder="비밀번호를 입력하세요."></td>
				</tr>
				<tr>
					<th>* 이름</th>
					<td><input type="text" id="name" name="name"
						class="form-control" placeholder="이름을 입력하세요."></td>
				</tr>
				<tr>
					<th>* 이메일</th>
					<td><input type="text" id="email" name="email"
						class="form-control" placeholder="이메일 주소를 입력하세요."></td>
				</tr>
			</table>
			<br>

			<button type="submit" id="signUpBtn"
				class="btn btn-primary float-right">회원가입</button>
		</form>
	</div>
</div>
<script>
	$(document).ready(function() {
		// 중복확인 버튼 클릭
		$('#loginIdCheckBtn').on("click", function() {
			// 초기화
			$('#idCheckLength').addClass("d-none"); // 숨김
			$('#idCheckDuplicated').addClass("d-none"); // 숨김
			$('#idCheckOk').addClass("d-none"); // 숨김

			let loginId = $('input[name=loginId]').val().trim();

			if (loginId.length < 4) {
				/*
				idCheckLength
				idCheckDuplicated
				idCheckOk
				 */
				$('#idCheckLength').removeClass("d-none"); // 경고문구 노출
				return;
			}

			// ajax 통신 - 중복확인
			$.ajax({
				// request
				// type: 생략시 자동 get방식됨
				url : "/user/is_duplicated_id",
				data : {
					"loginId" : loginId
				}

				// response
				,
				success : function(data) {
					if (data.code == 1) {
						// 성공
						if (data.result) {
							// 중복일때
							$('#idCheckDuplicated').removeClass("d-none");
						} else {
							// 사용 가능
							$('#idCheckOk').removeClass("d-none");
						}

					} else {
						// 실패
						alert(data.errorMessage);
					}
				},
				error : function(e) {
					alert("중복확인에 실패했습니다.");
				}
			});

		}); //->중복확인 끝

		// 회원가입(form태그 ajax동시 사용시)
		$('#signUpForm').on('submit', function(e) {
			e.preventDefault(); // 서브밋 기능 중단

			// validation
			let loginId = $('input[name=loginId]').val().trim();
			let password = $('#password').val().trim();
			let recheckPassword = $('#recheckPassword').val().trim();
			let name = $('#name').val().trim();
			let email = $('#email').val().trim();

			if (loginId == '') {
				alert("아이디를 입력해주세요.");
				return false; //submit일 경우 return false; (서버에 안 가고 빠져나오게끔)
			}

			if (password == '' || recheckPassword == '') {
				alert("비밀번호를 입력해주세요.");
				return false;
			}

			if (password != recheckPassword) {
				alert("비밀번호가 일치하지 않습니다.");
				return false;
			}

			if (name == '') {
				alert("이름을 입력해주세요.");
				return false;
			}

			if (email == '') {
				alert("이메일을 입력해주세요.");
				return false;
			}

			// 아이디 중복확인 완료 됐는지 확인 => idCheckOk d-none을 갖고있으면 확인하라는 얼럿 띄워야함
			if ($('#idCheckOk').hasClass('d-none')) {
				alert("아이디 중복확인을 다시 해주세요.");
				return false;
			}

			// 서버로 보내는 방법
			// 1) submit
			//우리가선택한 첫번째 폼태그의 submit함수 (화면이동이 form태그 동작)
			// $(this)[0].submit(); // 화면이 넘어간다.(이유: form태그)

			// 2) ajax
			// this = 지금 동작된 폼태그
			let url = $(this).attr("action"); //form 태그에 action을 갖고온다.
			// 파라미터를 한번에 모아서 보내기(직렬화)
			let params = $(this).serialize(); // form 태그에 있는 name으로 파라미터들 구성
			console.log(params);

			//ajax호출
			//           데이터 대용으로 param이 들어감 (구글링 : 제이쿼리 포스트 함수)
			$.post(url, params) // request
			.done(function(data) {

				//response
				if (data.code == 1) {
					// 성공
					alert("가입을 환영합니다! 로그인 해주세요.");
					location.href = "/user/sign_in_view";
				} else {
					// 실패
					alert(data.errorMessage);
				}
			});

		});//->회원가입 끝
	});
</script>