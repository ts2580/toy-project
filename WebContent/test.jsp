<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<body>

	<c:set var="str1" value="hello jstl"></c:set>

	<c:set var="str2"value="hello jstl" ></c:set>

	<c:if test="${str1 eq str2}">

	${str1}와 ${str2}는 같습니다.

	</c:if>

	<c:if test="${not str1 eq str2}">

	${str1}와 ${str2}는 같지 않습니다.

	</c:if>

</body>
</html>