package persistentlayer;

import java.util.List;

import customexceptions.CustomException;
import model.Transaction;

public interface TransactionManager {

	void initTransaction(Transaction transaction) throws CustomException;

	void initTransaction(List<Transaction> transactions) throws CustomException;

	int getTransactionCount(int accountNo, long startTime) throws CustomException;

	List<Transaction> getTransactions(int accountNo, long startTime, int offset, int limit) throws CustomException;

	boolean isSameBankTransaction(int sourceAccountNo, int targetAccountNo) throws CustomException;

}
