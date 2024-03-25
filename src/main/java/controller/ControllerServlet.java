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
import customexceptions.InsufficientFundException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import logicallayer.AdminHandler;
import logicallayer.CustomerHandler;
import logicallayer.SessionHandler;
import model.Account;
import model.Branch;
import model.User;
import utility.ActiveStatus;
import utility.Utils;

@WebServlet("/controller/*")
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect(request.getContextPath() + "/");
			return;
		}
		String path = request.getPathInfo();
		System.out.println(path);
		switch (path) {

		case "/login": {
			request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
			break;
		}

		case "/home": {
			System.out.println(path);
			redirectToHomePage(request, response);
			break;
		}

		case "/account": {
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			int months = Math.abs(Utils.parseInt(request.getParameter("months")));
			int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
			if (accountNo != -1) {
				CustomerHandler customerHandler = new CustomerHandler();
				User user = (User) session.getAttribute("user");
				if (user == null) {
					response.sendRedirect(request.getContextPath() + "/controller/login");
					break;
				}
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
		}

		case "/addAccount": {
			AdminHandler adminHandler = new AdminHandler();
			Map<Integer, Branch> branches;
			try {
				branches = adminHandler.getBranches(ActiveStatus.ACTIVE);
				request.setAttribute("branches", branches);
				request.getRequestDispatcher("/WEB-INF/jsp/accountForm.jsp").forward(request, response);
			} catch (CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/moneyTransfer": {
			CustomerHandler customerHandler = new CustomerHandler();
			User user = (User) request.getSession(false).getAttribute("user");
			if (user == null) {
				response.sendRedirect(request.getContextPath() + "/controller/login");
				break;
			}
			try {
				Map<Integer, Account> accounts = (Map<Integer, Account>) customerHandler.getAccounts(user.getUserId());
				request.setAttribute("accounts", accounts);
				request.getRequestDispatcher("/WEB-INF/jsp/moneyTransfer.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/withdrawl": {
			CustomerHandler customerHandler = new CustomerHandler();
			User user = (User) request.getSession(false).getAttribute("user");
			if (user == null) {
				response.sendRedirect(request.getContextPath() + "/controller/login");
				break;
			}
			try {
				Map<Integer, Account> accounts = (Map<Integer, Account>) customerHandler.getAccounts(user.getUserId());
				request.setAttribute("accounts", accounts);
				request.setAttribute("path", "withdrawl");
				request.getRequestDispatcher("/WEB-INF/jsp/transactionForm.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/deposit": {
			CustomerHandler customerHandler = new CustomerHandler();
			User user = (User) request.getSession(false).getAttribute("user");
			if (user == null) {
				response.sendRedirect(request.getContextPath() + "/controller/login");
				break;
			}
			try {
				Map<Integer, Account> accounts = (Map<Integer, Account>) customerHandler.getAccounts(user.getUserId());
				request.setAttribute("accounts", accounts);
				request.setAttribute("path", "deposit");
				request.getRequestDispatcher("/WEB-INF/jsp/transactionForm.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/manageAccount": {
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			if (accountNo != -1) {
				AdminHandler adminHandler = new AdminHandler();
				User user = (User) session.getAttribute("user");
				if (user == null) {
					response.sendRedirect(request.getContextPath() + "/controller/login");
					break;
				}
				try {
					request.setAttribute("account", adminHandler.getAccount(accountNo));
					request.getRequestDispatcher("/WEB-INF/jsp/viewAccount.jsp").forward(request, response);
				} catch (InvalidValueException | CustomException e) {
					e.printStackTrace();
					response.sendError(401, e.getMessage());
				}
			} else {
				response.sendRedirect(request.getContextPath() + "/controller/home");
//				response.getWriter().println("<script>alert('Invalid Account No!');window.location.href='"
//						+ request.getContextPath() + "/controller/home';</script>");
			}
			break;
		}

		case "/branches": {
			AdminHandler handler = new AdminHandler();
			try {
				int branchStatus = Math.abs(Utils.parseInt(request.getParameter("branchStatus")));
				request.setAttribute("status", branchStatus);
				ActiveStatus status = ActiveStatus.values()[branchStatus];
				Map<Integer, Branch> branches = handler.getBranches(status);
				request.setAttribute("branches", branches);
				request.getRequestDispatcher("/WEB-INF/jsp/branches.jsp").forward(request, response);
			} catch (CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/addBranch": {
			request.getRequestDispatcher("/WEB-INF/jsp/branchForm.jsp").forward(request, response);
			break;
		}

		case "/branch": {
			int branchId = Utils.parseInt(request.getParameter("id"));
			request.setAttribute("branchId", branchId);
			AdminHandler adminHandler = new AdminHandler();
			try {
				Branch branch = adminHandler.getBranch(branchId);
				request.setAttribute("branch", branch);
				request.getRequestDispatcher("/WEB-INF/jsp/viewBranch.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/users": {
			AdminHandler handler = new AdminHandler();
			int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
			int uType = Math.abs(Utils.parseInt(request.getParameter("userType")));
			try {
				if (uType == 0) {
					request.setAttribute("totalPages", handler.getAllCustomerPageCount(10));
					request.setAttribute("customers", handler.getAllCustomers(pageNo, 10));
				} else {
					request.setAttribute("totalPages", handler.getAllEmployeesPageCount(10));
					request.setAttribute("employees", handler.getAllEmployees(pageNo, 10));
				}
				request.setAttribute("userType", uType);
				request.getRequestDispatcher("/WEB-INF/jsp/users.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/manageUser": {
			AdminHandler handler = new AdminHandler();
			int userId = Utils.parseInt(request.getParameter("userId"));
			request.setAttribute("userId", userId);
			request.getRequestDispatcher("/WEB-INF/jsp/viewUser.jsp").forward(request, response);
			break;
		}

		case "/error":
			request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
			break;

		case "/logout":
			session.removeAttribute("user");
			session.invalidate();
			response.sendRedirect(request.getContextPath() + "/controller/login");
			break;

		default:
			response.sendError(404);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		System.out.println(path + " post");
		HttpSession session = request.getSession(false);
		if (!path.equals("/login") && (session == null || session.getAttribute("user") == null)) {
			response.sendRedirect(request.getContextPath() + "/");
			return;
		}
		switch (path) {

		case "/login": {
			try {
				int userId = Integer.parseInt(request.getParameter("userId"));
				String password = request.getParameter("password");
				SessionHandler sessionHandler = new SessionHandler();
				User user = sessionHandler.authenticate(userId, password);
				if (user == null) {
					response.sendRedirect(request.getContextPath() + "/controller/login");
					break;
				}
				HttpSession newSession = request.getSession();
				newSession.setAttribute("user", user);
				System.out.println(user);
				response.sendRedirect(request.getContextPath() + "/controller/home");
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/changeMpin": {
			CustomerHandler customerHandler = new CustomerHandler();
			User user = (User) request.getSession(false).getAttribute("user");
			if (user == null) {
				response.sendRedirect(request.getContextPath() + "/controller/login");
				break;
			}
			try {
				int id = user.getUserId();
				int accountNo = Utils.parseInt(request.getParameter("accountNo"));
				String currentMin = request.getParameter("currentMpin");
				String newMpin = request.getParameter("newMpin");
				String confirmMpin = request.getParameter("confirmMpin");
				if (!newMpin.equals(confirmMpin)) {
					throw new InvalidValueException("Please re-enter new Mpin correctly!");
				}
				System.out.println(accountNo);
				customerHandler.changeMpin(id, accountNo, currentMin, newMpin);
				String message = "MPIN Updated Successful!";
				String redirectUrl = request.getContextPath() + "/controller/home";
				response.getWriter().println(
						"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/setPrimary": {
			CustomerHandler customerHandler = new CustomerHandler();
			User user = (User) request.getSession(false).getAttribute("user");
			if (user == null) {
				response.sendRedirect(request.getContextPath() + "/controller/login");
				break;
			}
			try {
				int id = user.getUserId();
				int accountNo = Utils.parseInt(request.getParameter("accountNo"));
				customerHandler.setPrimaryAccount(id, accountNo);
				String message = "Primary Account Updated Successful!";
				String redirectUrl = request.getContextPath() + "/controller/home";
				response.getWriter().println(
						"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/moneyTransfer": {
			CustomerHandler customerHandler = new CustomerHandler();
			User user = (User) request.getSession(false).getAttribute("user");
			if (user == null) {
				response.sendRedirect(request.getContextPath() + "/controller/login");
				break;
			}
			int id = user.getUserId();
			String mpin = request.getParameter("mpin");
			Double amount = Utils.parseDouble(request.getParameter("amount"));
			int accountNo = Utils.parseInt(request.getParameter("senderAccountNo"));
			int benificiaryAccountNo = Utils.parseInt(request.getParameter("beneficiaryAccNo"));
			String ifsc = request.getParameter("beneficiaryIfsc");
			String description = request.getParameter("description");
			try {
				customerHandler.moneyTransfer(id, mpin, accountNo, benificiaryAccountNo, amount, ifsc, description);
				String message = "Transaction Successful!";
				String redirectUrl = request.getContextPath() + "/controller/home";
				response.getWriter().println(
						"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
			} catch (CustomException | InvalidValueException | InsufficientFundException
					| InvalidOperationException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/withdrawl": {
			CustomerHandler customerHandler = new CustomerHandler();
			User user = (User) request.getSession(false).getAttribute("user");
			if (user == null) {
				response.sendRedirect(request.getContextPath() + "/controller/login");
				break;
			}
			int id = user.getUserId();
			String mpin = request.getParameter("mpin");
			Double amount = Utils.parseDouble(request.getParameter("amount"));
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			String description = request.getParameter("description");
			try {
				customerHandler.withdrawl(id, mpin, accountNo, amount, description);
				String message = "Withdraw Successful!";
				String redirectUrl = request.getContextPath() + "/controller/home";
				response.getWriter().println(
						"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
			} catch (CustomException | InvalidValueException | InsufficientFundException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/deposit": {
			CustomerHandler customerHandler = new CustomerHandler();
			User user = (User) request.getSession(false).getAttribute("user");
			if (user == null) {
				response.sendRedirect(request.getContextPath() + "/controller/login");
				break;
			}
			int id = user.getUserId();
			String mpin = request.getParameter("mpin");
			Double amount = Utils.parseDouble(request.getParameter("amount"));
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			String description = request.getParameter("description");
			try {
				customerHandler.deposit(id, mpin, accountNo, amount, description);
				String message = "Deposit Successful!";
				String redirectUrl = request.getContextPath() + "/controller/home";
				response.getWriter().println(
						"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/manageAccount": {
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			String activate = request.getParameter("activate");
			String deactivate = request.getParameter("deactivate");
			ActiveStatus status = null;
			if (activate != null && activate.equals("1")) {
				status = ActiveStatus.values()[1];
			} else if (deactivate != null && deactivate.equals("1")) {
				status = ActiveStatus.values()[0];
			}
			AdminHandler adminHandler = new AdminHandler();
			try {
				if (status != null) {
					adminHandler.setAccountStatus(accountNo, status);
					String message = "Account Status Updated!";
					String redirectUrl = request.getContextPath() + "/controller/manageAccount?accountNo=" + accountNo;
					response.getWriter().println(
							"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
				} else {
					response.sendRedirect(
							request.getContextPath() + "/controller/manageAccount?accountNo=" + accountNo);
				}
			} catch (CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/addAccount": {
			AdminHandler adminHandler = new AdminHandler();
			try {
				int customerId = Utils.parseInt(request.getParameter("customerId"));
				int branchId = Utils.parseInt(request.getParameter("branchId"));
				adminHandler.createAccount(customerId, branchId);
				response.sendRedirect(request.getContextPath() + "/controller/home");
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/addBranch": {
			AdminHandler adminHandler = new AdminHandler();
			try {
				Branch branch = new Branch();
				branch.setIfsc(request.getParameter("ifsc"));
				branch.setLocation(request.getParameter("location"));
				branch.setCity(request.getParameter("city"));
				branch.setState(request.getParameter("state"));
				adminHandler.addBranch(branch);
				response.sendRedirect(request.getContextPath() + "/controller/branches");
			} catch (CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/branch": {
			int branchId = Utils.parseInt(request.getParameter("branchId"));
			String deactivate = request.getParameter("deactivate");
			AdminHandler adminHandler = new AdminHandler();
			try {
				if (deactivate != null && deactivate.equals("1")) {
					adminHandler.removeBranch(branchId);
					String message = "Removed Branch!";
					String redirectUrl = request.getContextPath() + "/controller/branch?id=" + branchId;
					response.getWriter().println(
							"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
				} else {
					response.sendRedirect(request.getContextPath() + "/controller/branch?id=" + branchId);
				}
			} catch (CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		default:
			response.getWriter().println("wrong url!");
//			response.sendError(404);
			break;
		}
	}

	private void redirectToHomePage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("user");
		if (user == null) {
			request.getRequestDispatcher("/login").forward(request, response);
			return;
		}
		switch (user.getType()) {
		case ADMIN: {
			try {
				AdminHandler handler = new AdminHandler();
				String id = request.getParameter("branchId");
				Map<Integer, Account> accounts = null;
				int pages = 0;
				if (id != null && !id.isEmpty()) {
					int branchId = Integer.parseInt(id);
					accounts = handler.getBranchAccounts(branchId);
					pages = handler.getBranchAccountsPageCount(branchId, 10);
					request.setAttribute("branchId", branchId);
				}
				request.setAttribute("totalPages", pages);
				request.setAttribute("accounts", accounts);
				int status = Math.abs(Utils.parseInt(request.getParameter("branchStatus")));
				ActiveStatus branchStatus = ActiveStatus.values()[status];
				request.setAttribute("status", status);
				request.setAttribute("branches", handler.getBranches(branchStatus));
				request.getRequestDispatcher("/WEB-INF/jsp/accounts.jsp").forward(request, response);

			} catch (InvalidValueException | CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case USER: {
			try {
				CustomerHandler customerHandler = new CustomerHandler();
				User customer = (User) request.getSession(false).getAttribute("user");
				if (customer == null) {
					response.sendRedirect(request.getContextPath() + "/controller/login");
					break;
				}
				int customerId = customer.getUserId();
				Map<Integer, Account> accounts = customerHandler.getAccounts(customerId);
				request.setAttribute("accounts", accounts);
				double totalBalance = accounts.entrySet().stream().mapToDouble(e -> e.getValue().getCurrentBalance())
						.sum();
				request.setAttribute("totalBalance", totalBalance);
				request.getRequestDispatcher("/WEB-INF/jsp/customer.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case EMPLOYEE: {
			request.getRequestDispatcher("/WEB-INF/jsp/accounts.jsp").forward(request, response);
			break;
		}

		default:
			request.getRequestDispatcher("/login").forward(request, response);
		}
	}

}
