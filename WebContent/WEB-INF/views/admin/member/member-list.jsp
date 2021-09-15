<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/head.jsp" %>
</head>
<body>
	<h1>회원목록</h1>
	<table>
		<thead>
			<tr>
				<th>아이디</th>
				<th>휴대폰</th>
				<th>이메일</th>
				<th>탈퇴처리</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${members}" var="member">
				<tr>
					<td><c:out value="${member.userId}"></c:out></td>
					<td><c:out value="${member.tell}"></c:out></td>
					<td><c:out value="${member.email}"></c:out></td>
					<td><a href="/admin/member/leave">탈퇴처리</a></td>
				</tr>
			
			
			</c:forEach>
		</tbody>
	
	</table>

</body>
</html>