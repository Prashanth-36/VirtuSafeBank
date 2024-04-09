package persistentlayer;

import java.util.List;
import java.util.Map;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.Customer;
import utility.ActiveStatus;

public interface CustomerManager {

	int addCustomer(Customer customer) throws CustomException, InvalidValueException;

	void removeCustomer(int customerId, int modifiedBy) throws CustomException;

	Customer getCustomer(int id) throws CustomException, InvalidValueException;

	int getCustomerId(long aadhaarNo) throws CustomException;

	Map<Integer, Customer> getCustomers(int branchId, int offset, int limit, ActiveStatus status)
			throws CustomException;

	int getCustomersCount(int branchId, ActiveStatus status) throws CustomException;

	List<Integer> getCustomerBranches(int customerId) throws CustomException;

	void setCustomerStatus(int customerId, ActiveStatus status, int modifiedBy, long modifiedOn) throws CustomException;

	Map<Integer, Customer> getAllCustomers(int offset, int limit) throws CustomException;

	int getAllCustomerCount() throws CustomException;

	void updateCustomer(Customer customer) throws CustomException, InvalidValueException;

	void setPassword(int customerId, String currentPassword, String newPassword, int modifiedBy, long modifiedOn)
			throws InvalidValueException, CustomException;
}
