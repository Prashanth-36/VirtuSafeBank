package controller;

import java.beans.IntrospectionException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import customexceptions.CustomException;
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
import utility.Gender;
import utility.UserType;
import utility.Validate;

@WebServlet("/api/*")
public class Api extends HttpServlet {
	private static final long serialVersionUID = 1L;

	static AdminHandler adminHandler = new AdminHandler();
	static SessionHandler sessionHandler = new SessionHandler();
	static EmployeeHandler employeeHandler = new EmployeeHandler();
	static CustomerHandler customerHandler = new CustomerHandler();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		System.out.println(path + "  get");
		String pathParams[] = path.split("/");

		if (path.startsWith("/users")) {
			if (pathParams.length == 3) {
				int userId = Integer.parseInt(pathParams[2]);
				try {
					Customer customer = employeeHandler.getCustomer(userId);
					JSONObject data = new JSONObject(customer);
					response.getWriter().write(data.toString());
				} catch (InvocationTargetException | IllegalAccessException | IntrospectionException e) {
					e.printStackTrace();
				} catch (CustomException | InvalidValueException e) {
					e.printStackTrace();
				}
			}
		} else if (path.startsWith("/employees")) {
			if (pathParams.length == 3) {
				int userId = Integer.parseInt(pathParams[2]);
				try {
					Employee employee = adminHandler.getEmployee(userId);
					JSONObject data = new JSONObject(employee);
					response.getWriter().write(data.toString());
				} catch (InvocationTargetException | IllegalAccessException | IntrospectionException e) {
					e.printStackTrace();
				} catch (CustomException | InvalidValueException e) {
					e.printStackTrace();
				}
			}
		} else if (path.startsWith("/accounts")) {
			if (pathParams.length == 3) {
				int accountNo = Integer.parseInt(pathParams[2]);
				try {
					Account account = adminHandler.getAccount(accountNo);
					JSONObject data = new JSONObject(account);
					response.getWriter().write(data.toString());
				} catch (InvocationTargetException | IllegalAccessException | IntrospectionException e) {
					e.printStackTrace();
				} catch (CustomException | InvalidValueException e) {
					e.printStackTrace();
				}
			}
		} else if (path.startsWith("/branches")) {
			if (pathParams.length == 3) {
				int branchId = Integer.parseInt(pathParams[2]);
				try {
					Branch branch = adminHandler.getBranch(branchId);
					JSONObject data = new JSONObject(branch);
					response.getWriter().write(data.toString());
				} catch (InvocationTargetException | IllegalAccessException | IntrospectionException e) {
					e.printStackTrace();
				} catch (CustomException | InvalidValueException e) {
					e.printStackTrace();
				}
			}
		} else {
			response.sendError(404, "Invalid URL!");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		System.out.println(path + "  post");

		if (path.startsWith("/users")) {
			try {
				String jsonString = getJsonString(request);
				JSONObject data = new JSONObject(jsonString);
				System.out.println(data);
				Customer customer = new Customer();
				customer.setName(data.getString("name"));
				customer.setDob(data.getLong("dob"));
				customer.setGender(Gender.valueOf(data.getString("gender")));
				String number = data.getString("number");
				Validate.mobile(number);
				customer.setNumber(Long.parseLong(number));
				String email = data.getString("email");
				Validate.email(email);
				customer.setEmail(email);
				customer.setType(UserType.valueOf(data.getString("type")));
				customer.setPassword(data.getString("password"));
				String aadhaar = data.getString("aadhaarNo");
				Validate.aadhaar(aadhaar);
				customer.setAadhaarNo(Long.parseLong(aadhaar));
				String pan = data.getString("panNo");
				Validate.pan(pan);
				customer.setPanNo(pan);
				customer.setLocation(data.getString("location"));
				customer.setCity(data.getString("city"));
				customer.setState(data.getString("state"));
				customer.setModifiedBy(data.getInt("modifiedBy"));
				customer.setModifiedOn(data.getLong("modifiedOn"));
				System.out.println(customer);

				employeeHandler.addCustomer(customer);
				response.getWriter().write("{\"message\":\"Customer Added\"}");
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
			} catch (InvalidOperationException e) {
				e.printStackTrace();
			}
		} else {
			response.sendError(404, "Invalid URL!");
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		System.out.println(path + "  delete");
		String pathParams[] = path.split("/");

		if (path.startsWith("/users")) {
			if (pathParams.length == 3) {
				try {
					int userId = Integer.parseInt(pathParams[2]);
					employeeHandler.removeCustomer(userId, 1);
					response.getWriter().write("{\"message\":\"Customer Removed\"}");
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
		} else if (path.startsWith("/employees")) {
			if (pathParams.length == 3) {
				try {
					int userId = Integer.parseInt(pathParams[2]);
					adminHandler.removeEmployee(userId, 1);
					response.getWriter().write("{\"message\":\"Employee Removed\"}");
				} catch (CustomException | InvalidOperationException e) {
					e.printStackTrace();
				}
			}
		} else if (path.startsWith("/accounts")) {
			if (pathParams.length == 3) {
				try {
					int accountNo = Integer.parseInt(pathParams[2]);
					adminHandler.deleteAccount(accountNo, 1);
					response.getWriter().write("{\"message\":\"Account Removed\"}");
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
		} else if (path.startsWith("/branches")) {
			if (pathParams.length == 3) {
				int branchId = Integer.parseInt(pathParams[2]);
				try {
					adminHandler.removeBranch(branchId, 1, 1);
				} catch (CustomException e) {
					e.printStackTrace();
				}
			}
		} else {
			response.sendError(404, "Invalid URL!");
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		System.out.println(path + "  put");

		if (path.startsWith("/users")) {
			try {
				String jsonString = getJsonString(request);
				JSONObject data = new JSONObject(jsonString);
				System.out.println(data);
				Customer customer = new Customer();
				customer.setUserId(data.getInt("userId"));
				customer.setName(data.getString("name"));
				customer.setDob(data.getLong("dob"));
				customer.setGender(Gender.valueOf(data.getString("gender")));
				String number = data.getString("number");
				Validate.mobile(number);
				customer.setNumber(Long.parseLong(number));
				String email = data.getString("email");
				Validate.email(email);
				customer.setEmail(email);
				customer.setType(UserType.valueOf(data.getString("type")));
				customer.setPassword(data.getString("password"));
				String aadhaar = data.getString("aadhaarNo");
				Validate.aadhaar(aadhaar);
				customer.setAadhaarNo(Long.parseLong(aadhaar));
				String pan = data.getString("panNo");
				Validate.pan(pan);
				customer.setPanNo(pan);
				customer.setLocation(data.getString("location"));
				customer.setCity(data.getString("city"));
				customer.setState(data.getString("state"));
				customer.setModifiedBy(data.getInt("modifiedBy"));
				customer.setModifiedOn(data.getLong("modifiedOn"));
				System.out.println(customer);

				employeeHandler.updateCustomer(customer);
				response.getWriter().write("{\"message\":\"Customer Data Updated\"}");
			} catch (CustomException | InvalidValueException e) {
				e.printStackTrace();
			}
		} else {
			response.sendError(404, "Invalid URL!");
		}
	}

	private String getJsonString(HttpServletRequest request) throws CustomException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
			StringBuffer buffer = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		} catch (IOException e) {
			throw new CustomException("Json data invalid", e);
		}
	}
}