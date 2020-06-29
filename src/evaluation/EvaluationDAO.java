package evaluation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import util.DatabaseUtil;

public class EvaluationDAO {

	// 강의평가 정보를 기록해주는 글쓰기 함수
	public int write(EvaluationDTO evaluationDTO) {
		String SQL ="INSERT INTO EVALUATION VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
		// evaluationID는 auto_increment 를 적용했기 때문에 NULL 값을 넣으면 차례대로 1씩 증가하면서 번호가 기입된다. 마지막은 likecount
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;	// ResultSet은 특정한 sql문장을 실행한 이후에 그 결과값에 대해 처리해주고자 할 떄 사용하는 클래스
		try {
			conn = DatabaseUtil.getConnection();
			
			pstmt = conn.prepareStatement(SQL);		// 크로스 사이트 스크립트 공격(웹페이지에 악성 스크립트를 삽입해 공격)에 방어하기 위해 특정 스크립트를 치환해준다.
			pstmt.setString(1, evaluationDTO.getUserID().replace("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(2, evaluationDTO.getLectureName().replace("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(3, evaluationDTO.getProfessorName().replace("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setInt(4, evaluationDTO.getLectureYear());	// 숫자이기 때문에 굳이 치환안한다.
			pstmt.setString(5, evaluationDTO.getSemesterDivide().replace("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(6, evaluationDTO.getLectureDivide().replace("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(7, evaluationDTO.getEvaluationTitle().replace("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(8, evaluationDTO.getEvaluationContent().replace("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(9, evaluationDTO.getTotalScore().replace("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(10, evaluationDTO.getCreditScore().replace("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(11, evaluationDTO.getComfortableScore().replace("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
			pstmt.setString(12, evaluationDTO.getLectureScore().replace("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));

			// sql문 결과를 바로 반환한다.(insert 한번하면 1이 반환)
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 데이터베이스에 한 번 접근하고 나면 자원을 해제해 주는 것이 중요하다.
			try {if(conn != null) conn.close(); } catch (Exception e) {e.printStackTrace();}
			try {if(pstmt != null) pstmt.close(); } catch (Exception e) {e.printStackTrace();}
			try {if(rs != null) rs.close(); } catch (Exception e) {e.printStackTrace();}
		}
		return -1;	// 데이터베이스 오류
	}
	
	public ArrayList<EvaluationDTO> getList(String lectureDivide, String searchType, String search, int pageNumber) {
		if(lectureDivide.equals("전체")) { // 전체는 공백으로 치환해서 항상 포함하도록 만든다.
			lectureDivide = "";
		}
		ArrayList<EvaluationDTO> evaluationList = null; // 중간에 오류가 생기면 null 값의 리스트를 아래에서 반환한다.
		String SQL = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if(searchType.equals("최신순")) {
				SQL = "SELECT * FROM EVALUATION WHERE lectureDivide LIKE ? AND CONCAT(lectureName, professorName, evaluationTitle, evaluationContent) LIKE ? ORDER BY evaluationID DESC LIMIT " + pageNumber * 5 + ", " + pageNumber * 5 + 6;
			} else if(searchType.equals("추천순")) {
				SQL = "SELECT * FROM EVALUATION WHERE lectureDivide LIKE ? AND CONCAT(lectureName, professorName, evaluationTitle, evaluationContent) LIKE ? ORDER BY likeCount DESC LIMIT " + pageNumber * 5 + ", " + pageNumber * 5 + 6;
			}
			conn = DatabaseUtil.getConnection();
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, "%" + lectureDivide + "%"); // lectureDivide는 전체,전공,교양,기타가 있었는데 전체는 공백으로 , 전공,교양,기타는 동일한 글자만 출력이된다. 
			pstmt.setString(2, "%" + search + "%");	// lectureName, professorName, evaluationTitle, evaluationContent을 다 포함한 문자열에 사용자가 검색한 내용이 포함이 되어있는지 물어본다.
			rs = pstmt.executeQuery();
			evaluationList = new ArrayList<EvaluationDTO>();
			while(rs.next()) {
				// 특정한 결과가 나올 때 마다 그 결과를 초기화 해준다.
				EvaluationDTO evaluation = new EvaluationDTO(
					rs.getInt(1),
					rs.getString(2),
					rs.getString(3),
					rs.getString(4),
					rs.getInt(5),
					rs.getString(6),
					rs.getString(7),
					rs.getString(8),
					rs.getString(9),
					rs.getString(10),
					rs.getString(11),
					rs.getString(12),
					rs.getString(13),
					rs.getInt(14)
				);
				evaluationList.add(evaluation); // 모든 강의평가 데이터를 evaluationList에 담는다
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return evaluationList; // 성공적으로 검색이 이루어져서 특정한 게시글을 확인했다면 리스트에 담긴 결과를 반환
	}
}
