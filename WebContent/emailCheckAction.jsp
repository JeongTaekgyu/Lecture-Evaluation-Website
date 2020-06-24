<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="util.SHA256"%>
<%@page import="user.UserDAO"%>
<%
	// 사용자가 이메일 인증을 하면 그에 대한 정보를 체크하는 페이지

	request.setCharacterEncoding("UTF-8");
	String code = request.getParameter("code");
	
	if(request.getParameter("code") != null){
		code = request.getParameter("code");
	}

	UserDAO userDAO = new UserDAO();
	String userID = null;
	if(session.getAttribute("userID") != null) { // 사용자가 로그인 한 상태라면(세션값이 유효한 상태라면)
		userID = (String) session.getAttribute("userID");	// userID 를 초기화 한다.
	}
	if(userID == null) { // 로그인한 상태가 아니라면
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('로그인을 해주세요.');");
		script.println("location.href = 'userLogin.jsp'");
		script.println("</script>");
		script.close();
		return;
	}

	String userEmail = userDAO.getUserEmail(userID); // 현재 사용자의 이메일 주소를 받는다.
	// 현재 사용자가 보낸 코드가 정확히 해당 사용자의 이메일 주소에 해시값을 적용한 데이터와 일치하는지 확인한다.
	boolean rightCode = (new SHA256().getSHA256(userEmail).equals(code)) ? true : false;
	if(rightCode == true) {
		userDAO.setUserEmailChecked(userID); // 사용자의 이메일 인증을 처리해준다. 
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('인증에 성공했습니다.');");
		script.println("location.href = 'index.jsp'");
		script.println("</script>");
		script.close();		
		return;
	} else {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('유효하지 않은 코드입니다.');");
		script.println("location.href = 'index.jsp'");
		script.println("</script>");
		script.close();		
		return;
	}
%>