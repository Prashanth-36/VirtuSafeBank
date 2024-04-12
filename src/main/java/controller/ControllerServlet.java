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
import model.Audit;
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

	static AdminHandler adminHandler = new AdminHandler();
	static SessionHandler sessionHandler = new SessionHandler();
	static EmployeeHandler employeeHandler = new EmployeeHandler();
	static CustomerHandler customerHandler = new CustomerHandler();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		System.out.println(path + "  get");
		HttpSession session = request.getSession(false);
		switch (path) {

		case "/login": {
			request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
			break;
		}

		case "/user/home": {
			try {
				int customerId = (int) session.getAttribute("userId");
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
				String id = request.getParameter("branchId");
				Map<Integer, Account> accounts = null;
				int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
				request.setAttribute("page", pageNo);
				int pages = 0;
				if (id != null && !id.isEmpty()) {
					int branchId = Integer.parseInt(id);
					accounts = adminHandler.getBranchAccounts(branchId);
					pages = adminHandler.getBranchAccountsPageCount(branchId, 10);
					request.setAttribute("branchId", branchId);
				}
				request.setAttribute("totalPages", pages);
				request.setAttribute("accounts", accounts);
				int status = Math.abs(Utils.parseInt(request.getParameter("branchStatus")));
				ActiveStatus branchStatus = ActiveStatus.values()[status];
				request.setAttribute("status", status);
				request.setAttribute("branches", adminHandler.getBranches(branchStatus));
				request.getRequestDispatcher("/WEB-INF/jsp/employee/accounts.jsp").forward(request, response);

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
				int branchId = (int) session.getAttribute("branchId");
				Map<Integer, Account> accounts = null;
				int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
				request.setAttribute("page", pageNo);
				int pages = 0;
				accounts = employeeHandler.getBranchAccounts(branchId);
				pages = employeeHandler.getBranchAccountsPageCount(branchId, 10);
				request.setAttribute("accounts", accounts);
				request.setAttribute("totalPages", pages);
				request.getRequestDispatcher("/WEB-INF/jsp/employee/accounts.jsp").forward(request, response);
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
			if (accountNo != -1) {
				try {
					request.setAttribute("accountNo", accountNo);
					int months = Math.abs(Utils.parseInt(request.getParameter("months")));
					request.setAttribute("months", months);
					int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
					request.setAttribute("page", pageNo);
					int userId = (int) session.getAttribute("userId");
					Account account = customerHandler.getAccount(accountNo);
					request.setAttribute("isPrimary", account.getIsPrimaryAccount());
					request.setAttribute("totalPages",
							customerHandler.getTransactionPageCount(userId, accountNo, months, 10));
					request.setAttribute("transactions",
							customerHandler.getTransactions(userId, accountNo, months, pageNo, 10));
					request.getRequestDispatcher("/WEB-INF/jsp/user/customerAccount.jsp").forward(request, response);
				} catch (InvalidValueException | CustomException e) {
					e.printStackTrace();
					response.sendRedirect(request.getContextPath() + "/controller/user/home?error=" + e.getMessage());
				}
			}
			break;
		}

		case "/user/moneyTransfer": {
			try {
				int userId = (int) session.getAttribute("userId");
				Map<Integer, Account> accounts = (Map<Integer, Account>) customerHandler.getAccounts(userId);
				request.setAttribute("accounts", accounts);
				request.setAttribute("path", "moneyTransfer");
				request.getRequestDispatcher("/WEB-INF/jsp/user/moneyTransfer.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/controller/user/home?error=" + e.getMessage());
			}
			break;
		}

		case "/user/withdrawl": {
			try {
				int userId = (int) session.getAttribute("userId");
				Map<Integer, Account> accounts = (Map<Integer, Account>) customerHandler.getAccounts(userId);
				request.setAttribute("accounts", accounts);
				request.setAttribute("path", "withdrawl");
				request.getRequestDispatcher("/WEB-INF/jsp/user/transactionForm.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/controller/user/home?error=" + e.getMessage());
			}
			break;
		}

		case "/user/deposit": {
			try {
				int userId = (int) session.getAttribute("userId");
				Map<Integer, Account> accounts = (Map<Integer, Account>) customerHandler.getAccounts(userId);
				request.setAttribute("accounts", accounts);
				request.setAttribute("path", "deposit");
				request.getRequestDispatcher("/WEB-INF/jsp/user/transactionForm.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/controller/user/home?error=" + e.getMessage());
			}
			break;
		}

		case "/user/profile": {
			try {
				int userId = (int) session.getAttribute("userId");
				Customer profile = (Customer) employeeHandler.getCustomer(userId);
				request.setAttribute("profile", profile);
				request.getRequestDispatcher("/WEB-INF/jsp/user/profile.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/controller/user/home?error=" + e.getMessage());
			}
			break;
		}

		case "/admin/addAccount": {
			try {
				Map<Integer, Branch> branches = adminHandler.getBranches(ActiveStatus.ACTIVE);
				request.setAttribute("branches", branches);
				request.getRequestDispatcher("/WEB-INF/jsp/employee/accountForm.jsp").forward(request, response);
			} catch (CustomException e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/controller/admin/home?error=" + e.getMessage());
			}
			break;
		}

		case "/admin/manageAccount": {
			try {
				int accountNo = Utils.parseInt(request.getParameter("accountNo"));
				int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
				int months = Math.abs(Utils.parseInt(request.getParameter("months")));
				request.setAttribute("months", months);
				request.setAttribute("account", adminHandler.getAccount(accountNo));
				request.setAttribute("totalPages", adminHandler.getTransactionPageCount(accountNo, 1, 10));
				request.setAttribute("page", pageNo);
				request.setAttribute("transactions", adminHandler.getTransactions(accountNo, months, pageNo, 10));
				request.getRequestDispatcher("/WEB-INF/jsp/employee/accountDetails.jsp").forward(request, response);
			} catch (InvalidValueException | CustomException e) {
				e.printStackTrace();
				request.setAttribute("error", e.getMessage());
				response.sendRedirect(request.getContextPath() + "/controller/admin/home?error=" + e.getMessage());
			}
			break;
		}

		case "/admin/branches": {
			try {
				int branchStatus = Math.abs(Utils.parseInt(request.getParameter("branchStatus")));
				request.setAttribute("status", branchStatus);
				ActiveStatus status = ActiveStatus.values()[branchStatus];
				Map<Integer, Branch> branches = adminHandler.getBranches(status);
				request.setAttribute("branches", branches);
				request.getRequestDispatcher("/WEB-INF/jsp/admin/branches.jsp").forward(request, response);
			} catch (CustomException e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/controller/admin/home?error=" + e.getMessage());
			}
			break;
		}

		case "/admin/addBranch": {
			request.getRequestDispatcher("/WEB-INF/jsp/admin/branchForm.jsp").forward(request, response);
			break;
		}

		case "/admin/branch": {
			try {
				int branchId = Utils.parseInt(request.getParameter("id"));
				request.setAttribute("branchId", branchId);
				Branch branch = adminHandler.getBranch(branchId);
				request.setAttribute("branch", branch);
				request.getRequestDispatcher("/WEB-INF/jsp/admin/viewBranch.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/controller/admin/branches?error=" + e.getMessage());
			}
			break;
		}

		case "/admin/users": {
			try {
				int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
				request.setAttribute("page", pageNo);
				int uType = Math.abs(Utils.parseInt(request.getParameter("userType")));
				if (uType == 0) {
					request.setAttribute("totalPages", adminHandler.getAllCustomerPageCount(10));
					request.setAttribute("customers", adminHandler.getAllCustomers(pageNo, 10));
				} else {
					request.setAttribute("totalPages", adminHandler.getAllEmployeesPageCount(10));
					request.setAttribute("employees", adminHandler.getAllEmployees(pageNo, 10));
				}
				request.setAttribute("userType", uType);
				request.getRequestDispatcher("/WEB-INF/jsp/employee/users.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.sendRedirect("/controller/admin/home?message=" + e.getMessage());
			}
			break;
		}

		case "/employee/addUser":
		case "/admin/addUser": {
			try {
				Map<Integer, Branch> branches = adminHandler.getBranches(ActiveStatus.ACTIVE);
				request.setAttribute("branches", branches);
				request.getRequestDispatcher("/WEB-INF/jsp/employee/userForm.jsp").forward(request, response);
			} catch (CustomException e) {
				e.printStackTrace();
				UserType userType = (UserType) session.getAttribute("userType");
				String role = userType.name().toLowerCase();
				response.sendRedirect(
						request.getContextPath() + "/controller/" + role + "/users?error=" + e.getMessage());
			}
			break;
		}

		case "/admin/modifyUser": {
			try {
				int userId = Utils.parseInt(request.getParameter("userId"));
				int type = Integer.parseInt(request.getParameter("userType"));
				if (UserType.values()[type] == UserType.USER) {
					String getAccounts = request.getParameter("getAllAccounts");
					if (getAccounts != null && getAccounts.equals("1")) {
						request.setAttribute("accounts", customerHandler.getAccounts(userId));
					}
					request.setAttribute("user", adminHandler.getCustomer(userId));
				} else {
					request.setAttribute("user", adminHandler.getEmployee(userId));
					Map<Integer, Branch> branches = adminHandler.getBranches(ActiveStatus.ACTIVE);
					request.setAttribute("branches", branches);
				}
				request.getRequestDispatcher("/WEB-INF/jsp/employee/userForm.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/controller/admin/users?error=" + e.getMessage());
			}
			break;
		}

		case "/employee/profile":
		case "/admin/profile": {
			try {
				int userId = (int) session.getAttribute("userId");
				Employee profile = (Employee) adminHandler.getEmployee(userId);
				request.setAttribute("profile", profile);
				Branch branch = adminHandler.getBranch(profile.getBranchId());
				request.setAttribute("branch", branch);
				request.getRequestDispatcher("/WEB-INF/jsp/user/profile.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				UserType userType = (UserType) session.getAttribute("userType");
				response.sendRedirect(request.getContextPath() + "/controller/" + userType.name().toLowerCase()
						+ "/home?error=" + e.getMessage());
			}
			break;
		}

		case "/employee/addAccount": {
			request.getRequestDispatcher("/WEB-INF/jsp/employee/accountForm.jsp").forward(request, response);
			break;
		}

		case "/employee/manageAccount": {
			try {
				int accountNo = Utils.parseInt(request.getParameter("accountNo"));
				int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
				int months = Math.abs(Utils.parseInt(request.getParameter("months")));
				request.setAttribute("months", months);
				request.setAttribute("account", employeeHandler.getAccount(accountNo));
				request.setAttribute("totalPages", employeeHandler.getTransactionPageCount(accountNo, 1, 10));
				request.setAttribute("page", pageNo);
				request.setAttribute("transactions", employeeHandler.getTransactions(accountNo, months, pageNo, 10));
				request.getRequestDispatcher("/WEB-INF/jsp/employee/accountDetails.jsp").forward(request, response);
			} catch (InvalidValueException | CustomException e) {
				e.printStackTrace();
				request.setAttribute("error", e.getMessage());
				response.sendRedirect(request.getContextPath() + "/controller/employee/home?error=" + e.getMessage());
			}
			break;
		}

		case "/employee/users": {
			try {
				int pageNo = Math.abs(Utils.parseInt(request.getParameter("page")));
				request.setAttribute("page", pageNo);
				request.setAttribute("userType", 0);
				int customerStatus = Math.abs(Utils.parseInt(request.getParameter("status")));
				request.setAttribute("status", customerStatus);
				ActiveStatus status = ActiveStatus.values()[customerStatus];
				int branchId = (int) session.getAttribute("branchId");
				request.setAttribute("totalPages", employeeHandler.getCustomerPageCount(branchId, 10, status));
				request.setAttribute("customers", employeeHandler.getCustomers(branchId, pageNo, 10, status));
				request.getRequestDispatcher("/WEB-INF/jsp/employee/users.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/controller/employee/home?error=" + e.getMessage());
			}
			break;
		}

		case "/employee/modifyUser": {
			try {
				int userId = Utils.parseInt(request.getParameter("userId"));
				String getAccounts = request.getParameter("getAllAccounts");
				if (getAccounts != null && getAccounts.equals("1")) {
					request.setAttribute("accounts", customerHandler.getAccounts(userId));
				}
				request.setAttribute("user", employeeHandler.getCustomer(userId));
				request.getRequestDispatcher("/WEB-INF/jsp/employee/userForm.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/controller/employee/users?error=" + e.getMessage());
			}
			break;
		}

		case "/employee/fundTransfer": {
			try {
				int branchId = (int) session.getAttribute("branchId");
				Set<Integer> accounts = employeeHandler.getBranchAccounts(branchId).keySet();
				request.setAttribute("accounts", accounts);
				request.getRequestDispatcher("/WEB-INF/jsp/employee/transactionForm.jsp").forward(request, response);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/controller/employee/home?error=" + e.getMessage());
			}
			break;
		}

		case "/error": {
			request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
			break;
		}

		case "/logout": {
			if (session != null) {
				Audit audit = new Audit();
				audit.setTime(System.currentTimeMillis());
				audit.setAction("logout");
				int userId = (int) session.getAttribute("userId");
				audit.setModifiedBy(userId);
				audit.setDescription("logged out successfully");
				audit.setStatus(true);
				audit.setTargetId(userId);
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
				session.removeAttribute("userId");
				session.removeAttribute("branchId");
				session.removeAttribute("userName");
				session.invalidate();
			}
			response.sendRedirect(request.getContextPath() + "/controller/login");
			break;
		}

		default: {
			response.sendError(404, "Invalid URL!");
		}
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		System.out.println(path + " post");
		HttpSession session = request.getSession(false);
		switch (path) {

		case "/login": {
			int userId = Integer.parseInt(request.getParameter("userId"));
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("login");
			audit.setModifiedBy(userId);
			try {
				String password = request.getParameter("password");
				User user = sessionHandler.authenticate(userId, password);
				HttpSession httpSession = request.getSession();
				httpSession.setAttribute("userId", user.getUserId());
				httpSession.setAttribute("userName", user.getName());
				httpSession.setAttribute("userType", user.getType());
				switch (user.getType()) {
				case ADMIN: {
					Employee employee = (Employee) user;
					httpSession.setAttribute("branchId", employee.getBranchId());
					response.sendRedirect(request.getContextPath() + "/controller/admin/home");
					break;
				}
				case EMPLOYEE: {
					Employee employee = (Employee) user;
					httpSession.setAttribute("branchId", employee.getBranchId());
					response.sendRedirect(request.getContextPath() + "/controller/employee/home");
					break;
				}
				case USER: {
					response.sendRedirect(request.getContextPath() + "/controller/user/home");
					break;
				}
				}
				audit.setDescription("logged in successfully");
				audit.setStatus(true);
				audit.setTargetId(userId);
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(userId);
				request.setAttribute("error", e.getMessage());
				request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/user/changeMpin": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("account");
			audit.setModifiedBy(sessionUserId);
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			try {
				int id = (int) session.getAttribute("userId");
				String currentMin = request.getParameter("currentMpin");
				String newMpin = request.getParameter("newMpin");
				String confirmMpin = request.getParameter("confirmMpin");
				if (!newMpin.equals(confirmMpin)) {
					throw new InvalidValueException("Please re-enter new Mpin correctly!");
				}
				customerHandler.changeMpin(id, accountNo, currentMin, newMpin, sessionUserId, modifiedOn);
				String message = "MPIN Updated Successful!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(accountNo);
				String redirectUrl = request.getContextPath() + "/controller/user/account?accountNo=" + accountNo;
				response.sendRedirect(redirectUrl + "&message=" + message);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				audit.setDescription("MPIN Updation Failed");
				audit.setStatus(false);
				audit.setTargetId(accountNo);
				String redirectUrl = request.getContextPath() + "/controller/user/account?accountNo=" + accountNo;
				response.sendRedirect(redirectUrl + "&error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/user/changePassword": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("user");
			audit.setModifiedBy(sessionUserId);
			try {
				String currentPassword = request.getParameter("currentPassword");
				String newPassword = request.getParameter("newPassword");
				String confirmPassword = request.getParameter("confirmPassword");
				if (!newPassword.equals(confirmPassword)) {
					throw new InvalidValueException("Please re-enter new Password correctly!");
				}
				customerHandler.changePassword(sessionUserId, currentPassword, newPassword, sessionUserId, modifiedOn);
				String message = "Password Updated Successful!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(sessionUserId);
				String redirectUrl = request.getContextPath() + "/controller/user/profile";
				response.sendRedirect(redirectUrl + "?message=" + message);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(sessionUserId);
				String redirectUrl = request.getContextPath() + "/controller/user/profile";
				response.sendRedirect(redirectUrl + "?error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/user/setPrimary": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("account");
			audit.setModifiedBy(sessionUserId);
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			try {
				int id = (int) session.getAttribute("userId");
				customerHandler.setPrimaryAccount(id, accountNo, sessionUserId, modifiedOn);
				String message = "Primary Account Updated Successful!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(accountNo);
				String redirectUrl = request.getContextPath() + "/controller/user/account?accountNo=" + accountNo;
				response.sendRedirect(redirectUrl + "&message=" + message);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(accountNo);
				String redirectUrl = request.getContextPath() + "/controller/user/account?accountNo=" + accountNo;
				response.sendRedirect(redirectUrl + "&error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/user/moneyTransfer": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("moneyTransfer");
			audit.setModifiedBy(sessionUserId);
			int accountNo = Utils.parseInt(request.getParameter("senderAccountNo"));
			int benificiaryAccountNo = Utils.parseInt(request.getParameter("beneficiaryAccNo"));
			try {
				int userId = (int) session.getAttribute("userId");
				String mpin = request.getParameter("mpin");
				Double amount = Utils.parseDouble(request.getParameter("amount"));
				String ifsc = request.getParameter("beneficiaryIfsc");
				String description = request.getParameter("description");
				customerHandler.moneyTransfer(userId, mpin, accountNo, benificiaryAccountNo, amount, ifsc, description,
						sessionUserId, modifiedOn);
				String message = "Transaction Successful!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(benificiaryAccountNo);
				String redirectUrl = request.getContextPath() + "/controller/user/moneyTransfer";
				response.sendRedirect(redirectUrl + "?message=" + message);
			} catch (CustomException | InvalidValueException | InsufficientFundException
					| InvalidOperationException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(benificiaryAccountNo);
				String redirectUrl = request.getContextPath() + "/controller/user/moneyTransfer";
				response.sendRedirect(redirectUrl + "?error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/user/withdrawl": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("withdrawl");
			audit.setModifiedBy(sessionUserId);
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			try {
				int userId = (int) session.getAttribute("userId");
				String mpin = request.getParameter("mpin");
				Double amount = Utils.parseDouble(request.getParameter("amount"));
				String description = request.getParameter("description");
				customerHandler.withdrawl(userId, mpin, accountNo, amount, description, sessionUserId, modifiedOn);
				String message = "Withdraw Successful!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(accountNo);
				String redirectUrl = request.getContextPath() + "/controller/user/withdrawl";
				response.sendRedirect(redirectUrl + "?message=" + message);
			} catch (CustomException | InvalidValueException | InsufficientFundException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(accountNo);
				String redirectUrl = request.getContextPath() + "/controller/user/withdrawl";
				response.sendRedirect(redirectUrl + "?error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/user/deposit": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("deposit");
			audit.setModifiedBy(sessionUserId);
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			try {
				int userId = (int) session.getAttribute("userId");
				String mpin = request.getParameter("mpin");
				Double amount = Utils.parseDouble(request.getParameter("amount"));
				String description = request.getParameter("description");
				customerHandler.deposit(userId, mpin, accountNo, amount, description, sessionUserId, modifiedOn);
				String message = "Deposit Successful!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(accountNo);
				String redirectUrl = request.getContextPath() + "/controller/user/deposit";
				response.sendRedirect(redirectUrl + "?message=" + message);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(accountNo);
				String redirectUrl = request.getContextPath() + "/controller/user/deposit";
				response.sendRedirect(redirectUrl + "?error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/admin/manageAccount": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("account");
			audit.setModifiedBy(sessionUserId);
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			try {
				String activate = request.getParameter("activate");
				ActiveStatus status = ActiveStatus.values()[Integer.parseInt(activate)];
				adminHandler.setAccountStatus(accountNo, status, sessionUserId, modifiedOn);
				String message = "Account Status Updated!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(accountNo);
				String redirectUrl = request.getContextPath() + "/controller/admin/manageAccount?accountNo="
						+ accountNo;
				response.sendRedirect(redirectUrl + "&message=" + message);
			} catch (CustomException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(accountNo);
				String redirectUrl = request.getContextPath() + "/controller/admin/manageAccount?accountNo="
						+ accountNo;
				response.sendRedirect(redirectUrl + "&error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/admin/addAccount": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("account");
			audit.setModifiedBy(sessionUserId);
			try {
				int customerId = Utils.parseInt(request.getParameter("customerId"));
				int branchId = Utils.parseInt(request.getParameter("branchId"));
				int accountNo = adminHandler.createAccount(customerId, branchId, sessionUserId, modifiedOn);
				String message = "Account Created Successfully!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(accountNo);
				response.sendRedirect(request.getContextPath() + "/controller/admin/addAccount?message=" + message);
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				response.sendRedirect(
						request.getContextPath() + "/controller/admin/addAccount?error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/admin/addBranch": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("branch");
			audit.setModifiedBy(sessionUserId);
			try {
				Branch branch = new Branch();
				branch.setIfsc(request.getParameter("ifsc"));
				branch.setLocation(request.getParameter("location"));
				branch.setCity(request.getParameter("city"));
				branch.setState(request.getParameter("state"));
				branch.setModifiedBy(sessionUserId);
				branch.setModifiedOn(modifiedOn);
				int branchId = adminHandler.addBranch(branch);
				audit.setTargetId(branchId);
				audit.setStatus(true);
				audit.setDescription("Created new Branch");
				response.sendRedirect(request.getContextPath() + "/controller/admin/branches");
			} catch (CustomException e) {
				audit.setStatus(false);
				audit.setDescription(e.getMessage());
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/controller/admin/addBranch?error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/admin/branch": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("branch");
			audit.setModifiedBy(sessionUserId);
			int branchId = Utils.parseInt(request.getParameter("branchId"));
			try {
				String activate = request.getParameter("activate");
				if (activate.equals("0")) {
					adminHandler.removeBranch(branchId, sessionUserId, modifiedOn);
					String message = "Removed Branch!";
					audit.setDescription("Branch removed");
					audit.setStatus(true);
					audit.setTargetId(branchId);
					response.sendRedirect(request.getContextPath() + "/controller/admin/branch?id=" + branchId
							+ "&message=" + message);
				} else {
					response.sendRedirect(request.getContextPath() + "/controller/admin/branch?id=" + branchId);
				}
			} catch (CustomException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(branchId);
				response.sendRedirect(request.getContextPath() + "/controller/admin/branch?id=" + branchId + "&error="
						+ e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/admin/manageUser": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("user");
			audit.setModifiedBy(sessionUserId);
			int userId = Utils.parseInt(request.getParameter("userId"));
			String type = request.getParameter("userType");
			try {
				String activate = request.getParameter("activate");
				Utils.checkNull(activate);
				ActiveStatus status = ActiveStatus.values()[Integer.parseInt(activate)];
				if (type != null && UserType.valueOf(type) == UserType.USER) {
					adminHandler.setCustomerStatus(userId, status, sessionUserId, modifiedOn);
				} else {
					adminHandler.setEmployeeStatus(userId, status, sessionUserId, modifiedOn);
				}
				String message = "User Status Updated!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(userId);
				response.sendRedirect(request.getContextPath() + "/controller/admin/modifyUser?userId=" + userId
						+ "&userType=" + UserType.valueOf(type).ordinal() + "&message=" + message);
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(userId);
				response.sendRedirect(request.getContextPath() + "/controller/admin/modifyUser?userId=" + userId
						+ "&userType=" + UserType.valueOf(type).ordinal() + "&error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/admin/addUser": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("user");
			audit.setModifiedBy(sessionUserId);
			int userId = 0;
			int type = Integer.parseInt(request.getParameter("userType"));
			try {
				if (type == 0) {
					int branchId = Integer.parseInt(request.getParameter("branchId"));
					Customer customer = getCustomerFormData(request, response);
					customer.setModifiedOn(modifiedOn);
					userId = adminHandler.addCustomer(customer);
					adminHandler.createAccount(userId, branchId, sessionUserId, modifiedOn);
				} else {
					Employee employee = getEmployeeFormData(request, response);
					employee.setModifiedOn(modifiedOn);
					userId = adminHandler.addEmployee(employee);
				}
				String message = "User Created Successfully!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(userId);
				String redirectUrl = request.getContextPath() + "/controller/admin/modifyUser";
				response.sendRedirect(redirectUrl + "?userId=" + userId + "&userType=" + type + "&message=" + message);
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(userId);
				String redirectUrl = request.getContextPath() + "/controller/admin/modifyUser";
				response.sendRedirect(
						redirectUrl + "?userId=" + userId + "&userType=" + type + "&error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/admin/modifyUser": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("user");
			audit.setModifiedBy(sessionUserId);
			int userId = Integer.parseInt(request.getParameter("userId"));
			int type = Integer.parseInt(request.getParameter("userType"));
			try {
				if (type == 0) {
					Customer customer = getCustomerFormData(request, response);
					customer.setModifiedOn(modifiedOn);
					customer.setUserId(userId);
					adminHandler.updateCustomer(customer);
				} else {
					Employee employee = getEmployeeFormData(request, response);
					employee.setModifiedOn(modifiedOn);
					employee.setUserId(userId);
					adminHandler.updateEmployee(employee);
				}
				String message = "User Data Updated Successfully!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(userId);
				response.sendRedirect(request.getContextPath() + "/controller/admin/modifyUser?userId=" + userId
						+ "&userType=" + type + "&message=" + message);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(userId);
				response.sendRedirect(request.getContextPath() + "/controller/admin/modifyUser?userId=" + userId
						+ "&userType=" + type + "&error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/admin/changePassword": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("user");
			audit.setModifiedBy(sessionUserId);
			try {
				String currentPassword = request.getParameter("currentPassword");
				String newPassword = request.getParameter("newPassword");
				String confirmPassword = request.getParameter("confirmPassword");
				if (!newPassword.equals(confirmPassword)) {
					throw new InvalidValueException("Please re-enter new Password correctly!");
				}
				adminHandler.changePassword(sessionUserId, currentPassword, newPassword, sessionUserId, modifiedOn);
				String message = "Password Updated Successful!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(sessionUserId);
				String redirectUrl = request.getContextPath() + "/controller/admin/profile";
				response.sendRedirect(redirectUrl + "?message=" + message);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(sessionUserId);
				String redirectUrl = request.getContextPath() + "/controller/admin/profile";
				response.sendRedirect(redirectUrl + "?error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/employee/addAccount": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("account");
			audit.setModifiedBy(sessionUserId);
			try {
				int customerId = Utils.parseInt(request.getParameter("customerId"));
				int branchId = (int) session.getAttribute("branchId");
				int accountNo = employeeHandler.createAccount(customerId, branchId, sessionUserId, modifiedOn);
				String message = "Account Created Successfully!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(accountNo);
				response.sendRedirect(request.getContextPath() + "/controller/employee/addAccount?message=" + message);
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				response.sendRedirect(
						request.getContextPath() + "/controller/employee/addAccount?error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/employee/manageAccount": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("account");
			audit.setModifiedBy(sessionUserId);
			try {
				String activate = request.getParameter("activate");
				ActiveStatus status = ActiveStatus.values()[Integer.parseInt(activate)];
				employeeHandler.setAccountStatus(accountNo, status, sessionUserId, modifiedOn);
				String message = "Account Status Updated!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(accountNo);
				String redirectUrl = request.getContextPath() + "/controller/employee/manageAccount?accountNo="
						+ accountNo;
				response.sendRedirect(redirectUrl + "&message=" + message);
			} catch (CustomException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(accountNo);
				String redirectUrl = request.getContextPath() + "/controller/employee/manageAccount?accountNo="
						+ accountNo;
				response.sendRedirect(redirectUrl + "&error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/employee/addUser": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("user");
			audit.setModifiedBy(sessionUserId);
			int userId = 0;
			try {
				Customer customer = getCustomerFormData(request, response);
				customer.setModifiedOn(modifiedOn);
				userId = employeeHandler.addCustomer(customer);
				int branchId = (int) session.getAttribute("branchId");
				employeeHandler.createAccount(userId, branchId, sessionUserId, modifiedOn);
				String message = "User Created Successfully!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(userId);
				String redirectUrl = request.getContextPath() + "/controller/employee/modifyUser";
				response.sendRedirect(redirectUrl + "?message=" + message);
			} catch (CustomException | InvalidValueException | InvalidOperationException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(userId);
				String redirectUrl = request.getContextPath() + "/controller/employee/modifyUser";
				response.sendRedirect(redirectUrl + "?userId=" + userId + "&error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/employee/manageUser": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("user");
			audit.setModifiedBy(sessionUserId);
			int userId = Utils.parseInt(request.getParameter("userId"));
			try {
				String activate = request.getParameter("activate");
				ActiveStatus status = ActiveStatus.values()[Integer.parseInt(activate)];
				employeeHandler.setCustomerStatus(userId, status, sessionUserId, modifiedOn);
				String message = "User Status Updated!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(userId);
				response.sendRedirect(request.getContextPath() + "/controller/employee/modifyUser?userId=" + userId
						+ "&message=" + message);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(userId);
				response.sendRedirect(request.getContextPath() + "/controller/employee/modifyUser?userId=" + userId
						+ "&error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/employee/modifyUser": {
			int userId = Integer.parseInt(request.getParameter("userId"));
			long modifiedOn = System.currentTimeMillis();
			int sessionUserId = (int) session.getAttribute("userId");
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("user");
			audit.setModifiedBy(sessionUserId);
			try {
				Customer customer = getCustomerFormData(request, response);
				customer.setModifiedOn(modifiedOn);
				customer.setUserId(userId);
				employeeHandler.updateCustomer(customer);
				String message = "User Data Updated!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(userId);
				request.setAttribute("message", message);
				response.sendRedirect(request.getContextPath() + "/controller/employee/modifyUser?userId=" + userId
						+ "&message=" + message);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(userId);
				response.sendRedirect(request.getContextPath() + "/controller/employee/modifyUser?userId=" + userId
						+ "&error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/employee/fundTransfer": {
			long modifiedOn = System.currentTimeMillis();
			int sessionUserId = (int) session.getAttribute("userId");
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("deposit");
			audit.setModifiedBy(sessionUserId);
			int accountNo = Utils.parseInt(request.getParameter("accountNo"));
			try {
				double amount = Utils.parseDouble(request.getParameter("amount"));
				String description = request.getParameter("description");
				employeeHandler.deposit(accountNo, amount, description, sessionUserId, modifiedOn);
				String message = "Amount Deposited!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(accountNo);
				request.setAttribute("message", message);
				response.sendRedirect(
						request.getContextPath() + "/controller/employee/fundTransfer?message=" + message);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(accountNo);
				response.sendRedirect(
						request.getContextPath() + "/controller/employee/fundTransfer?error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		case "/employee/changePassword": {
			int sessionUserId = (int) session.getAttribute("userId");
			long modifiedOn = System.currentTimeMillis();
			Audit audit = new Audit();
			audit.setTime(modifiedOn);
			audit.setAction("user");
			audit.setModifiedBy(sessionUserId);
			try {
				String currentPassword = request.getParameter("currentPassword");
				String newPassword = request.getParameter("newPassword");
				String confirmPassword = request.getParameter("confirmPassword");
				if (!newPassword.equals(confirmPassword)) {
					throw new InvalidValueException("Please re-enter new Password correctly!");
				}
				employeeHandler.changePassword(sessionUserId, currentPassword, newPassword, sessionUserId, modifiedOn);
				String message = "Password Updated Successful!";
				audit.setDescription(message);
				audit.setStatus(true);
				audit.setTargetId(sessionUserId);
				String redirectUrl = request.getContextPath() + "/controller/employee/profile";
				response.sendRedirect(redirectUrl + "?message=" + message);
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
				audit.setDescription(e.getMessage());
				audit.setStatus(false);
				audit.setTargetId(sessionUserId);
				String redirectUrl = request.getContextPath() + "/controller/employee/profile";
				response.sendRedirect(redirectUrl + "?error=" + e.getMessage());
			} finally {
				try {
					sessionHandler.audit(audit);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
			break;
		}

		default: {
			response.sendError(404, "Invalid URL!");
			break;
		}
		}
	}

	private Employee getEmployeeFormData(HttpServletRequest request, HttpServletResponse response)
			throws InvalidValueException {
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
		HttpSession session = request.getSession();
		employee.setModifiedBy((int) session.getAttribute("userId"));
		return employee;
	}

	private Customer getCustomerFormData(HttpServletRequest request, HttpServletResponse response)
			throws InvalidValueException {
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
		HttpSession session = request.getSession();
		customer.setModifiedBy((int) session.getAttribute("userId"));
		return customer;
	}
}