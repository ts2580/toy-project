package com.kh.toy.member.model.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kh.toy.common.db.JDBCTemplate;
import com.kh.toy.common.exception.DataAccessException;
import com.kh.toy.member.model.dto.Member;



public class MemberDao {

	
	private JDBCTemplate template = JDBCTemplate.getInstance();
	
	// DAO(DATA ACCESS OBJECT)
	// DBMS에 접근해 데이터의 조회, 수정, 삼입, 삭제 요청을 보내는 클래스
	// DAO의 메서드는 하나의 메서드상 하나의 쿼리만 처리하도록 작성

	public Member memberAuthenticate(String userId, String password, Connection conn) {
		
		Member member = null;
		
		PreparedStatement pstm = null;
		ResultSet rset = null;
		
		
		String query = "select * from member where user_id = ? and password = ?";
		
		try {
			
			pstm = conn.prepareStatement(query);
			pstm.setString(1, userId);
			pstm.setString(2, password);
			rset = pstm.executeQuery();

			if (rset.next()) {

				member = new Member();

				member.setUserId(rset.getString("user_id"));
				member.setPassword(rset.getString("password"));
				member.setEmail(rset.getString("email"));
				member.setGrade(rset.getString("grade"));
				member.setIsLeave(rset.getInt("is_leave"));
				member.setRegDate(rset.getDate("reg_date"));
				member.setRentableDate(rset.getDate("rentable_date"));
				member.setTell(rset.getString("tell"));
			}

		}catch(SQLException e){
			throw new DataAccessException(e);
		}finally {
			template.close(rset);
			template.close(pstm);
		}

		return member;
	}
	
	public int insertMember(Member member, Connection conn) {
		
		
		
		PreparedStatement pstm = null;
		int res = 0;
		

		
		String query = "insert into member(user_id, password, email, tell) values(?,?,?,?) ";
		
		try {
			pstm = conn.prepareStatement(query);
			pstm.setString(1, member.getUserId());
			pstm.setString(2, member.getPassword());
			pstm.setString(3, member.getEmail());
			pstm.setString(4, member.getTell());
			

			res = pstm.executeUpdate();
			
			template.commit(conn);
			// 성공시 커밋
		} catch(SQLException e){
			throw new DataAccessException(e);
		}finally {
			template.close(pstm);
		}


		return res;
	}

	public int changePW(String userId, String password, Connection conn) throws SQLException {
		
		
		PreparedStatement pstmt = null;
		int res = 0;
		
		
		String query =  "update member set password =? where user_id = ?";
		
		
		try {
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, password);
			pstmt.setString(2, userId);
			
			res = pstmt.executeUpdate();
			
		} finally {

			template.close(pstmt);
		}

		return res;
	}
	
	
	
	public Member selectMemberById(String userId, Connection conn) throws SQLException{
		Member member = null;
	
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		
		String query =  "select * from member where user_id  = ?";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			rset = pstmt.executeQuery();
			
			if (rset.next()) {

				member = new Member();
				member = convertAllColumnToMember(rset);
			}

		} finally {
			template.close(rset);
			template.close(pstmt);
		}
		
		return member;
	}

	


	public int delID(String userId, Connection conn) throws SQLException {
		
	
		PreparedStatement pstmt = null;
		int res = 0;
		
	
		String query = "delete from member where user_id = ?";
		
		try {
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, userId);
			res = pstmt.executeUpdate();
			
		} finally {
			template.close(pstmt);

		}
		
		
		
		return res;

	}



	public List<Member> selectMemberList(Connection conn) throws SQLException{

		List<Member> memberList = new ArrayList<Member>();
		Member member = null; 
		// convertRowToMember에서 받아올꺼임
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		String query = "select * from member";
		// String query = "select user_id, email, tell, grade from member";
		// 이렇게만 뽑아오고싶다.
		// 근데 convertAllColumnToMember에서 다 불러오는데 쿼리에 저것만 있으면 nullpoint 뜸

		//String columns = "user_id, email, tell, grade";
		//String query = "select " + columns + " from member";
		// 여기에 없는 나머지 결과값은 null(문자), 0(숫자)로 뜰것임

		
		try {
			pstmt = conn.prepareStatement(query);
			rset = pstmt.executeQuery();

			while (rset.next()) {

				member = convertAllColumnToMember(rset);
				// convertRowToMember에서 불러온다니까
				// Member member = convertRowToMember(columns.split(","), rset);
				// ','로 쪼갠 columns값을 배열로써 보내
				memberList.add(member);
			}
		} finally {
			template.close(rset);
			template.close(pstmt);
		}

		return memberList;
	}


	private Member convertAllColumnToMember(ResultSet rset) throws SQLException{
		
		Member member = new Member();
		member.setUserId(rset.getString("USER_ID"));
		member.setEmail(rset.getString("EMAIL"));
		member.setGrade(rset.getString("GRADE"));
		member.setIsLeave(rset.getInt("IS_LEAVE"));
		member.setPassword(rset.getString("PASSWORD"));
		member.setRegDate(rset.getDate("REG_DATE"));
		member.setRentableDate(rset.getDate("RENTABLE_DATE"));
		member.setTell(rset.getString("TELL"));
		
		return member;
		
		
	}
	
	/*
	 * private Member convertRowToMember(String[] columns, ResultSet rset) throws
	 * SQLException { Member member = new Member(); for (int i = 0; i <
	 * columns.length; i++) { String column = columns[i].toLowerCase(); column =
	 * column.trim();
	 * 
	 * switch (column) { case "user_id":
	 * member.setUserId(rset.getString("user_id")); break; case "password":
	 * member.setPassword(rset.getString("password")); break; case "email" :
	 * member.setEmail(rset.getString("email")); break; case "grade" :
	 * member.setGrade(rset.getString("grade")); break; case "is_leave" :
	 * member.setIsLeave(rset.getInt("is_leave")); break; case "reg_date" :
	 * member.setRegDate(rset.getDate("reg_date")); break; case "rentable_date" :
	 * member.setRentableDate(rset.getDate("rentable_date")); break; case "tell" :
	 * member.setTell(rset.getString("tell")); break; default : throw new
	 * SQLException("부적절한 컬럼명을 전달했습니다."); //예외처리 } } return member; }
	 */
	




		

}
