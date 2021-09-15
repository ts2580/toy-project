package com.kh.toy.board.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.kh.toy.board.model.dto.Board;
import com.kh.toy.common.db.JDBCTemplate;
import com.kh.toy.common.exception.DataAccessException;
import com.kh.toy.common.file.FileDTO;

public class BoardDao { //오타 없는것같은데 정보를 

   JDBCTemplate template = JDBCTemplate.getInstance();
   
   public void insertBoard(Connection conn, Board board) {
      String sql = "insert into board(bd_idx,user_id, title,content) values(sc_board_idx.nextval,?,?,?)";
      
      PreparedStatement pstm = null;
      
      try {
         
         pstm = conn.prepareStatement(sql);
         pstm.setString(1, board.getUserId());
         pstm.setString(2, board.getTitle());
         pstm.setString(3, board.getContent());
         pstm.executeUpdate();
         
      } catch (SQLException e) {
         throw new DataAccessException(e);
      }finally {
         template.close(pstm);
      }
   }

   public void insertFile(Connection conn, FileDTO fileDTO) {
      String sql = "insert into file_info(fl_idx,type_idx, origin_file_name,rename_file_name,save_path) values(sc_file_idx.nextval,sc_board_idx.currval,?,?,?)";
      
      PreparedStatement pstm = null;
      
      try {
         pstm = conn.prepareStatement(sql);
         pstm.setString(1, fileDTO.getOriginFileName());
         pstm.setString(2, fileDTO.getRenameFileName());
         pstm.setString(3, fileDTO.getSavePath());
         
         pstm.executeUpdate();
      } catch (SQLException e) {
         throw new DataAccessException(e);
      }finally {
         template.close(pstm);
      }
	}

	public Board selectBoardDetail(Connection conn, String bdIdx) {

		String sql = "select bd_idx,user_id,reg_date,title,content from board where bd_idx = ? and is_del = 0";

		PreparedStatement pstm = null;
		ResultSet rset = null;
		Board board = null;

		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, bdIdx);
			rset = pstm.executeQuery();

			while (rset.next()) {
				board = new Board();
				board.setBdIdx(rset.getString("bd_idx"));
				board.setContent(rset.getString("content"));
				board.setTitle(rset.getString("title"));
				board.setRegDate(rset.getDate("reg_date"));
				board.setUserId(rset.getString("user_id"));

			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			template.close(rset);
			template.close(pstm);
		}

		return board;

	}

	public List<FileDTO> selectFileDTOs(Connection conn, String bdIdx) {

		String sql = "select fl_idx,type_idx,origin_file_name,rename_file_name,save_path,reg_date from file_info where type_idx = ? and is_del = 0";

		PreparedStatement pstm = null;
		ResultSet rset = null;
		List<FileDTO> files = new ArrayList<FileDTO>();

		try {
			pstm = conn.prepareStatement(sql);
			pstm.setString(1, bdIdx);
			rset = pstm.executeQuery();

			while (rset.next()) {
				FileDTO file = new FileDTO();
				file.setFileIdx(rset.getString("fl_idx"));
				file.setTypeIdx(rset.getString("type_idx"));
				file.setOriginFileName(rset.getString("origin_file_name"));
				file.setRenameFileName(rset.getString("rename_file_name"));
				file.setSavePath(rset.getString("save_path"));
				file.setRegDate(rset.getDate("reg_date"));

				files.add(file);
			}
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			template.close(rset);
			template.close(pstm);
		}

		return files;

	}

}