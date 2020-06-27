package util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class Gmail extends Authenticator{

	@Override
	protected PasswordAuthentication getPasswordAuthentication( ) {
		return new PasswordAuthentication("실제이메일 입력해야 된다@gmail.com", "실제 패스워드");
	}
}
