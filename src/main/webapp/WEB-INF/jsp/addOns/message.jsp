<%
  String message=(String) request.getAttribute("message") ;
  message=message!=null?message:request.getParameter("message");
  String error=(String) request.getAttribute("error") ;
  error=error!=null?error:request.getParameter("error");
  if(message!=null || error!=null){
%>
<div class="card <%=message!=null?"success":"error"%>" id="message">
  <%=message!=null?message:error%>
</div>
<script>
	setTimeout(()=>{
		document.getElementById('message').style.display='none';
	},3000)
</script>
<%
  }
%>