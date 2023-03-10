<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="d-flex align-items-center justify-content-between w-100">
	<%-- logo --%>
	<div>
		<h1 class="font-weight-bold pl-3">MEMO 게시판</h1>
	</div>
	
	<%-- 로그인 정보: 로그인이 되었을 때만 노출 --%>
	<c:if test="${not empty userId}">
		<div class="mr-4">
			<span>${userName}님 안녕하세요</span>
			<a href="/user/sign_out" class="ml-3 font-weight-bold">로그아웃</a>
		</div>
	</c:if>
</div>