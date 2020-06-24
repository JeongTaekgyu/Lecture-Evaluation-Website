package util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class Gmail extends Authenticator{

	@Override
	protected PasswordAuthentication getPasswordAuthentication( ) {
		return new PasswordAuthentication("1234@gmail.com", "1234");
	}
}
