package persistentdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import customexceptions.CustomException;
import model.Transaction;
import persistentlayer.TransactionManager;
import utility.TransactionType;

public class TransactionDao implements TransactionManager {

	@Override
	public void initTransaction(Transaction transaction) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement transactionStatement = connection.prepareStatement(
						"INSERT INTO transaction(transactionId,type,time,amount,primaryAccount,transactionalAccount,description,customerId,balance,ifsc) values(?,?,?,?,?,?,?,?,?,?)");
				PreparedStatement accountStatement = connection
						.prepareStatement("UPDATE account SET currentBalance = currentBalance + ? WHERE accountNo = ?");
				PreparedStatement balanceStatement = connection
						.prepareStatement("SELECT currentBalance FROM account WHERE accountNo = ?")) {
			String id = transaction.getId();
			if (id == null) {
				id = createdTransactionId(transaction.getPrimaryAccount(), transaction.getTransactionalAccount());
			}
			transactionStatement.setString(1, id);
			transactionStatement.setInt(2, transaction.getType().ordinal());
			transactionStatement.setLong(3, System.currentTimeMillis());
			transactionStatement.setDouble(4, transaction.getAmount());
			transactionStatement.setInt(5, transaction.getPrimaryAccount());
			transactionStatement.setInt(6, transaction.getTransactionalAccount());
			transactionStatement.setString(7, transaction.getDescription());
			transactionStatement.setInt(8, transaction.getCustomerId());
			transactionStatement.setDouble(9, transaction.getBalance());
			transactionStatement.setString(10, transaction.getIfsc());

			balanceStatement.setInt(1, transaction.getPrimaryAccount());

			accountStatement.setInt(2, transaction.getPrimaryAccount());

			if (transaction.getType() == TransactionType.DEBIT) {
				accountStatement.setDouble(1, -transaction.getAmount());
			} else {
				accountStatement.setDouble(1, transaction.getAmount());
			}

			try {
				connection.setAutoCommit(false);
				accountStatement.executeUpdate();
				try (ResultSet resultSet = balanceStatement.executeQuery()) {
					if (resultSet.next()) {
						double balance = resultSet.getDouble(1);
						transactionStatement.setDouble(9, balance);
						transactionStatement.executeUpdate();
						connection.commit();
					}
				}
			} catch (SQLException e) {
				connection.rollback();
				throw new CustomException("Transaction failed!", e);
			} finally {
				connection.setAutoCommit(true);
			}

		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Transaction failed!", e);
		}
	}

	@Override
	public void initTransaction(List<Transaction> transactions) throws CustomException {
		try (Connection connection = DBConnection.getConnection();) {
			try {
				connection.setAutoCommit(false);
				String transactionId = createdTransactionId(transactions.get(0).getPrimaryAccount(),
						transactions.get(0).getTransactionalAccount());

				for (Transaction transaction : transactions) {
					transaction.setId(transactionId);
					initTransaction(transaction);
				}
				connection.commit();
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException e1) {
					throw new CustomException("Rollback failed!", e);
				}
				throw new CustomException("Transaction failed!", e);
			} finally {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException e) {
					throw new CustomException("Toggle Auto Commit failed!", e);
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Transaction failed!", e);
		}
	}

	@Override
	public boolean isSameBankTransaction(int sourceAccountNo, int targetAccountNo) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT COUNT(*) as count FROM account WHERE accountNo IN (?,?)")) {
			statement.setInt(1, sourceAccountNo);
			statement.setInt(2, targetAccountNo);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				if (result.getInt("count") == 2) {
					return true;
				}
			}
			return false;
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Validation failed!", e);
		}
	}

	@Override
	public int getTransactionCount(int accountNo, long startTime) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"SELECT Count(*) FROM transaction WHERE primaryAccount = ? and time > ? ORDER BY time DESC")) {
			statement.setLong(1, accountNo);
			statement.setLong(2, startTime);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return (resultSet.getInt(1));
				}
				return 0;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Transaction fetch failed!", e);
		}
	}

	@Override
	public List<Transaction> getTransactions(int accountNo, long startTime, int offset, int limit)
			throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"SELECT * FROM transaction WHERE primaryAccount = ? and time > ? ORDER BY time DESC LIMIT ?,?")) {
			statement.setLong(1, accountNo);
			statement.setLong(2, startTime);
			statement.setInt(3, offset);
			statement.setInt(4, limit);
			List<Transaction> transactions = new ArrayList<Transaction>();
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					transactions.add(resultSetToTransaction(resultSet));
				}
				return transactions;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Transaction fetch failed!", e);
		}
	}

	private Transaction resultSetToTransaction(ResultSet resultSet) throws SQLException {
		Transaction transaction = new Transaction();
		transaction.setId(resultSet.getString("transactionId"));
		transaction.setTimestamp(resultSet.getLong("time"));
		transaction.setPrimaryAccount(resultSet.getInt("primaryAccount"));
		transaction.setTransactionalAccount(resultSet.getInt("transactionalAccount"));
		transaction.setAmount(resultSet.getDouble("amount"));
		transaction.setType(TransactionType.values()[resultSet.getInt("type")]);
		transaction.setDescription(resultSet.getString("description"));
		transaction.setCustomerId(resultSet.getInt("customerId"));
		transaction.setBalance(resultSet.getDouble("balance"));
		transaction.setIfsc(resultSet.getString("ifsc"));
		return transaction;
	}

	String createdTransactionId(int primaryAccount, int transactionalAccount) {
		return String.format("%04d", primaryAccount) + System.currentTimeMillis()
				+ String.format("%04d", transactionalAccount);
	}

}
