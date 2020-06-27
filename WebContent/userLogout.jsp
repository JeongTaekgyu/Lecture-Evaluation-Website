<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>

<!-- 회원가입에 성공한 사람이 로그인할 떄 로그인 요청을 처리해 주는 Action 페이지 이다. -->
<%
	session.invalidate();	// 클라이언트의 모든 세션정보를 파기시킨다.
%>
<script>
	location.href = 'index.jsp';
</script>