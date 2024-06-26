package persistentlayer;

import java.util.Map;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.Employee;
import utility.ActiveStatus;

public interface EmployeeManager {

	int addEmployee(Employee employee) throws CustomException, InvalidValueException;

	Employee getEmployee(int id) throws CustomException, InvalidValueException;

	void removeEmployee(int id, int modifiedBy) throws CustomException;

	Map<Integer, Employee> getEmployees(int branchId, int offset, int limit, ActiveStatus status)
			throws CustomException;

	int getEmployeesCount(int branchId, ActiveStatus status) throws CustomException;

	void setEmployeeStatus(int employeeId, ActiveStatus status, int modifiedBy, long modifiedOn) throws CustomException;

	int getAllEmployeesCount() throws CustomException;

	Map<Integer, Employee> getAllEmployees(int offset, int limit) throws CustomException;

	void updateEmployee(Employee employee) throws CustomException, InvalidValueException;

	void setPassword(int customerId, String currentPassword, String newPassword, int modifiedBy, long modifiedOn)
			throws InvalidValueException, CustomException;;

}
