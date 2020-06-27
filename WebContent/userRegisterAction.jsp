<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="user.UserDTO"%>
<%@ page import="user.UserDAO"%>
<%@ page import="util.SHA256"%>
<%@ page import="java.io.PrintWriter"%>
<%
	request.setCharacterEncoding("UTF-8");

	// 현재 로그인이 된 상태라면 회원가입을 하지 못하도록 막아줘야한다.
	String userID = null;
	if(session.getAttribute("userID") != null) { // 유저 session값이 존재한다면
		userID = (String) session.getAttribute("userID"); 
	}
	if(userID != null) { // 로그인을 한 상태라면
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('로그인이 된 상태이기 때문에 회원가입을 할 수 없습니다.(userRegisterAction)');");
		script.println("location.href = 'index.jsp'");
		script.println("</script>");
		script.close();	
	}
	String userPassword = null;
	String userEmail = null;

	
	if(request.getParameter("userID") != null) {
		userID = (String) request.getParameter("userID");
	}
	if(request.getParameter("userPassword") != null) {
		userPassword = (String) request.getParameter("userPassword");
	}
	if(request.getParameter("userEmail") != null) {
		userEmail = (String) request.getParameter("userEmail");
	}

	if (userID == null || userPassword == null || userEmail == null) {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('입력이 안 된 사항이 있습니다.');");
		script.println("history.back();");
		script.println("</script>");
		script.close();
	} else {
		UserDAO userDAO = new UserDAO();
		int result = userDAO.join(new UserDTO(userID, userPassword, userEmail, SHA256.getSHA256(userEmail), false));

		if (result == -1) {	// 성공적으로 회원가입이 안된거라면
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('이미 존재하는 아이디입니다.')");
			script.println("history.back();");
			script.println("</script>");
			script.close();
		} else { // 회원가입 성공
			session.setAttribute("userID", userID);	// 세션값으로 userID를 넣어서 서버에서 관리할 수 있도록 해준다.
			PrintWriter script = response.getWriter();

			script.println("<script>");
			script.println("location.href = 'emailSendAction.jsp';");
			script.println("</script>");
			script.close();
		}
	}
%>