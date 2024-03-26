<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error</title>
</head>
<body>
	<h1>Exception!</h1>
	<%= exception.getMessage() %>
  <% 
    StackTraceElement element[]=exception.getStackTrace();
    for(StackTraceElement line:element){
      %>
       <%=line.toString()+"<br>"%> 
       <%
    }
  %>
</body>
</html>

    <error-page>
        <exception-type>java.lang.Throwable</exception-type>
        <location>/WEB-INF/jsp/error.jsp</location>
    </error-page>