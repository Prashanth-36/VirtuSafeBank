package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

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
import logicallayer.EmployeeHandler;
import logicallayer.SessionHandler;
import model.Account;
import model.Branch;
import model.Customer;
import model.Employee;
import model.User;
import utility.ActiveStatus;
import utility.Gender;
import utility.UserType;
import utility.Utils;
import utility.Validate;

@WebServlet("/controller/*")
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		HttpSession session = request.getSession(false);
		if (!path.equals("/login") && (session == null || session.getAttribute("user") == null)) {
			response.sendRedirect(request.getContextPath() + "/");
			return;
		}
		System.out.println(path + "  get");
		switch (path) {

		case "/login": {
			request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
			break;
		}

		case "/user/home": {
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
				request.getRequestDispatcher("/WEB-INF/jsp/user/customer.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/home": {
			try {
				AdminHandler handler = new AdminHandler();
				String id = request.getParameter("branchId");
				Map<Integer, Account> accounts = null;
				int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
				request.setAttribute("page", pageNo);
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
				request.getRequestDispatcher("/WEB-INF/jsp/admin/accounts.jsp").forward(request, response);

			} catch (InvalidValueException | CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/employee/home": {
			try {
				EmployeeHandler handler = new EmployeeHandler();
				Employee employee = (Employee) session.getAttribute("user");
				int branchId = employee.getBranchId();
				Map<Integer, Account> accounts = null;
				int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
				request.setAttribute("page", pageNo);
				int pages = 0;
				accounts = handler.getBranchAccounts(branchId);
				pages = handler.getBranchAccountsPageCount(branchId, 10);
				request.setAttribute("accounts", accounts);
				request.setAttribute("totalPages", pages);
				request.getRequestDispatcher("/WEB-INF/jsp/employee/employee.jsp").forward(request, response);
			} catch (InvalidValueException | CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/user/account": {
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			int months = Math.abs(Utils.parseInt(request.getParameter("months")));
			int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
			request.setAttribute("page", pageNo);
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
					request.getRequestDispatcher("/WEB-INF/jsp/user/customerAccount.jsp").forward(request, response);
				} catch (InvalidValueException | CustomException e) {
					e.printStackTrace();
					response.sendError(401, e.getMessage());
				}
			}
			break;
		}

		case "/user/moneyTransfer": {
			CustomerHandler customerHandler = new CustomerHandler();
			User user = (User) request.getSession(false).getAttribute("user");
			if (user == null) {
				response.sendRedirect(request.getContextPath() + "/controller/login");
				break;
			}
			try {
				Map<Integer, Account> accounts = (Map<Integer, Account>) customerHandler.getAccounts(user.getUserId());
				request.setAttribute("accounts", accounts);
				request.getRequestDispatcher("/WEB-INF/jsp/user/moneyTransfer.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/user/withdrawl": {
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
				request.getRequestDispatcher("/WEB-INF/jsp/user/transactionForm.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/user/deposit": {
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
				request.getRequestDispatcher("/WEB-INF/jsp/user/transactionForm.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/addAccount": {
			AdminHandler adminHandler = new AdminHandler();
			Map<Integer, Branch> branches;
			try {
				branches = adminHandler.getBranches(ActiveStatus.ACTIVE);
				request.setAttribute("branches", branches);
				request.getRequestDispatcher("/WEB-INF/jsp/admin/accountForm.jsp").forward(request, response);
			} catch (CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/manageAccount": {
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			if (accountNo != -1) {
				AdminHandler adminHandler = new AdminHandler();
				try {
					request.setAttribute("account", adminHandler.getAccount(accountNo));
					request.getRequestDispatcher("/WEB-INF/jsp/admin/viewAccount.jsp").forward(request, response);
				} catch (InvalidValueException | CustomException e) {
					e.printStackTrace();
					response.sendError(401, e.getMessage());
				}
			} else {
				response.sendRedirect(request.getContextPath() + "/controller/home");
			}
			break;
		}

		case "/admin/branches": {
			AdminHandler handler = new AdminHandler();
			try {
				int branchStatus = Math.abs(Utils.parseInt(request.getParameter("branchStatus")));
				request.setAttribute("status", branchStatus);
				ActiveStatus status = ActiveStatus.values()[branchStatus];
				Map<Integer, Branch> branches = handler.getBranches(status);
				request.setAttribute("branches", branches);
				request.getRequestDispatcher("/WEB-INF/jsp/admin/branches.jsp").forward(request, response);
			} catch (CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/addBranch": {
			request.getRequestDispatcher("/WEB-INF/jsp/admin/branchForm.jsp").forward(request, response);
			break;
		}

		case "/admin/branch": {
			int branchId = Utils.parseInt(request.getParameter("id"));
			request.setAttribute("branchId", branchId);
			AdminHandler adminHandler = new AdminHandler();
			try {
				Branch branch = adminHandler.getBranch(branchId);
				request.setAttribute("branch", branch);
				request.getRequestDispatcher("/WEB-INF/jsp/admin/viewBranch.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/modifyBranch": {
			int branchId = Utils.parseInt(request.getParameter("id"));
			AdminHandler adminHandler = new AdminHandler();
			try {
				request.setAttribute("branch", adminHandler.getBranch(branchId));
				request.getRequestDispatcher("/WEB-INF/jsp/admin/branchForm.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/users": {
			AdminHandler handler = new AdminHandler();
			int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
			int uType = Math.abs(Utils.parseInt(request.getParameter("userType")));
			request.setAttribute("page", pageNo);
			try {
				if (uType == 0) {
					request.setAttribute("totalPages", handler.getAllCustomerPageCount(10));
					request.setAttribute("customers", handler.getAllCustomers(pageNo, 10));
				} else {
					request.setAttribute("totalPages", handler.getAllEmployeesPageCount(10));
					request.setAttribute("employees", handler.getAllEmployees(pageNo, 10));
				}
				request.setAttribute("userType", uType);
				request.getRequestDispatcher("/WEB-INF/jsp/admin/users.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/manageUser": {
			AdminHandler handler = new AdminHandler();
			int userId = Utils.parseInt(request.getParameter("userId"));
			request.setAttribute("userId", userId);
			int type = Utils.parseInt(request.getParameter("userType"));
			request.setAttribute("userType", type);
			try {
				if (type != -1) {
					if (UserType.values()[type] == UserType.USER) {
						Customer customer = handler.getCustomer(userId);
						request.setAttribute("user", customer);
					} else {
						Employee employee = handler.getEmployee(userId);
						request.setAttribute("user", employee);
					}
				}
				request.getRequestDispatcher("/WEB-INF/jsp/admin/viewUser.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/addUser": {
			AdminHandler adminHandler = new AdminHandler();
			try {
				Map<Integer, Branch> branches = adminHandler.getBranches(ActiveStatus.ACTIVE);
				request.setAttribute("branches", branches);
				request.getRequestDispatcher("/WEB-INF/jsp/admin/userForm.jsp").forward(request, response);
			} catch (CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/modifyUser": {
			int userId = Utils.parseInt(request.getParameter("userId"));
			String type = request.getParameter("userType");
			AdminHandler adminHandler = new AdminHandler();
			request.setAttribute("type", type);
			try {
				if (type != null && UserType.valueOf(type) == UserType.USER) {
					request.setAttribute("user", adminHandler.getCustomer(userId));
				} else {
					request.setAttribute("user", adminHandler.getEmployee(userId));
					Map<Integer, Branch> branches = adminHandler.getBranches(ActiveStatus.ACTIVE);
					request.setAttribute("branches", branches);
				}
				request.getRequestDispatcher("/WEB-INF/jsp/admin/userForm.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/employee/addAccount": {
			request.getRequestDispatcher("/WEB-INF/jsp/employee/accountForm.jsp").forward(request, response);
			break;
		}

		case "/employee/manageAccount": {
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
			int months = Math.abs(Utils.parseInt(request.getParameter("months")));
			if (accountNo != -1) {
				EmployeeHandler employeeHandler = new EmployeeHandler();
				try {
					request.setAttribute("account", employeeHandler.getAccount(accountNo));
					request.setAttribute("totalPages", employeeHandler.getTransactionPageCount(accountNo, 1, 10));
					request.setAttribute("page", pageNo);
					request.setAttribute("transactions",
							employeeHandler.getTransactions(accountNo, months, pageNo, 10));
					request.getRequestDispatcher("/WEB-INF/jsp/employee/accountDetails.jsp").forward(request, response);
				} catch (InvalidValueException | CustomException e) {
					e.printStackTrace();
					response.sendError(401, e.getMessage());
				}
			} else {
				response.sendRedirect(request.getContextPath() + "/controller/home");
			}
			break;
		}

		case "/employee/users": {
			EmployeeHandler employeeHandler = new EmployeeHandler();
			int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
			request.setAttribute("page", pageNo);
			int customerStatus = Math.abs(Utils.parseInt(request.getParameter("status")));
			request.setAttribute("status", customerStatus);
			ActiveStatus status = ActiveStatus.values()[customerStatus];
			Employee employee = (Employee) session.getAttribute("user");
			int branchId = employee.getBranchId();
			try {
				request.setAttribute("totalPages", employeeHandler.getCustomerPageCount(branchId, 10, status));
				request.setAttribute("customers", employeeHandler.getCustomers(branchId, pageNo, 10, status));
				request.getRequestDispatcher("/WEB-INF/jsp/employee/users.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/employee/addUser": {
			request.getRequestDispatcher("/WEB-INF/jsp/employee/userForm.jsp").forward(request, response);
			break;
		}

		case "/employee/manageUser": {
			AdminHandler handler = new AdminHandler();
			int userId = Utils.parseInt(request.getParameter("userId"));
			request.setAttribute("userId", userId);
			try {
				Customer customer = handler.getCustomer(userId);
				request.setAttribute("user", customer);
				request.getRequestDispatcher("/WEB-INF/jsp/employee/userDetails.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/employee/modifyUser": {
			int userId = Utils.parseInt(request.getParameter("userId"));
			EmployeeHandler employeeHandler = new EmployeeHandler();
			try {
				request.setAttribute("user", employeeHandler.getCustomer(userId));
				request.getRequestDispatcher("/WEB-INF/jsp/employee/userForm.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/employee/fundTransfer": {
			EmployeeHandler employeeHandler = new EmployeeHandler();
			try {
				Employee employee = (Employee) session.getAttribute("user");
				Set<Integer> accounts = employeeHandler.getBranchAccounts(employee.getBranchId()).keySet();
				request.setAttribute("accounts", accounts);
				request.getRequestDispatcher("/WEB-INF/jsp/employee/transactionForm.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
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
				HttpSession httpSession = request.getSession();
				httpSession.setAttribute("user", user);
				switch (user.getType()) {
				case ADMIN:
					response.sendRedirect(request.getContextPath() + "/controller/admin/home");
					break;
				case EMPLOYEE:
					response.sendRedirect(request.getContextPath() + "/controller/employee/home");
					break;
				case USER:
					response.sendRedirect(request.getContextPath() + "/controller/user/home");
				}
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/user/changeMpin": {
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
				String redirectUrl = request.getContextPath() + "/controller/user/home";
				response.getWriter().println(
						"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/user/setPrimary": {
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
				String redirectUrl = request.getContextPath() + "/controller/user/home";
				response.getWriter().println(
						"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/user/moneyTransfer": {
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
				String redirectUrl = request.getContextPath() + "/controller/user/home";
				response.getWriter().println(
						"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
			} catch (CustomException | InvalidValueException | InsufficientFundException
					| InvalidOperationException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/user/withdrawl": {
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
				String redirectUrl = request.getContextPath() + "/controller/user/home";
				response.getWriter().println(
						"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
			} catch (CustomException | InvalidValueException | InsufficientFundException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/user/deposit": {
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
				String redirectUrl = request.getContextPath() + "/controller/user/home";
				response.getWriter().println(
						"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/manageAccount": {
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
					String redirectUrl = request.getContextPath() + "/controller/admin/manageAccount?accountNo="
							+ accountNo;
					response.getWriter().println(
							"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
				} else {
					response.sendRedirect(
							request.getContextPath() + "/controller/admin/manageAccount?accountNo=" + accountNo);
				}
			} catch (CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/addAccount": {
			AdminHandler adminHandler = new AdminHandler();
			try {
				int customerId = Utils.parseInt(request.getParameter("customerId"));
				int branchId = Utils.parseInt(request.getParameter("branchId"));
				adminHandler.createAccount(customerId, branchId);
				response.sendRedirect(request.getContextPath() + "/controller/admin/home");
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/addBranch": {
			AdminHandler adminHandler = new AdminHandler();
			try {
				Branch branch = new Branch();
				branch.setIfsc(request.getParameter("ifsc"));
				branch.setLocation(request.getParameter("location"));
				branch.setCity(request.getParameter("city"));
				branch.setState(request.getParameter("state"));
				adminHandler.addBranch(branch);
				response.sendRedirect(request.getContextPath() + "/controller/admin/branches");
			} catch (CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/branch": {
			int branchId = Utils.parseInt(request.getParameter("branchId"));
			String deactivate = request.getParameter("deactivate");
			AdminHandler adminHandler = new AdminHandler();
			try {
				if (deactivate != null && deactivate.equals("1")) {
					adminHandler.removeBranch(branchId);
					String message = "Removed Branch!";
					String redirectUrl = request.getContextPath() + "/controller/admin/branch?id=" + branchId;
					response.getWriter().println(
							"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
				} else {
					response.sendRedirect(request.getContextPath() + "/controller/admin/branch?id=" + branchId);
				}
			} catch (CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/manageUser": {
			int userId = Utils.parseInt(request.getParameter("userId"));
			String activate = request.getParameter("activate");
			String deactivate = request.getParameter("deactivate");
			String type = request.getParameter("userType");
			ActiveStatus status = null;
			if (activate != null && activate.equals("1")) {
				status = ActiveStatus.values()[1];
			} else if (deactivate != null && deactivate.equals("1")) {
				status = ActiveStatus.values()[0];
			}
			AdminHandler adminHandler = new AdminHandler();
			try {
				if (status != null) {
					if (type != null && UserType.valueOf(type) == UserType.USER) {
						adminHandler.setCustomerStatus(userId, status);
					} else {
						adminHandler.setEmployeeStatus(userId, status);
					}
					String message = "User Status Updated!";
					response.getWriter().println(
							"<script>alert('" + message + "'); window.location.href=document.referrer</script>");
				} else {
					response.getWriter().println(
							"<script>alert('Invalid request url!'); window.location.href=document.referrer</script>");
				}
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/addUser": {
			AdminHandler adminHandler = new AdminHandler();
			try {
				int type = Integer.parseInt(request.getParameter("userType"));
				if (type == 0) {
					Customer customer = new Customer();
					customer.setName(request.getParameter("name"));
					customer.setDob(Utils.getMillis(LocalDate.parse(request.getParameter("dob"))));
					customer.setGender(Gender.values()[Integer.parseInt(request.getParameter("gender"))]);
					String number = request.getParameter("number");
					Validate.mobile(number);
					customer.setNumber(Long.parseLong(number));
					String email = request.getParameter("email");
					Validate.email(email);
					customer.setEmail(email);
					customer.setType(UserType.values()[Integer.parseInt(request.getParameter("userType"))]);
					customer.setPassword(request.getParameter("password"));
					String aadhaar = request.getParameter("aadhaarNo");
					Validate.aadhaar(aadhaar);
					customer.setAadhaarNo(Long.parseLong(aadhaar));
					String pan = request.getParameter("panNo");
					Validate.pan(pan);
					customer.setPanNo(pan);
					customer.setLocation(request.getParameter("location"));
					customer.setCity(request.getParameter("city"));
					customer.setState(request.getParameter("state"));
					adminHandler.addCustomer(customer);
				} else {
					Employee employee = new Employee();
					employee.setName(request.getParameter("name"));
					employee.setDob(Utils.getMillis(LocalDate.parse(request.getParameter("dob"))));
					employee.setGender(Gender.values()[Integer.parseInt(request.getParameter("gender"))]);
					String number = request.getParameter("number");
					Validate.mobile(number);
					employee.setNumber(Long.parseLong(number));
					String email = request.getParameter("email");
					Validate.email(email);
					employee.setEmail(email);
					employee.setType(UserType.values()[Integer.parseInt(request.getParameter("userType"))]);
					employee.setPassword(request.getParameter("password"));
					employee.setLocation(request.getParameter("location"));
					employee.setCity(request.getParameter("city"));
					employee.setState(request.getParameter("state"));
					employee.setBranchId(Integer.parseInt(request.getParameter("branchId")));
					adminHandler.addEmployee(employee);
				}
				String message = "User Created Successfully!";
				String redirectUrl = request.getContextPath() + "/controller/admin/users";
				response.getWriter().println(
						"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/admin/modifyUser": {
			int userId = Integer.parseInt(request.getParameter("userId"));
			AdminHandler adminHandler = new AdminHandler();
			try {
				int type = Integer.parseInt(request.getParameter("userType"));
				if (type == 0) {
					Customer customer = new Customer();
					customer.setUserId(userId);
					customer.setName(request.getParameter("name"));
					customer.setDob(Utils.getMillis(LocalDate.parse(request.getParameter("dob"))));
					customer.setGender(Gender.values()[Integer.parseInt(request.getParameter("gender"))]);
					String number = request.getParameter("number");
					Validate.mobile(number);
					customer.setNumber(Long.parseLong(number));
					String email = request.getParameter("email");
					Validate.email(email);
					customer.setEmail(email);
					customer.setType(UserType.values()[Integer.parseInt(request.getParameter("userType"))]);
					customer.setPassword(request.getParameter("password"));
					String aadhaar = request.getParameter("aadhaarNo");
					Validate.aadhaar(aadhaar);
					customer.setAadhaarNo(Long.parseLong(aadhaar));
					String pan = request.getParameter("panNo");
					Validate.pan(pan);
					customer.setPanNo(pan);
					customer.setLocation(request.getParameter("location"));
					customer.setCity(request.getParameter("city"));
					customer.setState(request.getParameter("state"));
					adminHandler.updateCustomer(customer);
				} else {
					Employee employee = new Employee();
					employee.setUserId(userId);
					employee.setName(request.getParameter("name"));
					employee.setDob(Utils.getMillis(LocalDate.parse(request.getParameter("dob"))));
					employee.setGender(Gender.values()[Integer.parseInt(request.getParameter("gender"))]);
					String number = request.getParameter("number");
					Validate.mobile(number);
					employee.setNumber(Long.parseLong(number));
					String email = request.getParameter("email");
					Validate.email(email);
					employee.setEmail(email);
					employee.setType(UserType.values()[Integer.parseInt(request.getParameter("userType"))]);
					employee.setPassword(request.getParameter("password"));
					employee.setLocation(request.getParameter("location"));
					employee.setCity(request.getParameter("city"));
					employee.setState(request.getParameter("state"));
					employee.setBranchId(Integer.parseInt(request.getParameter("branchId")));
					adminHandler.updateEmployee(employee);
				}
				String message = "User Data Updated Successfully!";
				response.getWriter()
						.println("<script>alert('" + message + "'); window.location.href=document.referrer</script>");
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/employee/addAccount": {
			EmployeeHandler employeeHandler = new EmployeeHandler();
			int customerId = Utils.parseInt(request.getParameter("customerId"));
			Employee employee = (Employee) session.getAttribute("user");
			try {
				employeeHandler.createAccount(customerId, employee.getBranchId());
				String message = "Account Created Updated!";
				String redirectUrl = request.getContextPath() + "/controller/employee/home";
				response.getWriter().println(
						"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/employee/manageAccount": {
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			String activate = request.getParameter("activate");
			String deactivate = request.getParameter("deactivate");
			ActiveStatus status = null;
			if (activate != null && activate.equals("1")) {
				status = ActiveStatus.values()[1];
			} else if (deactivate != null && deactivate.equals("1")) {
				status = ActiveStatus.values()[0];
			}
			EmployeeHandler employeeHandler = new EmployeeHandler();
			try {
				if (status != null) {
					employeeHandler.setAccountStatus(accountNo, status);
					String message = "Account Status Updated!";
					String redirectUrl = request.getContextPath() + "/controller/employee/manageAccount?accountNo="
							+ accountNo;
					response.getWriter().println(
							"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
				} else {
					response.sendRedirect(
							request.getContextPath() + "/controller/employee/manageAccount?accountNo=" + accountNo);
				}
			} catch (CustomException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/employee/addUser": {
			EmployeeHandler employeeHandler = new EmployeeHandler();
			try {
				Customer customer = new Customer();
				customer.setName(request.getParameter("name"));
				customer.setDob(Utils.getMillis(LocalDate.parse(request.getParameter("dob"))));
				customer.setGender(Gender.values()[Integer.parseInt(request.getParameter("gender"))]);
				String number = request.getParameter("number");
				Validate.mobile(number);
				customer.setNumber(Long.parseLong(number));
				String email = request.getParameter("email");
				Validate.email(email);
				customer.setEmail(email);
				customer.setType(UserType.values()[Integer.parseInt(request.getParameter("userType"))]);
				customer.setPassword(request.getParameter("password"));
				String aadhaar = request.getParameter("aadhaarNo");
				Validate.aadhaar(aadhaar);
				customer.setAadhaarNo(Long.parseLong(aadhaar));
				String pan = request.getParameter("panNo");
				Validate.pan(pan);
				customer.setPanNo(pan);
				customer.setLocation(request.getParameter("location"));
				customer.setCity(request.getParameter("city"));
				customer.setState(request.getParameter("state"));
				employeeHandler.addCustomer(customer);
				String message = "User Created Successfully!";
				String redirectUrl = request.getContextPath() + "/controller/employee/users";
				response.getWriter().println(
						"<script>alert('" + message + "'); window.location.href='" + redirectUrl + "'</script>");
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/employee/manageUser": {
			int userId = Utils.parseInt(request.getParameter("userId"));
			String activate = request.getParameter("activate");
			String deactivate = request.getParameter("deactivate");
			ActiveStatus status = null;
			if (activate != null && activate.equals("1")) {
				status = ActiveStatus.values()[1];
			} else if (deactivate != null && deactivate.equals("1")) {
				status = ActiveStatus.values()[0];
			}
			EmployeeHandler employeeHandler = new EmployeeHandler();
			try {
				employeeHandler.setCustomerStatus(userId, status);
				String message = "User Status Updated!";
				response.getWriter()
						.println("<script>alert('" + message + "'); window.location.href=document.referrer</script>");
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/employee/modifyUser": {
			int userId = Integer.parseInt(request.getParameter("userId"));
			EmployeeHandler employeeHandler = new EmployeeHandler();
			try {
				Customer customer = new Customer();
				customer.setUserId(userId);
				customer.setName(request.getParameter("name"));
				customer.setDob(Utils.getMillis(LocalDate.parse(request.getParameter("dob"))));
				customer.setGender(Gender.values()[Integer.parseInt(request.getParameter("gender"))]);
				customer.setNumber(Long.parseLong(request.getParameter("number")));
				customer.setEmail(request.getParameter("email"));
				customer.setType(UserType.values()[Integer.parseInt(request.getParameter("userType"))]);
				customer.setAadhaarNo(Long.parseLong(request.getParameter("aadhaarNo")));
				customer.setPanNo(request.getParameter("panNo"));
				customer.setLocation(request.getParameter("location"));
				customer.setCity(request.getParameter("city"));
				customer.setState(request.getParameter("state"));
				employeeHandler.updateCustomer(customer);
				String message = "User Data Updated!";
				response.getWriter()
						.println("<script>alert('" + message + "'); window.location.href=document.referrer</script>");
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		case "/employee/fundTransfer": {
			EmployeeHandler employeeHandler = new EmployeeHandler();
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			double amount = Utils.parseDouble(request.getParameter("amount"));
			String description = request.getParameter("description");
			try {
				employeeHandler.deposit(accountNo, amount, description);
				String message = "Amount Deposited!";
				response.getWriter()
						.println("<script>alert('" + message + "'); window.location.href=document.referrer</script>");
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.getWriter().println(e.getMessage());
			}
			break;
		}

		default:
			response.getWriter().println("wrong url!");
			break;
		}

	}
}