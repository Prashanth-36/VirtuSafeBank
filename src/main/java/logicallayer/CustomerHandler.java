package logicallayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customexceptions.CustomException;
import customexceptions.InsufficientFundException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import model.Account;
import model.Transaction;
import persistentdao.AccountDao;
import persistentdao.CustomerDao;
import persistentdao.TransactionDao;
import persistentlayer.AccountManager;
import persistentlayer.Cache;
import persistentlayer.CustomerManager;
import persistentlayer.TransactionManager;
import utility.TransactionType;
import utility.Utils;

public class CustomerHandler {

	static TransactionManager transactionManager = new TransactionDao();

	static AccountManager accountManager = new AccountDao();

	static CustomerManager customerManager = new CustomerDao();

	public static Cache<Integer, Account> accountCache = new RedisCache<>();

	public static Cache<Integer, List<Integer>> userAccountsCache = new RedisCache<>(6380);

	public double getCurrentBalance(int customerId, String mpin, int accountNo)
			throws CustomException, InvalidValueException {
		checkValidRequest(customerId, mpin, accountNo);
		Account account = accountCache.get(accountNo);
		if (account != null) {
			return account.getCurrentBalance();
		}
		return accountManager.getCurrentBalance(accountNo);
	}

	public double getTotalBalance(int customerId) throws CustomException, InvalidValueException {
		return accountManager.getTotalBalance(customerId);
	}

	public Map<Integer, Account> getAccounts(int customerId) throws CustomException, InvalidValueException {
		List<Integer> accounts = userAccountsCache.get(customerId);
		if (accounts == null) {
			Map<Integer, Account> allAccounts = accountManager.getAccounts(customerId);
			for (Map.Entry<Integer, Account> e : allAccounts.entrySet()) {
				accountCache.set(e.getKey(), e.getValue());
			}
			userAccountsCache.set(customerId, new ArrayList<>(allAccounts.keySet()));
			return allAccounts;
		}
		Map<Integer, Account> allAccounts = new HashMap<>();
		for (int accountNo : accounts) {
			Account account = getAccount(accountNo);
			allAccounts.put(account.getAccountNo(), account);
		}
		return allAccounts;
	}

	public Account getAccount(int accountNo) throws CustomException, InvalidValueException {
		Account account = accountCache.get(accountNo);
		if (account != null) {
			return account;
		}
		account = accountManager.getAccount(accountNo);
		accountCache.set(accountNo, account);
		return account;
	}

	public void setPrimaryAccount(int customerId, int accountNo) throws CustomException, InvalidValueException {
		if (!getAccounts(customerId).keySet().contains(accountNo)) {
			throw new InvalidValueException("Invalid Account Number / Restricted Access");
		}
		List<Integer> accounts = userAccountsCache.get(customerId);
		if (accounts != null) {
			for (int num : accounts) {
				accountCache.remove(num);
			}
		}
		accountManager.setPrimaryAccount(customerId, accountNo);
	}

	public void deposit(int customerId, String mpin, int accountNo, double amount, String description)
			throws CustomException, InvalidValueException {
		checkValidRequest(customerId, mpin, accountNo);
		checkNegativeAmount(amount);
		accountCache.remove(accountNo);
		Transaction transaction = new Transaction();
		transaction.setPrimaryAccount(accountNo);
		transaction.setAmount(amount);
		transaction.setType(TransactionType.CREDIT);
		transaction.setDescription(description);
		transaction.setCustomerId(customerId);
		transactionManager.initTransaction(transaction);
	}

	public void withdrawl(int customerId, String mpin, int accountNo, double amount, String description)
			throws CustomException, InvalidValueException, InsufficientFundException {
		checkValidRequest(customerId, mpin, accountNo);
		checkSufficientBalance(accountNo, amount);
		accountCache.remove(accountNo);
		Transaction transaction = new Transaction();
		transaction.setPrimaryAccount(accountNo);
		transaction.setAmount(amount);
		transaction.setType(TransactionType.DEBIT);
		transaction.setDescription(description);
		transaction.setCustomerId(customerId);
		transactionManager.initTransaction(transaction);
	}

