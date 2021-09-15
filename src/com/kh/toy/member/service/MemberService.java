package com.kh.toy.member.service;

import java.sql.Connection;
import java.util.List;

import com.kh.toy.common.db.JDBCTemplate;
import com.kh.toy.common.http.HttpConnector;
import com.kh.toy.common.http.RequestParams;
import com.kh.toy.common.mail.Mailsender;
import com.kh.toy.member.model.dao.MemberDao;
import com.kh.toy.member.model.dto.Member;

public class MemberService {

	// Service
	// 어플리케이션의 비즈니스 로직을 작성
	// 사용자의 요청을 컨트롤러부터 위임받아 해당 요청을 처리하기 위해 필요한 핵심적인 작업을 진행
	// 작업을 수행하기 위해 데이터베이스에 저장된 데이터가 필요하면 Dao에게 요청
	// 비즈니스로직을 Service가 담당하기에 Transection관리를 Service가 담당
	// 즉, commit과 rollback을 Service가 담당함
	
	// Connection객체 생성, close처리
	// commit, rollback
	// SQLException에 대한 예외처리(rollback);
	private MemberDao memberDao = new MemberDao();
	private JDBCTemplate template = JDBCTemplate.getInstance();
	
	
		
	public Member memberAuthenticate(String userId, String password) {
		Connection conn = template.getConnection();
		Member member = null;
		
		try {
			member = memberDao.memberAuthenticate(userId, password,conn);
		}finally {
			template.close(conn);
		}
		return member;
	}

	public Member selectMemberById(String userId) {
		Connection conn = template.getConnection();
		Member member = null;
		
		// catch (Exception e) {e.printStackTrace(); 필요한가? 하는것도 없는게
		// dao에서 잡아서 uncheck로 던져
		
		try {
			member =  memberDao.selectMemberById(userId, conn);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			template.close(conn);
		}
		return member;
		
	}

	public List<Member> selectMemberList() {
		Connection conn = template.getConnection();
		List<Member> memberList = null;
		try {
			memberList =  memberDao.selectMemberList(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			template.close(conn);
		}
		return memberList;
	}

	public int insertMember(Member member) {
		Connection conn = template.getConnection();
		int temNum = 0;
		try {
			temNum = memberDao.insertMember(member, conn);
			//회원가입에 성공하면 그 회원의 정보를 받아와서(아이디로) 저장
			//Member member2 =  memberDao.selectMemberById(member.getUserId(), conn);
			//template.commit(conn);
			//다 성공시 커밋
		} catch (Exception e) {
			//template.rollback(conn);
			//실패시 롤백
			template.rollback(conn);
			throw e;
		}finally {
			template.close(conn);
		}
		return  temNum;
	}
	

	

	public int changePW(String userId, String password) {
		Connection conn = template.getConnection();
		int a = 0;
		
		try {
			a = memberDao.changePW(userId, password, conn);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			template.close(conn);
		}
		
		return a;
	}


	public int delID(String userId) {
		Connection conn = template.getConnection();
		int a = 0;
		
		try {
			a = memberDao.delID(userId, conn);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			template.close(conn);
		}
		
		return a;
	}

	public void authenticateByEmail(Member member, String persistToken) {
		
		Mailsender mailSender = new Mailsender();
		HttpConnector conn = new HttpConnector();
		
		String queryString = conn.urlEncodingForm(RequestParams.builder().param("mailTemplate", "join-auth-mail").param("userId", member.getUserId()).param("persistToken", persistToken).build());
		
		String response = conn.get("http://localhost:9090/mail?" + queryString);
		mailSender.sendEmail(member.getEmail(), "회원가입 축하", response);
	
	}

}
