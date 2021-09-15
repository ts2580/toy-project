package com.kh.toy.member.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.kh.toy.common.exception.PageNotFoundException;
import com.kh.toy.member.model.dto.Member;
import com.kh.toy.member.service.MemberService;

@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private MemberService memberService = new MemberService();
	
    public MemberController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] uriArr = request.getRequestURI().split("/");
		
		switch(uriArr[uriArr.length-1]) {
		case "login-form":
			loginForm(request, response);
			break;
		case "login":
			login(request, response);
			break;
		case "logout":
			logout(request, response);
			break;
		case "join-form":
			joinForm(request, response);
			break;
		case "join":
			join(request, response);
			break;
		case "id-check":
			checkId(request, response);
			break;
		case "join-impl":
			joinImpl(request, response);
			break;
		case "mypage":
			mypage(request, response);
			break;
		default: throw new PageNotFoundException();
		}
	}
	
	private void mypage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		request.getRequestDispatcher("/member/mypage").forward(request, response);
		
	}

	private void join(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	      
		String userId =request.getParameter("userId");
		String password =request.getParameter("password");
		String tell =request.getParameter("tell");
		String email =request.getParameter("email");
		
		Member member = new Member();
		member.setEmail(email);
		member.setTell(tell);
		member.setUserId(userId);
		member.setPassword(password);
		
		String persistToken = UUID.randomUUID().toString();
		// 토큰 만들어서 넘겨주고, 유효할때만 작동. 만료되면 작동 안함. 회원가입정보 중복으로 넘어가서 개짓하는거 방지
		request.getSession().setAttribute("persistUser", member);
		request.getSession().setAttribute("persistToken", persistToken);
		
		
		memberService.authenticateByEmail(member, persistToken);
		
		request.setAttribute("msg", "이메일 발송");
		request.setAttribute("url", "/index");
		request.getRequestDispatcher("/error/result").forward(request, response);
		
	}
	
	private void joinImpl(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		HttpSession session = request.getSession();
		
		Member member = (Member)session.getAttribute("persistUser");
		memberService.insertMember(member);
		
		session.removeAttribute("persistToken");
		session.removeAttribute("persistUser");
		// 다 사용된 세션 날려서 만료시켜.
		
		response.sendRedirect("/member/login-form");
		
	}

	private void checkId(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String userId =  request.getParameter("userId");
		
		Member member = memberService.selectMemberById(userId);
		
		if(member == null) {
			//join-form에서 fetch로 db랑 통신중. 브라우저랑 통신 하는게 아니니까 포워드나 리다이렉트 안씀.
			response.getWriter().print("avilable");
		}else {
			response.getWriter().print("disable");
		}
		
	}

	private void joinForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.getRequestDispatcher("/member/join-form").forward(request, response);
		
	}

	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");
		
		Member member =memberService.memberAuthenticate(userId, password);

		// 2. 사용자가 잘못된 아이디와 비밀번호를 입력한 경우
		//	(서비스단에선 처리안됨. null이 걍 반환되거든. 컨트롤러에서 처리)
		//  경고 하나 띄우고 로그인페이지로
		if(member == null) {
			response.sendRedirect("/member/login-form?err=1");
			/* jsp로 보내는거 아님 request.getRequestDispatcher 아니라고*/
			return;
		}
		
		request.getSession().setAttribute("authentication", member);
		response.sendRedirect("/index");
		
	}
	
	private void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		// 세션 : 사용자 인증정보만 담기는 공간이 아니다.
		// 그러니 인증정보만 날리자.
		
		request.getSession().removeAttribute("authentication");
		response.sendRedirect("/index");
		
	}

	private void loginForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.getRequestDispatcher("/member/login").forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
