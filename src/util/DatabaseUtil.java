package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {

	// getConnection() 함수를 다른 라이브러리에서 쉽게 사용할 수 있도록 static을 넣어 준다.
	// 메서드 앞에 static을 붙이면 인스턴스를 생성하지 않고 호출이 가능한 static 메서드가 된다.
	// Connection은 getConnection()메서드가 반환되는 타입이다. 즉 반환되는 객체의 타입이다.
	public static Connection getConnection() {
		try {
			String dbURL = "jdbc:mysql://localhost:3333/LectureEvaluation";
			String dbID = "root";
			String dbPassword = "013174zz";
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch(Exception e){
			e.printStackTrace();
		}
		// 오류 발생시 null 값 반환
		return null;
	}
}
