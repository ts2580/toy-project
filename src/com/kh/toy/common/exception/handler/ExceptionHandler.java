package com.kh.toy.common.exception.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.toy.common.exception.HandleableException;

@WebServlet("/exception-handler")
public class ExceptionHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ExceptionHandler() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// servlet container는 HandleableException이 발생하면 요청을 exception-handler로 재지정
		// 이때 request의 javax.servlet.error.exception 속성에 발생한 예외를 함께 담아서 준다.
		HandleableException e = (HandleableException)request.getAttribute("javax.servlet.error.exception");
		
		request.setAttribute("msg", e.error.MESSAGE);
		request.setAttribute("url", e.error.URL);
		request.getRequestDispatcher("/WEB-INF/views/error/result.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
