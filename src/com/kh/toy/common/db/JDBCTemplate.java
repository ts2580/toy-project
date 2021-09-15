package com.kh.toy.common.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;


public class JDBCTemplate {
	
	// SingleTon pattern
	// DAO에서 그냥 불러오면 서버에 사람 한명 들어올때마다 인스턴스 하나 생성
	// 모든 사용자가 다 똑같이 사용하는건데 그건 너무 비효율적
	// 하나만 띄우게
	// 클래스의 인스턴스를 하나만 생성되도록 처리하는 디자인패턴이 싱글톤
	
	// 1. 생성자를 private
	// 2. 필드에 static변수 하나 생성
	
	private static JDBCTemplate instance;
	PoolDataSource pds;
	// 3. 인스턴스를 받아서 보내는 메소드 만들기
	//    스태틱은 클래스로 부를 수 있다
	
	public static JDBCTemplate getInstance() {
		if(instance == null) {
			instance = new JDBCTemplate();
		}
		// 맨 첨에는 instance가 null이니까 인스턴스 생성해서 반환
		// 그 담에는 새로 생성할거없이 인스턴스만 반환
		// 스태틱이니 시스템 시작부터 종료까지 올라가있으니 한번만 생성이지.
		return instance;
	}
	
	


	//오라클 드라이버를 JVM에 등록
	private JDBCTemplate() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@db202109131358_high?TNS_ADMIN=C:/CODE/d_front/e_servlet/Wallet_DB202109131358";
			String user = "ADMIN";
			String password = "2whTpalvmf__";
			
			final String CONN_FACTORY_CLASS_NAME="oracle.jdbc.pool.OracleDataSource";
			
			// Get the PoolDataSource for UCP
			pds = PoolDataSourceFactory.getPoolDataSource();

		    // Set the connection factory first before all other properties
		    pds.setConnectionFactoryClassName(CONN_FACTORY_CLASS_NAME);
		    pds.setURL(url);
		    pds.setUser(user);
		    pds.setPassword(password);
		    pds.setConnectionPoolName("JDBC_UCP_POOL");

		    // Default is 0. Set the initial number of connections to be created
		    // when UCP is started.
		    pds.setInitialPoolSize(5);

		    // Default is 0. Set the minimum number of connections
		    // that is maintained by UCP at runtime.
		    pds.setMinPoolSize(5);

		    // Default is Integer.MAX_VALUE (2147483647). Set the maximum number of
		    // connections allowed on the connection pool.
		    pds.setMaxPoolSize(20);

		    // Default is 30secs. Set the frequency in seconds to enforce the timeout
		    // properties. Applies to inactiveConnectionTimeout(int secs),
		    // AbandonedConnectionTimeout(secs)& TimeToLiveConnectionTimeout(int secs).
		    // Range of valid values is 0 to Integer.MAX_VALUE. .
		    pds.setTimeoutCheckInterval(5);

		    // Default is 0. Set the maximum time, in seconds, that a
		    // connection remains available in the connection pool.
		    pds.setInactiveConnectionTimeout(10);
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	// 데이터베이스와 연결
	public Connection getConnection() {
		
		Connection conn = null;
		
		try {
			
			conn = pds.getConnection();
			conn.setAutoCommit(false);
			// 이제 우리가 커밋하고 우리가 롤백함
			// 아래에 커밋과 롤백매소드 만들자
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return conn;
	}
	
	public void commit(Connection conn) {
		
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void rollback(Connection conn) {
		
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	

	
	public void close(Connection conn) {

		try {
			
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close(Statement stmt) {

		try {

			if (stmt != null && !stmt.isClosed()) {
				stmt.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close(ResultSet rset) {

		try {

			if (rset != null && !rset.isClosed()) {
				rset.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// close ㄱㄱㄱ
	public void close(ResultSet rset, Statement stmt, Connection conn) {
		close(conn);
		close(stmt);
		close(rset);
		//위에 만든 메소드 불러오는거니까 걍 갔다써
	}

	public void close(ResultSet rset, Connection conn) {
		close(rset);
		close(conn);
	}

	public void close(Statement stmt, Connection conn) {
		close(stmt);
		close(conn);
	}
	
	
	
	
	
}
