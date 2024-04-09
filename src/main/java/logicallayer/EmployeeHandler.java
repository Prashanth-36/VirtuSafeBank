package logicallayer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import model.Account;
import model.Customer;
import model.Transaction;
import persistentdao.AccountDao;
import persistentdao.BranchDao;
import persistentdao.CustomerDao;
import persistentdao.EmployeeDao;
import persistentdao.TransactionDao;
import persistentlayer.AccountManager;
import persistentlayer.BranchManager;
import persistentlayer.CustomerManager;
import persistentlayer.EmployeeManager;
import persistentlayer.TransactionManager;
import utility.ActiveStatus;
import utility.TransactionType;
import utility.Utils;

public class EmployeeHandler {

	static CustomerManager customerManager = new CustomerDao();

	static BranchManager branchManager = new BranchDao();

	static AccountManager accountManager = new AccountDao();

	static TransactionManager transactionManager = new TransactionDao();

	public int addCustomer(Customer customer) throws InvalidOperationException, CustomException, InvalidValueException {
		Utils.checkNull(customer);
		int customerId = customerManager.getCustomerId(customer.getAadhaarNo());
		if (customerId != -1) {
			throw new InvalidOperationException("Customer already exists with id:" + customerId);
		}
		return customerManager.addCustomer(customer);
	}

	public void removeCustomer(int customerId, int modifiedBy) throws CustomException {
		customerManager.removeCustomer(customerId, modifiedBy);
	}

	public Customer getCustomer(int customerId) throws CustomException, InvalidValueException {
		return customerManager.getCustomer(customerId);
	}

	public int getCustomerId(long aadhaarNo) throws CustomException, InvalidValueException {
		return customerManager.getCustomerId(aadhaarNo);
	}

	public Map<Integer, Customer> getCustomers(int branchId, int pageNo, int limit, ActiveStatus status)
			throws CustomException, InvalidValueException {
		int offset = Utils.pagination(pageNo, limit);
		return customerManager.getCustomers(branchId, offset, limit, status);
	}

	public int getCustomerPageCount(int branchId, int limit, ActiveStatus status)
			throws InvalidValueException, CustomException {
		if (!branchManager.isValidBranch(branchId)) {
			throw new InvalidValueException("Invalid Branch id");
		}
		Utils.checkRange(5, limit, 50, "Limit should be within 5 to 50.");
		int totalCustomers = customerManager.getCustomersCount(branchId, status);
		int pageCount = (int) Math.ceil((double) totalCustomers / limit);
		return pageCount;
	}

	public void updateCustomer(Customer customer) throws CustomException, InvalidValueException {
		customerManager.updateCustomer(customer);
	}

	public int createAccount(int customerId, int branchId, int modifiedBy, long modifiedOn)
			throws CustomException, InvalidValueException, InvalidOperationException {
		Account account = new Account();
		customerManager.getCustomer(customerId); // to validate existing customer
		List<Integer> customerBranches = customerManager.getCustomerBranches(customerId);
		if (customerBranches.contains(branchId)) {
			throw new InvalidOperationException("Customer has an existing account in this branch!");
		}
		if (customerBranches.isEmpty()) {
			account.setIsPrimaryAccount(true);
		} else {
			account.setIsPrimaryAccount(false);
		}
		account.setCustomerId(customerId);
		account.setBranchId(branchId);
		account.setModifiedBy(modifiedBy);
		account.setModifiedOn(modifiedOn);
		return accountManager.createAccount(account);
	}

	public void deleteAccount(int accountNo, int modifiedBy) throws CustomException {
		accountManager.deleteAccount(accountNo, modifiedBy);
	}

	public Account getAccount(int accountNo) throws InvalidValueException, CustomException {
		Account account = CustomerHandler.accountCache.get(accountNo);
		if (account != null) {
			return account;
		}
		account = accountManager.getAccount(accountNo);
		CustomerHandler.accountCache.set(accountNo, account);
		return account;
	}

	public Map<Integer, Account> getBranchAccounts(int branchId) throws InvalidValueException, CustomException {
		return accountManager.getBranchAccounts(branchId);
	}

	public int getBranchAccountsPageCount(int branchId, int limit) throws InvalidValueException, CustomException {
		int totalRecords = accountManager.getBranchAccountsCount(branchId, limit);
		int totalPages = (int) Math.ceil((double) totalRecords / limit);
		return totalPages;
	}

	public void setAccountStatus(int accountNo, ActiveStatus status, int modifiedBy, long modifiedOn)
			throws CustomException {
		CustomerHandler.accountCache.remove(accountNo);
		accountManager.setAccountStatus(accountNo, status, modifiedBy, modifiedOn);
	}

	public void setCustomerStatus(int customerId, ActiveStatus status, int modifiedBy, long modifiedOn)
			throws CustomException, InvalidValueException {
		customerManager.getCustomer(customerId); // to validate existing customer
		customerManager.setCustomerStatus(customerId, status, modifiedBy, modifiedOn);
	}

	public int getTransactionPageCount(int accountNo, int months, int limit)
			throws InvalidValueException, CustomException {
		Utils.checkRange(1, months, 6, "Can only fetch 6 month transactions at a time");
		Utils.checkRange(5, limit, 50, "Limit should be within 5 to 50.");
		long startTime = Utils.getMillis(LocalDate.now()) - (months * Utils.MONTH_MILLIS);
		int totalRecords = transactionManager.getTransactionCount(accountNo, startTime);
		int totalPages = (int) Math.ceil((double) totalRecords / limit);
		return totalPages;
	}

	public List<Transaction> getTransactions(int accountNo, int months, int pageNo, int limit)
			throws CustomException, InvalidValueException {
		if (months > 6) {
			throw new InvalidValueException("Can only fetch 6 month transactions at a time");
		}
		if (!accountManager.isValidAccount(accountNo)) {
			throw new InvalidValueException("Invalid Account number!");
		}
		long startTime = Utils.getMillis(LocalDate.now()) - (months * Utils.MONTH_MILLIS);
		int offset = Utils.pagination(pageNo, limit);
		return transactionManager.getTransactions(accountNo, startTime, offset, limit);
	}

	public void deposit(int accountNo, double amount, String description, int modifiedBy, long modifiedOn)
			throws CustomException, InvalidValueException {
		if (amount < 1) {
			throw new InvalidValueException("Invalid amount!");
		}
		Account account = getAccount(accountNo);
		CustomerHandler.accountCache.remove(accountNo);
		Transaction transaction = new Transaction();
		transaction.setPrimaryAccount(accountNo);
		transaction.setAmount(amount);
		transaction.setType(TransactionType.CREDIT);
		transaction.setDescription(description);
		transaction.setCustomerId(account.getCustomerId());
		transaction.setModifiedBy(modifiedBy);
		transaction.setModifiedOn(modifiedOn);
		transactionManager.initTransaction(transaction);
	}

	public void changePassword(int customerId, String currentPassword, String newPassword, int modifiedBy,
			long modifiedOn) throws InvalidValueException, CustomException {
		EmployeeManager employeeManager = new EmployeeDao();
		employeeManager.setPassword(customerId, currentPassword, newPassword, modifiedBy, modifiedOn);
	}
}
