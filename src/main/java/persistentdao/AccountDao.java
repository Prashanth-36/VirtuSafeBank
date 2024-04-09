package persistentdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	public int createAccount(Account account) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO account(customerId,openDate,branchId,status,isPrimaryAccount,mpin,modifiedBy,modifiedOn) values(?,?,?,?,?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS)) {
			statement.setObject(1, account.getCustomerId());
			statement.setObject(2, System.currentTimeMillis());
			statement.setObject(3, account.getBranchId());
			statement.setObject(4, ActiveStatus.ACTIVE.ordinal());
			statement.setObject(5, account.getIsPrimaryAccount());
			statement.setObject(6, account.getModifiedBy());
			statement.setObject(7, account.getModifiedOn());
			String mpin = account.getMpin();
			if (mpin == null || mpin.isEmpty()) {
				mpin = "0000";
			}
			String hashedMpin = Utils.hashPassword(mpin);
			statement.setString(6, hashedMpin);
			int rows = statement.executeUpdate();
			if (rows > 0) {
				try (ResultSet resultSet = statement.getGeneratedKeys()) {
					if (resultSet.next()) {
						int id = resultSet.getInt(1);
						return id;
					}
				}
			}
			throw new CustomException("Account Creation Failed!");
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Account Creation failed!", e);
		}
	}

	@Override
	public void setAccountStatus(int accountNo, ActiveStatus status, int modifiedBy, long modifedOn)
			throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"UPDATE account SET status = ?, modifiedOn = ?, modifiedBy = ? WHERE accountNo = ?")) {
			statement.setObject(1, status.ordinal());
			statement.setObject(2, modifedOn);
			statement.setInt(3, modifiedBy);
			statement.setObject(4, accountNo);
			statement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Account Deletion failed!", e);
		}
	}

	@Override
	public void deleteAccount(int accountNo, int modifiedBy) throws CustomException {
		setAccountStatus(accountNo, ActiveStatus.INACTIVE, modifiedBy, modifiedBy);
	}

	@Override
	public double getCurrentBalance(int accountNo) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT currentBalance FROM account WHERE accountNo = ?")) {
			statement.setObject(1, accountNo);
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
			statement.setObject(1, customerId);
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
			statement.setObject(1, accountNo);
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
			statement.setObject(1, accountNo);
			try (ResultSet result = statement.executeQuery();) {
				if (result.next()) {
					Account account = new Account();
					account.setAccountNo(result.getInt("accountNo"));
					account.setBranchId(result.getInt("branchId"));
					account.setCurrentBalance(result.getDouble("currentBalance"));
					account.setCustomerId(result.getInt("customerId"));
					account.setOpenDate(result.getLong("openDate"));
					account.setIsPrimaryAccount(result.getBoolean("isPrimaryAccount"));
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
			statement.setObject(1, customerId);
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
			statement.setObject(1, branchId);
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
	public int getBranchAccountsCount(int branchId, int limit) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT COUNT(*) FROM account WHERE branchId = ?")) {
			statement.setObject(1, branchId);
			try (ResultSet result = statement.executeQuery();) {
				if (result.next()) {
					return result.getInt(1);
				}
				return 0;
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
		account.setIsPrimaryAccount(result.getBoolean("isPrimaryAccount"));
		account.setStatus(ActiveStatus.values()[result.getInt("status")]);
		return account;
	}

	@Override
	public void setPrimaryAccount(int customerId, int accountNo, int modifiedBy, long modifedOn)
			throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement offStatement = connection.prepareStatement(
						"UPDATE account SET isPrimaryAccount = 0 WHERE customerId = ? and isPrimaryAccount = 1");
				PreparedStatement setStatement = connection.prepareStatement(
						"UPDATE account SET isPrimaryAccount = 1, modifiedBy = ?, modifiedOn = ? WHERE accountNo = ?")) {
			offStatement.setObject(1, customerId);
			setStatement.setObject(1, modifiedBy);
			setStatement.setObject(2, modifedOn);
			setStatement.setObject(3, accountNo);
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
	public void setMpin(int accountNo, String newPin, int modifiedBy, long modifedOn)
			throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"UPDATE account SET mPin = ?, modifiedBy = ?, modifiedOn = ? WHERE accountNo = ?")) {
			String hashedMpin = Utils.hashPassword(newPin);
			statement.setString(1, hashedMpin);
			statement.setObject(2, modifiedBy);
			statement.setObject(3, modifedOn);
			statement.setObject(4, accountNo);
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
			statement.setObject(1, accountNo);
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