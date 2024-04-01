package controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utility.UserType;

@WebFilter("/controller/*")
public class AccessFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		String path = req.getPathInfo();
		System.out.println("in filter " + path);
		if (path == null) {
			res.sendRedirect(req.getContextPath() + "/");
			return;
		}
		if (!path.equals("/login") && !path.equals("/logout")) {
			if (session == null || session.getAttribute("userId") == null) {
				res.sendRedirect(req.getContextPath() + "/");
				return;
			}
			UserType userType = (UserType) session.getAttribute("userType");
			String pathPrivilege = path.substring(1, path.indexOf("/", 1));
			if (userType != UserType.valueOf(pathPrivilege.toUpperCase())) {
				res.sendError(403);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

}
