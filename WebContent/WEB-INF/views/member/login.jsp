<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
<style type="text/css">
	.title{
		display: block;
		width: 150px;
	}
	.valid_info{
		color:red;
		font-size: 0.5vw;
	}
</style>
</head>
<body>
	
	<h1>로그인</h1>
	<hr>
	<form action="/member/login" method="post">
	
		<c:if test="${not empty param.err}">
			<span class="valid_info">아이디와 비밀번호를 확인해주세요</span>
		</c:if>
		
		
		<span class="title">ID : </span>
		<input type="text" name="userId" id="userId">
		<span class="title">Password : </span>
		<input type="password" name="password" id="password">
		<button>로그인</button>
	</form>
	
</body>
</html>