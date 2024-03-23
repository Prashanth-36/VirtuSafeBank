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
import logicallayer.CustomerHandler;
import logicallayer.SessionHandler;
import model.Account;
import model.User;
import utility.Utils;

@WebServlet("/controller/*")
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		switch (path) {

		case "/login":
			request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
			break;

		case "/home":
			redirectToHomePage(request, response);
			break;

		case "/account":
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			int months = Math.abs(Utils.parseInt(request.getParameter("months")));
			int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
			if (accountNo != -1) {
				CustomerHandler customerHandler = new CustomerHandler();
				HttpSession session = request.getSession();
				User user = (User) session.getAttribute("user");
				try {
					request.setAttribute("totalPages",
							customerHandler.getTransactionPageCount(user.getUserId(), accountNo, months, 10));
					request.setAttribute("transactions",
							customerHandler.getTransactions(user.getUserId(), accountNo, months, pageNo, 10));
					request.getRequestDispatcher("/WEB-INF/jsp/customerAccount.jsp").forward(request, response);
				} catch (InvalidValueException | CustomException e) {
					e.printStackTrace();
					response.sendError(401, e.getMessage());
				}
			}
			break;

		case "/moneyTransfer":
			request.getRequestDispatcher("/WEB-INF/jsp/moneyTransfer.jsp").forward(request, response);
			break;

		case "/withdrawl":
			request.setAttribute("path", "withdrawl");
			request.getRequestDispatcher("/WEB-INF/jsp/transactionForm.jsp").forward(request, response);
			break;

		case "/deposit":
			request.setAttribute("path", "deposit");
			request.getRequestDispatcher("/WEB-INF/jsp/transactionForm.jsp").forward(request, response);
			break;

		case "/error":
			request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
			break;

		case "/logout":
			HttpSession session = request.getSession();
			session.removeAttribute("user");
			session.invalidate();
			break;

		default:
			response.sendError(404);
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
				System.out.println(user);
				response.sendRedirect(request.getContextPath() + "/controller/home");
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
			}
			break;
		}

		case "/moneyTransfer":
			request.getRequestDispatcher("/WEB-INF/jsp/moneyTransfer.jsp").forward(request, response);
			break;

		case "/withdrawl":
			request.setAttribute("path", "withdrawl");
			request.getRequestDispatcher("/WEB-INF/jsp/transactionForm.jsp").forward(request, response);
			break;

		case "/deposit":
			request.setAttribute("path", "deposit");
			request.getRequestDispatcher("/WEB-INF/jsp/transactionForm.jsp").forward(request, response);
			break;

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
				break;
			}

			case USER:
				try {
					CustomerHandler customerHandler = new CustomerHandler();
					User customer = (User) request.getSession().getAttribute("user");
					int customerId = customer.getUserId();
					Map<Integer, Account> accounts = customerHandler.getAccounts(customerId);
					request.setAttribute("accounts", accounts);
					double totalBalance = accounts.entrySet().stream()
							.mapToDouble(e -> e.getValue().getCurrentBalance()).sum();
					request.setAttribute("totalBalance", totalBalance);
					request.getRequestDispatcher("/WEB-INF/jsp/customer.jsp").forward(request, response);
				} catch (CustomException | InvalidValueException e) {
					e.printStackTrace();
				}
				break;

			case EMPLOYEE:
				request.getRequestDispatcher("/WEB-INF/jsp/accounts.jsp").forward(request, response);
				break;

			default:
				request.getRequestDispatcher("/login").forward(request, response);
			}
		} else {
			request.getRequestDispatcher("/login").forward(request, response);
		}
	}

}
