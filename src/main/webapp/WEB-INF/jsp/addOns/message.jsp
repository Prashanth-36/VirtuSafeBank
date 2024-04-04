<% String message=(String) request.getAttribute("message") ;
  message=message!=null?message:request.getParameter("message"); String error=(String) request.getAttribute("error") ;
  error=error!=null?error:request.getParameter("error"); if(message!=null || error!=null){ %>
  <div class="card <%=message!=null?" success":"error"%>" id="message">
    <%=message!=null?message:error%>
  </div>
  <script>
    setTimeout(() => {
      document.getElementById('message').style.display = 'none';
      /*const params = new URLSearchParams(location.search);
      params.delete("message");
      params.delete("error");
      location.search = params;*/
      const url = new URL(window.location.href);
      url.searchParams.delete('error');
      url.searchParams.delete('message');
      history.replaceState({}, '', url);
    }, 4000)
  </script>
  <% } %>