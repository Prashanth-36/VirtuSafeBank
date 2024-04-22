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

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import logicallayer.SessionHandler;
import model.Token;

@WebFilter("/api/*")
public class ApiAccessFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		SessionHandler sessionHandler = new SessionHandler();
		String userToken = req.getHeader("Authorization");
		System.out.println(userToken);
		res.setContentType("application/json");
		if (userToken == null) {
			res.getWriter().write("{\"error\":\"Invalid Token\"}");
			return;
		}
		try {
			Token token = sessionHandler.getToken(userToken);
			if (!req.getMethod().equals("GET") && token.getAccessLevel() == 0) {
				res.getWriter().write("{\"error\":\"Restricted Access readonly token\"}");
				return;
			}
			if (token.getValidTill() - System.currentTimeMillis() <= 0) {
				sessionHandler.removeToken(userToken);
				res.getWriter().write("{\"error\":\"Token Expired!\"}");
				return;
			}
		} catch (CustomException | InvalidValueException e) {
			e.printStackTrace();
			res.getWriter().write("{\"error\":" + e.getMessage() + "}");
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
