package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DatabaseUtil;

public class userDAO {
	
	public int login(String userID, String userPassword) {
		String SQL ="SELECT userPassword FROM USER WHERE userID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;	// ResultSet은 특정한 sql문장을 실행한 이후에 그 결과값에 대해 처리해주고자 할 떄 사용하는 클래스
		try {
			conn = DatabaseUtil.getConnection();
			// 연결된 객체는 conn 객체에 담기게 된다.
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			
			// sql문 결과를 rs에 담아준다.(즉, executeQuery 는 db에서 검색해서 조회하는 것이다.)
			rs = pstmt.executeQuery();
			if(rs.next()) {	// rs에 결과가 있으면
				if(rs.getString(1).equals(userPassword)) {
					return 1;	// 로그인 성공
				}
				else {
					return 0;	// 비밀번호 틀림
				}
			}
			return -1;	// 아이디 없음
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 데이터베이스에 한 번 접근하고 나면 자원을 해제해 주는 것이 중요하다.
			try {if(conn != null) conn.close(); } catch (Exception e) {e.printStackTrace();}
			try {if(pstmt != null) pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return -2;	// 데이터베이스 오류
	}
	
	public int join(userDTO user) {
		String SQL ="INSERT INTO USER VALUES (?, ?, ?, ?, false)";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserEmail());
			pstmt.setString(4, user.getUserEmailHash());

			return pstmt.executeUpdate();
			// executeUpdate는 insert, update, delete 등을 처리하는데 실제로 영향을 받은 데이터의 개수를 반환한다. ex)-> isnert 하나하면 1이 반환된다.)
			// sql문 결과를 rs에 담아준다.

		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 데이터베이스에 한 번 접근하고 나면 자원을 해제해 주는 것이 중요하다.
			try {if(conn != null) conn.close(); } catch (Exception e) {e.printStackTrace();}
			try {if(pstmt != null) pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return -1;	// 회원가입 실패
	}
	
	// 특정회원의 이메일 자체를 반환 해주는 함수
	public String getUserEmail(String userID)
	{
		String SQL ="SELECT userEmail FROM USER WHERE userID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtil.getConnection();
			// 연결된 객체는 conn 객체에 담기게 된다.
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {	// 존재하는 사용자의 아이디가 있으면
				return rs.getString(1);
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 데이터베이스에 한 번 접근하고 나면 자원을 해제해 주는 것이 중요하다.
			try {if(conn != null) conn.close(); } catch (Exception e) {e.printStackTrace();}
			try {if(pstmt != null) pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return null;	// 데이터베이스 오류
	}
	
	// 사용자의 아이디값을 입력받아서 그 사용자가 이메일 인증이 되었는지 확인해 준다.
	public boolean getUserEmailChecked(String userID) {
		String SQL ="SELECT userEmailChecked FROM USER WHERE userID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtil.getConnection();
			// 연결된 객체는 conn 객체에 담기게 된다.
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {	// 존재하는 사용자의 아이디가 있으면
				return rs.getBoolean(1);
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 데이터베이스에 한 번 접근하고 나면 자원을 해제해 주는 것이 중요하다.
			try {if(conn != null) conn.close(); } catch (Exception e) {e.printStackTrace();}
			try {if(pstmt != null) pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return false;	// 데이터베이스 오류
	}
	
	// 특정한 사용자의 이메일 인증을 수행해 주는 함수  ( 사용자가 이메일 검증을 통해서 이메일 인증이 안료되도록 해주는 걸 담당 )
	public boolean setUserEmailChecked(String userID) {
		String SQL ="UPDATE USER SET userEmailChecked = true WHERE userID = ?";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseUtil.getConnection();
			// 연결된 객체는 conn 객체에 담기게 된다.
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, userID);
			pstmt.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 데이터베이스에 한 번 접근하고 나면 자원을 해제해 주는 것이 중요하다.
			try {if(conn != null) conn.close(); } catch (Exception e) {e.printStackTrace();}
			try {if(pstmt != null) pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return false;	// 데이터베이스 오류
	}
}
