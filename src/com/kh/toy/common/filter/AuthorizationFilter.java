package com.kh.toy.common.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.kh.toy.common.code.ErrorCode;
import com.kh.toy.common.code.MemberGrade;
import com.kh.toy.common.exception.HandleableException;
import com.kh.toy.member.model.dto.Member;

public class AuthorizationFilter implements Filter {

	public AuthorizationFilter() {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpRespopnse = (HttpServletResponse) response;

		String[] uriArr = httpRequest.getRequestURI().split("/");

		// /member/join => [, member, join]

		if (uriArr.length != 0) {

			switch (uriArr[1]) {
			case "member":
				memberAuthorize(httpRequest, httpRespopnse, uriArr);
				break;
			case "admin":
				adminAuthorize(httpRequest, httpRespopnse, uriArr);
				break;
			case "board":
				boardAuthorize(httpRequest, httpRespopnse, uriArr);
				break;
			case "upload":
				boardAuthorize(httpRequest, httpRespopnse, uriArr);
				break;
			}
		}

		chain.doFilter(request, response);

	}

	private void boardAuthorize(HttpServletRequest httpRequest, HttpServletResponse httpRespopnse, String[] uriArr) throws IOException, ServletException {
		
		HttpSession session = httpRequest.getSession();
		Member member = (Member)session.getAttribute("authentication");
		
		switch (uriArr[2]) {
		case "board-form":
			if(member == null) {
				throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE_ERROR);
			}
			break;
		case "upload":
			if(member == null) {
				throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE_ERROR);
			}
			break;
		default:
			break;
		}
		
	}

	private void adminAuthorize(HttpServletRequest httpRequest, HttpServletResponse httpRespopnse, String[] uriArr) throws IOException, ServletException {
		
		HttpSession session = httpRequest.getSession();
		Member member = (Member)session.getAttribute("authentication");
		
		// 비회원, 사용자 회원인지 판단
		if (member == null || MemberGrade.valueOf(member.getGrade()).ROLE.equals("user")) {
			throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE_ERROR);
		}
		
		// 슈퍼관리자니? 모든 어드민 페이지에 접근 가능
		if (MemberGrade.valueOf(member.getGrade()).DESC.equals("super")) {
			return;
		}
		
		switch (uriArr[2]) {
		case "member":
			if(!MemberGrade.valueOf(member.getGrade()).DESC.equals("member")) {
				throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE_ERROR);
			}
			break;
		case "board":
			if(!MemberGrade.valueOf(member.getGrade()).DESC.equals("board")) {
				throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE_ERROR);
			}
			break;
		default:
			break;
		}
		
	}

	private void memberAuthorize(HttpServletRequest httpRequest, HttpServletResponse httpRespopnse, String[] uriArr) throws IOException, ServletException {
		
		HttpSession session = httpRequest.getSession();
		
		switch (uriArr[2]) {
		case "join-impl":

			String serverToken = (String) session.getAttribute("persistToken");
			String clientToken = httpRequest.getParameter("persistToken");

			if (serverToken == null || !serverToken.equals(clientToken)) {
				throw new HandleableException(ErrorCode.AYTENTICATION_FAILED_ERROR);
			}
			break;
		case "mypage":
			
			if (session.getAttribute("authentication") == null) {
				throw new HandleableException(ErrorCode.UNAUTHORIZED_PAGE_ERROR);
			}
			break;
		default:
			break;
		}

	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
