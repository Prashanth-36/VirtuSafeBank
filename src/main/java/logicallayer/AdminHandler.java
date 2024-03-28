package logicallayer;

import java.util.Map;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import model.Branch;
import model.Customer;
import model.Employee;
import persistentdao.BranchDao;
import persistentdao.EmployeeDao;
import persistentlayer.BranchManager;
import persistentlayer.EmployeeManager;
import utility.ActiveStatus;
import utility.Utils;

public class AdminHandler extends EmployeeHandler {

	static EmployeeManager employeeManager = new EmployeeDao();

	static BranchManager branchManager = new BranchDao();

	public void addEmployee(Employee employee) throws CustomException, InvalidValueException {
		Utils.checkNull(employee);
		employeeManager.addEmployee(employee);
	}

	public Map<Integer, Employee> getEmployees(int branchId, int pageNo, int limit, ActiveStatus status)
			throws CustomException, InvalidValueException {
		int offset = Utils.pagination(pageNo, limit);
		return employeeManager.getEmployees(branchId, offset, limit, status);
	}

	public int getEmployeesPageCount(int branchId, int limit, ActiveStatus status)
			throws CustomException, InvalidValueException {
		if (!branchManager.isValidBranch(branchId)) {
			throw new InvalidValueException("Invalid Branch id");
		}
		Utils.checkRange(5, limit, 50, "Limit should be within 5 to 50.");
		int totalEmployees = employeeManager.getEmployeesCount(branchId, status);
		int pages = (int) Math.ceil((double) totalEmployees / limit);
		return pages;
	}

	public Map<Integer, Employee> getAllEmployees(int pageNo, int limit) throws CustomException, InvalidValueException {
		int offset = Utils.pagination(pageNo, limit);
		return employeeManager.getAllEmployees(offset, limit);
	}

	public int getAllEmployeesPageCount(int limit) throws CustomException, InvalidValueException {
		Utils.checkRange(5, limit, 50, "Limit should be within 5 to 50.");
		int totalEmployees = employeeManager.getAllEmployeesCount();
		int pages = (int) Math.ceil((double) totalEmployees / limit);
		return pages;
	}

	public void removeEmployee(int id) throws CustomException, InvalidOperationException {
		if (id == 1) {
			throw new InvalidOperationException("Can't remove admin!");
		}
		employeeManager.removeEmployee(id);
	}

	public void addBranch(Branch branch) throws CustomException {
		branchManager.addBranch(branch);
	}

	public Branch getBranch(int branchId) throws CustomException, InvalidValueException {
		return branchManager.getBranch(branchId);
	}

	public Map<Integer, Branch> getBranches(ActiveStatus status) throws CustomException {
		return branchManager.getBranches(status);
	}

	public void removeBranch(int branchId) throws CustomException {
		branchManager.removeBranch(branchId);
	}

	public Employee getEmployee(int id) throws CustomException, InvalidValueException {
		return employeeManager.getEmployee(id);
	}

	public void updateEmployee(Employee employee) throws CustomException, InvalidValueException {
		employeeManager.updateEmployee(employee);
	}

	public void setEmployeeStatus(int employeeId, ActiveStatus status) throws CustomException, InvalidValueException {
		employeeManager.getEmployee(employeeId); // validate existing employee
		employeeManager.setEmployeeStatus(employeeId, status);
	}

	public Map<Integer, Customer> getAllCustomers(int pageNo, int limit) throws CustomException, InvalidValueException {
		int offset = Utils.pagination(pageNo, limit);
		return customerManager.getAllCustomers(offset, limit);
	}

	public int getAllCustomerPageCount(int limit) throws CustomException, InvalidValueException {
		Utils.checkRange(5, limit, 50, "Limit should be within 5 to 50.");
		int totalEmployees = customerManager.getAllCustomerCount();
		int pages = (int) Math.ceil((double) totalEmployees / limit);
		return pages;
	}

}