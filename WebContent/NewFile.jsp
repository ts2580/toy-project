<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html>

<head>

<%@ include file="/WEB-INF/views/include/head.jsp"%>

<link href="../resources/css/mainPage.css" rel="stylesheet"
	type="text/css">

</head>

<body>

for(let i = paging.firstPagerNum; i < paging.lastPagerNum + 1; i++){
		let pageNumber = document.createElement('td');
		pageNumber.classList.add('page-' + i);
		pageNumber.append(i);
		document.querySelector('.num').before(pageNumber);
		
		document.querySelector('.page-' + i).addEventListener('click',() =>{
			sessionStorage.setItem('sessionPageNumber', i );
			location.href="/mainPage/clickedPage";
		});
		
	}


</body>

</html>
