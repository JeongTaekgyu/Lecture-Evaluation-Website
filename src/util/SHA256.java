package util;

import java.security.MessageDigest;

public class SHA256 {
	
	public static String getSHA256(String input) {
		StringBuffer result = new StringBuffer();
		try {
			// 사용자가 실제로 입력한 값을 SHA-256 알고리즘으로 적용한다.
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] salt = "Hello! This is Salt".getBytes();
			digest.reset();
			digest.update(salt); // 해시 데이터를 생성할 때 악의적인 목적을 가진 공격자가 원래의 데이터를 파악하기 더욱 어렵게 만들어 줄 수 있다.
			byte[] chars = digest.digest(input.getBytes("UTF-8"));
			for(int i = 0; i < chars.length; i++) {
				String hex = Integer.toHexString(0xff & chars[i]);
				if(hex.length() == 1) result.append("0"); // 1자리 수 인경우에는 0을 붙여서 2자리의 16진수로 만들어준다.
				result.append(hex);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}
