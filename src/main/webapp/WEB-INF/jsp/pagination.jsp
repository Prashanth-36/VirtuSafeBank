<%
	Integer totalPages = (Integer)request.getAttribute("totalPages");
    Integer pageNo=(Integer)request.getAttribute("page");
	if (totalPages!=null && totalPages > 0) {
%>
	<div class="pages" id="pages">
		<div id="previousPage" style="padding: 1rem;" onclick="previousPage()"><img
			src="<%=request.getContextPath()%>/static/images/left.png" alt="previous"
			style="width: 1rem" /></div>
		<div class="page-container">
		<% for (int i = 1; i <= totalPages; i++) { %>
			<div class="page <%=pageNo==i?"activePage":""%>" onclick="changePage(this)"><%=i%></div>
		<% } %>
        </div>
		<div id="nextPage" style="padding: 1rem;" onclick="nextPage()"><img
			src="<%=request.getContextPath()%>/static/images/right.png" alt="next"
			style="width: 1rem" /></div>
	</div>
	
	<script src="<%=request.getContextPath()%>/static/script/page.js"></script>
<%  } %>