	public void moneyTransfer(int customerId, String mpin, int sourceAccountNo, int targetAccountNo, double amount,
			String ifsc, String description)
			throws InvalidValueException, CustomException, InsufficientFundException, InvalidOperationException {

		if (sourceAccountNo == targetAccountNo) {
			throw new InvalidOperationException("Can't send money to same account!");
		}
		checkValidRequest(customerId, mpin, sourceAccountNo);
		checkSufficientBalance(sourceAccountNo, amount);
		accountCache.remove(sourceAccountNo);
		accountCache.remove(targetAccountNo);
		Transaction primaryTransaction = new Transaction();
		primaryTransaction.setPrimaryAccount(sourceAccountNo);
		primaryTransaction.setTransactionalAccount(targetAccountNo);
		primaryTransaction.setAmount(amount);
		primaryTransaction.setType(TransactionType.DEBIT);
		primaryTransaction.setDescription(description);
		primaryTransaction.setCustomerId(customerId);
		primaryTransaction.setIfsc(ifsc);

		try {
			Account transactionalAccount = getAccount(targetAccountNo);

			Transaction secondaryTransaction = new Transaction();
			secondaryTransaction.setPrimaryAccount(targetAccountNo);
			secondaryTransaction.setTransactionalAccount(sourceAccountNo);
			secondaryTransaction.setAmount(amount);
			secondaryTransaction.setType(TransactionType.CREDIT);
			secondaryTransaction.setDescription(description);
			secondaryTransaction.setIfsc(ifsc);
			secondaryTransaction.setCustomerId(transactionalAccount.getCustomerId());

			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(primaryTransaction);
			transactions.add(secondaryTransaction);

			transactionManager.initTransaction(transactions);

		} catch (InvalidValueException e) { // other bank transaction
			transactionManager.initTransaction(primaryTransaction);
		}

	}

	public int getTransactionPageCount(int customerId, int accountNo, int months, int limit)
			throws InvalidValueException, CustomException {
		Utils.checkRange(1, months, 6, "Can only fetch 6 month transactions at a time");
		Utils.checkRange(5, limit, 50, "Limit should be within 5 to 50.");
		if (!getAccounts(customerId).keySet().contains(accountNo)) {
			throw new InvalidValueException("Invalid Account Number / Restricted Access");
		}
		long startTime = Utils.getMillis(LocalDate.now()) - (months * Utils.MONTH_MILLIS);
		int totalRecords = transactionManager.getTransactionCount(accountNo, startTime);
		int totalPages = (int) Math.ceil((double) totalRecords / limit);
		return totalPages;
	}

	public List<Transaction> getTransactions(int customerId, int accountNo, int months, int pageNo, int limit)
			throws CustomException, InvalidValueException {
		Utils.checkRange(1, months, 6, "Can only fetch 6 month transactions at a time");
		Utils.checkRange(5, limit, 50, "Limit should be within 5 to 50.");
		if (!getAccounts(customerId).keySet().contains(accountNo)) {
			throw new InvalidValueException("Invalid Account Number / Restricted Access");
		}
		long startTime = Utils.getMillis(LocalDate.now()) - (months * Utils.MONTH_MILLIS);
		int offset = Utils.pagination(pageNo, limit);
		return transactionManager.getTransactions(accountNo, startTime, offset, limit);
	}

	private void checkNegativeAmount(double amount) throws InvalidValueException {
		if (amount < 1) {
			throw new InvalidValueException("Invalid amount!");
		}
	}

	private void checkSufficientBalance(int accountNo, double amount)
			throws InvalidValueException, CustomException, InsufficientFundException {
		checkNegativeAmount(amount);
		double currentBalance = accountManager.getCurrentBalance(accountNo);
		if (currentBalance - amount < 0) {
			throw new InsufficientFundException("Amount Insufficient!");
		}
	}

	private void checkValidRequest(int customerId, String mpin, int accountNo)
			throws InvalidValueException, CustomException {
		accountManager.checkValidRequest(customerId, mpin, accountNo);
	}

	public void changeMpin(int customerId, int accountNo, String currentPin, String newPin)
			throws InvalidValueException, CustomException {
		checkValidRequest(customerId, currentPin, accountNo);
		accountManager.setMpin(accountNo, newPin);
	}

	public void changePassword(int customerId, String currentPassword, String newPassword)
			throws InvalidValueException, CustomException {
		customerManager.setPassword(customerId, currentPassword, newPassword);
	}

}
