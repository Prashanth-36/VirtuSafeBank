<%

	int totalPages = (int)request.getAttribute("totalPages");
	if (totalPages > 0) {
%>
	<div class="pages" id="pages">
		<div id="previousPage" onclick="previousPage()"><img
			src="<%=request.getContextPath()%>/static/images/left.png" alt="previous"
			style="width: 1rem" /></div>
			
		<% for (int i = 1; i <= totalPages; i++) { %>
			<div class="page activePage"><%=i%></div>
		<% } %>
		
		<div id="nextPage" onclick="nextPage()"><img
			src="<%=request.getContextPath()%>/static/images/right.png" alt="next"
			style="width: 1rem" /></div>
	</div>
	
	<script src="<%=request.getContextPath()%>/script/page.js"></script>
<%  } %>