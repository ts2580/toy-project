package com.kh.toy.board.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.kh.toy.board.model.dto.Board;
import com.kh.toy.board.model.service.BoardService;
import com.kh.toy.common.exception.PageNotFoundException;
import com.kh.toy.common.file.FileDTO;
import com.kh.toy.common.file.FileUtil;
import com.kh.toy.common.file.MultiPartParams;
import com.kh.toy.member.model.dto.Member;

@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BoardService boardService = new BoardService();

	public BoardController() {
		super();
	}

	// 1. 웹어플리케이션에서 파일 업로드 경로는 프로젝트 외부일것!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// - 서버는 크으게 개발서버와 운영서버로 나뉘는데
	// - 운영서버(프로젝트)에 파일 올라가버리기!
	// - 서버 업데이트하면 개발서버에서 업뎃된 서버가 새로 올라가는데
	// - 기존 서버에있던 파일 다 사라짐.
	// - 파일서버 따로 파던가, 그것도 안되면 다른 디렉토리 어딘가에다가.
	// - 어머니 집에 파일서버 하나 놔드려야겠어요
	// 2. 파일 업로드 시 일자별로 새로운 폴더를 생성
	// 3. 파일 이름은 중복되지 않도록 유니크한 이름으로.
	// 4. 파일 다운로드시에는 사용자가 업로드 했던 파일명으로 다운로드.

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String[] uriArr = request.getRequestURI().split("/");

		switch (uriArr[uriArr.length - 1]) {
		case "board-form":
			boardForm(request, response);
			break;
		case "upload":
			upload(request, response);
			break;
		case "board-detail":
			boardDetail(request, response);
		break;
		default:
			throw new PageNotFoundException();
		}
	}

	private void boardDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// 게시글 상세 페이지, 해당 게시글의 bdIdx를 요청 파라미터에서 받아온다.
		String bdIdx = request.getParameter("bdIdx");
		
		System.out.println(bdIdx);
		
		// boardService에서 게시글 상세 페이지에 뿌려주기 위한 데이터(게시글 정보, 파일정보)를 받아온다
		Map<String, Object> datas = boardService.selectBoardDetail(bdIdx);
		
		System.out.println(datas); // 요기용 테이블엔 잘 올라가는데 
		
		request.setAttribute("datas", datas);
		request.getRequestDispatcher("/board/board-detail").forward(request, response);
	}

	private void upload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		FileUtil util = new FileUtil();
		MultiPartParams multiPart = util.fileUpload(request);
		Member member = (Member) request.getSession().getAttribute("authentication");

		// 게시글의 요청 파라미터
		// title, content

		Board board = new Board();
		board.setUserId(member.getUserId());
		board.setTitle(multiPart.getParameter("title"));
		board.setContent(multiPart.getParameter("content"));

		// FileDTO들
		// files
		List<FileDTO> files = multiPart.getFilesInfo();
		boardService.insertBoard(board, files);

		response.sendRedirect("/");
	}

	private void boardForm(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
			
		request.getRequestDispatcher("/board/board-form").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
			
		doGet(request, response);
	}

}
