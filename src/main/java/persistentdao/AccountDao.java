package persistentdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.Account;
import persistentlayer.AccountManager;
import utility.ActiveStatus;
import utility.Utils;

public class AccountDao implements AccountManager {

	@Override
	public void createAccount(Account account) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO account(customerId,openDate,branchId,status,isPrimaryAccount,mpin) values(?,?,?,?,?,?)")) {
			statement.setInt(1, account.getCustomerId());
			statement.setLong(2, System.currentTimeMillis());
			statement.setInt(3, account.getBranchId());
			statement.setInt(4, ActiveStatus.ACTIVE.ordinal());
			statement.setBoolean(5, account.isPrimaryAccout());
			String mpin = account.getMpin();
			if (mpin == null || mpin.isEmpty()) {
				mpin = "0000";
			}
			String hashedMpin = Utils.hashPassword(mpin);
			statement.setString(6, hashedMpin);
			statement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Account Creation failed!", e);
		}
	}

	@Override
	public void setAccountStatus(int accountNo, ActiveStatus status) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("UPDATE account SET status = ? WHERE accountNo = ?")) {
			statement.setInt(1, status.ordinal());
			statement.setInt(2, accountNo);
			statement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Account Deletion failed!", e);
		}
	}

	@Override
	public void deleteAccount(int accountNo) throws CustomException {
		setAccountStatus(accountNo, ActiveStatus.INACTIVE);
	}

	@Override
	public double getCurrentBalance(int accountNo) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT currentBalance FROM account WHERE accountNo = ?")) {
			statement.setInt(1, accountNo);
			try (ResultSet resultSet = statement.executeQuery();) {
				if (resultSet.next()) {
					return resultSet.getDouble(1);
				}
			}
			throw new InvalidValueException("Invalid account number!");
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Balance fetch failed!", e);
		}
	}

	@Override
	public double getTotalBalance(int customerId) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT SUM(currentBalance) FROM account WHERE customerId = ?")) {
			statement.setInt(1, customerId);
			try (ResultSet resultSet = statement.executeQuery();) {
				if (resultSet.next()) {
					return resultSet.getDouble(1);
				}
			}
			throw new InvalidValueException("Invalid account number!");
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Balance fetch failed!", e);
		}
	}

	@Override
	public boolean isValidAccount(int accountNo) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT COUNT(*) as count FROM account WHERE accountNo = ?")) {
			statement.setInt(1, accountNo);
			try (ResultSet result = statement.executeQuery();) {
				if (result.next()) {
					if (result.getInt("count") == 1) {
						return true;
					}
				}
			}
			return false;
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Account Validation failed!", e);
		}
	}

	@Override
	public Account getAccount(int accountNo) throws InvalidValueException, CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT * FROM account WHERE accountNo = ?")) {
			statement.setInt(1, accountNo);
			try (ResultSet result = statement.executeQuery();) {
				if (result.next()) {
					Account account = new Account();
					account.setAccountNo(result.getInt("accountNo"));
					account.setBranchId(result.getInt("branchId"));
					account.setCurrentBalance(result.getDouble("currentBalance"));
					account.setCustomerId(result.getInt("customerId"));
					account.setOpenDate(result.getLong("openDate"));
					account.setPrimaryAccout(result.getBoolean("isPrimaryAccount"));
					account.setStatus(ActiveStatus.values()[result.getInt("status")]);
					return account;
				}
			}
			throw new InvalidValueException("Invalid Account number!");
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Account Validation failed!", e);
		}
	}

	@Override
	public Map<Integer, Account> getAccounts(int customerId) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT * FROM account WHERE customerId = ?")) {
			statement.setInt(1, customerId);
			try (ResultSet result = statement.executeQuery();) {
				Map<Integer, Account> accounts = new HashMap<>();
				while (result.next()) {
					Account account = resultSetToAccount(result);
					accounts.put(account.getAccountNo(), account);
				}
				return accounts;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Account Validation failed!", e);
		}
	}

	@Override
	public Map<Integer, Account> getBranchAccounts(int branchId) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM account WHERE branchId = ?")) {
			statement.setInt(1, branchId);
			try (ResultSet result = statement.executeQuery();) {
				Map<Integer, Account> accounts = new HashMap<>();
				while (result.next()) {
					Account account = resultSetToAccount(result);
					accounts.put(account.getAccountNo(), account);
				}
				return accounts;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Account Validation failed!", e);
		}
	}

	private Account resultSetToAccount(ResultSet result) throws SQLException {
		Account account = new Account();
		account.setAccountNo(result.getInt("accountNo"));
		account.setBranchId(result.getInt("branchId"));
		account.setCurrentBalance(result.getDouble("currentBalance"));
		account.setCustomerId(result.getInt("customerId"));
		account.setOpenDate(result.getLong("openDate"));
		account.setPrimaryAccout(result.getBoolean("isPrimaryAccount"));
		account.setStatus(ActiveStatus.values()[result.getInt("status")]);
		return account;
	}

	@Override
	public void setPrimaryAccount(int customerId, int accountNo) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement offStatement = connection.prepareStatement(
						"UPDATE account SET isPrimaryAccount = 0 WHERE customerId = ? and isPrimaryAccount = 1");
				PreparedStatement setStatement = connection
						.prepareStatement("UPDATE account SET isPrimaryAccount = 1 WHERE accountNo = ?")) {
			offStatement.setInt(1, customerId);
			setStatement.setInt(1, accountNo);
			try {
				connection.setAutoCommit(false);
				offStatement.executeUpdate();
				setStatement.executeUpdate();
				connection.commit();
			} catch (SQLException e) {
				connection.rollback();
				throw e;
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Primary Account Modification failed!", e);
		}
	}

	@Override
	public void setMpin(int accountNo, String newPin) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("UPDATE account SET mPin = ? WHERE accountNo = ?")) {
			String hashedMpin = Utils.hashPassword(newPin);
			statement.setString(1, hashedMpin);
			statement.setInt(2, accountNo);
			statement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("MPIN Updation failed!", e);
		}
	}

	@Override
	public void checkValidRequest(int customerId, String mpin, int accountNo)
			throws InvalidValueException, CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT customerId,mpin FROM account WHERE accountNo = ?")) {
			statement.setInt(1, accountNo);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					if (customerId != resultSet.getInt(1)) {
						throw new InvalidValueException("Invalid account number and customer id!");
					}
					String hashedMpin = Utils.hashPassword(mpin);
					if (!hashedMpin.equals(resultSet.getString(2))) {
						throw new InvalidValueException("Invalid MPIN!");
					}
				} else {
					throw new InvalidValueException("Invalid account number!");
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Request Validation failed!");
		}
	}

}
