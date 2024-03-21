package controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import logicallayer.AdminHandler;
import logicallayer.SessionHandler;
import model.Account;
import model.User;

@WebServlet("/controller/*")
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		switch (path) {
		case "/login":
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			break;

		case "/home":
			redirectToHomePage(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		System.out.println(path + " post");
		switch (path) {

		case "/login": {
			try {
				int userId = Integer.parseInt(request.getParameter("userId"));
				String password = request.getParameter("password");
				SessionHandler sessionHandler = new SessionHandler();
				User user = sessionHandler.authenticate(userId, password);
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				response.sendRedirect(request.getContextPath() + "/controller/home");
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
			}
			break;
		}

		default:
			response.sendError(404);
			break;
		}
	}

	private void redirectToHomePage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user != null) {
			switch (user.getType()) {
			case ADMIN: {
				try {
					AdminHandler handler = new AdminHandler();
					String id = request.getParameter("branchId");
					Map<Integer, Account> accounts = null;
					if (id != null && !id.isEmpty()) {
						int branchId = Integer.parseInt(id);
						accounts = handler.getBranchAccounts(branchId);
					}
					request.setAttribute("accounts", accounts);
					request.getRequestDispatcher("/WEB-INF/jsp/accounts.jsp").forward(request, response);
				} catch (InvalidValueException | CustomException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			case USER:
				request.getRequestDispatcher("/WEB-INF/jsp/customer.jsp").forward(request, response);

			case EMPLOYEE:
				request.getRequestDispatcher("/WEB-INF/jsp/accounts.jsp").forward(request, response);

			default:
				request.getRequestDispatcher("/login").forward(request, response);
			}
		} else {
			request.getRequestDispatcher("/login").forward(request, response);
		}
	}

}
