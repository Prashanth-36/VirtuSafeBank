package persistentlayer;

import java.util.List;
import java.util.Map;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.Customer;
import utility.ActiveStatus;

public interface CustomerManager {

	void addCustomer(Customer customer) throws CustomException, InvalidValueException;

	void removeCustomer(int customerId) throws CustomException;

	Customer getCustomer(int id) throws CustomException, InvalidValueException;

	int getCustomerId(long aadhaarNo) throws CustomException;

	Map<Integer, Customer> getCustomers(int branchId, int offset, int limit, ActiveStatus status)
			throws CustomException;

	int getCustomersCount(int branchId, ActiveStatus status) throws CustomException;

	List<Integer> getCustomerBranches(int customerId) throws CustomException;

	void setCustomerStatus(int customerId, ActiveStatus status) throws CustomException;
}
