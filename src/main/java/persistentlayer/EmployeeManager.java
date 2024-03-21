package persistentlayer;

import java.util.Map;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.Employee;
import utility.ActiveStatus;

public interface EmployeeManager {

	void addEmployee(Employee employee) throws CustomException, InvalidValueException;

	Employee getEmployee(int id) throws CustomException, InvalidValueException;

	void removeEmployee(int id) throws CustomException;

	Map<Integer, Employee> getEmployees(int branchId, int offset, int limit, ActiveStatus status)
			throws CustomException;

	int getEmployeesCount(int branchId, ActiveStatus status) throws CustomException;

	void setEmployeeStatus(int employeeId, ActiveStatus status) throws CustomException;

}
