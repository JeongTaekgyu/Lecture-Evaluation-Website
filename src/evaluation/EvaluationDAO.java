package evaluation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
			
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, evaluationDTO.getUserID());
			pstmt.setString(2, evaluationDTO.getLectureName());
			pstmt.setString(3, evaluationDTO.getProfessorName());
			pstmt.setInt(4, evaluationDTO.getLectureYear());
			pstmt.setString(5, evaluationDTO.getSemesterDivide());
			pstmt.setString(6, evaluationDTO.getLectureDivide());
			pstmt.setString(7, evaluationDTO.getEvaluationTitle());
			pstmt.setString(8, evaluationDTO.getEvaluationContent());
			pstmt.setString(9, evaluationDTO.getTotalScore());
			pstmt.setString(10, evaluationDTO.getCreditScore());
			pstmt.setString(11, evaluationDTO.getComfortableScore());
			pstmt.setString(12, evaluationDTO.getLectureScore());

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
}
