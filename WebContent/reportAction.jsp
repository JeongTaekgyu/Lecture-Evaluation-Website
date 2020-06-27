<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="javax.mail.Transport"%>
<%@page import="javax.mail.Message"%>
<%@page import="javax.mail.Address"%>
<%@page import="javax.mail.internet.InternetAddress"%>
<%@page import="javax.mail.internet.MimeMessage"%>
<%@page import="javax.mail.Session"%>
<%@page import="javax.mail.Authenticator"%>
<%@page import="java.util.Properties"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="user.UserDAO"%>
<%@page import="util.SHA256"%>
<%@page import="util.Gmail"%>
<%

	UserDAO userDAO = new UserDAO();
	String userID = null;
	if(session.getAttribute("userID") != null) { // 사용자가 로그인한 상태(세션값이 유효한 상태일 떄)
		userID = (String) session.getAttribute("userID"); // 해당 세션값을 넣어준다.
	}

	if(userID == null) { // 로그인하지 않은 생태
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('로그인을 해주세요.');");
		script.println("location.href = 'userLogin.jsp'");
		script.println("</script>");
		script.close();
		return;
	}
	
	request.setCharacterEncoding("UTF-8");
	String reportTitle = null;
	String reportContent = null;
	if(request.getParameter("reportTitle") != null) { // 신고 제목을 정상적으로 입력받았다면
		reportTitle = (String) request.getParameter("reportTitle");
	}
	if(request.getParameter("reportContent") != null) { // 신고 내용을 정상적으로 입력받았다면
		reportContent = (String) request.getParameter("reportContent");
	}
	if (reportTitle == null || reportContent == null) {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('입력이 안 된 사항이 있습니다.');");
		script.println("history.back();");	// 이전 화면으로
		script.println("</script>");
		script.close();
		return;
	}
	
	// 인증이 안된 사용자이면 사용자에게 보낼 메시지를 기입합니다.
	String host = "http://localhost:8080/Lecture_Evaluation/";
	String from = "taekgyu0602@gmail.com";
	String to = "wjdxorrb93@naver.com";	// 관리자 메일
	String subject = "강의평가 사이트에서 접수된 신고메일입니다.";
	String content = "신고자: " + userID + 
					"<br>제목: " + reportTitle + 
					"<br>내용: " + reportContent;


	// SMTP에 접속하기 위한 정보를 기입합니다.
	Properties p = new Properties();
	p.put("mail.smtp.user", from);
	p.put("mail.smtp.host", "smtp.googlemail.com");
	p.put("mail.smtp.port", "465");
	p.put("mail.smtp.starttls.enable", "true");
	p.put("mail.smtp.auth", "true");
	p.put("mail.smtp.debug", "true");
	p.put("mail.smtp.socketFactory.port", "465");
	p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	p.put("mail.smtp.socketFactory.fallback", "false");
	 
	try{ // 구글 계정으로 gmail 인증을 한다. 관리자의 메일 주소로 사용자한테 이메일인증 메일을 전송한다.
	    Authenticator auth = new Gmail();
	    Session ses = Session.getInstance(p, auth);
	    ses.setDebug(true);	// 디버깅 설정
	    
	    MimeMessage msg = new MimeMessage(ses);
	    msg.setSubject(subject); // 메일 제목
	    Address fromAddr = new InternetAddress(from); // 보내는 메일 정보
	    msg.setFrom(fromAddr);
	    Address toAddr = new InternetAddress(to); // 받는 메일 정보
	    msg.addRecipient(Message.RecipientType.TO, toAddr);
	    msg.setContent(content, "text/html;charset=UTF-8"); // 메일 내용
	    Transport.send(msg);
	} catch(Exception e){
	    e.printStackTrace();
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('오류가 발생했습니다.');");
		script.println("history.back();");
		script.println("</script>");
		script.close();		
	    return;
	}
	
	PrintWriter script = response.getWriter();
	script.println("<script>");
	script.println("alert('정상적으로 신고되었습다.');");
	script.println("history.back();");
	script.println("</script>");
	script.close();		
    return;
%>