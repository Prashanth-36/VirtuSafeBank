package persistentdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.Customer;
import persistentlayer.CustomerManager;
import utility.ActiveStatus;
import utility.Gender;
import utility.UserType;
import utility.Utils;

public class CustomerDao implements CustomerManager {

	@Override
	public void addCustomer(Customer customer) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement userStatement = connection.prepareStatement(
						"INSERT INTO user(name, dob, number,password,status,type,location,city,state,email,gender) values(?,?,?,?,?,?,?,?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				PreparedStatement customerStatement = connection
						.prepareStatement("INSERT INTO customer(id,aadhaarNo,panNo) VALUES(?,?,?)");) {

			userStatement.setString(1, customer.getName());
			userStatement.setLong(2, customer.getDob());
			userStatement.setLong(3, customer.getNumber());
			String hashedPassword = Utils.hashPassword(customer.getPassword());
			userStatement.setString(4, hashedPassword);
			userStatement.setInt(5, ActiveStatus.ACTIVE.ordinal());
			userStatement.setInt(6, customer.getType().ordinal());
			userStatement.setString(7, customer.getLocation());
			userStatement.setString(8, customer.getCity());
			userStatement.setString(9, customer.getState());
			userStatement.setString(10, customer.getEmail());
			userStatement.setInt(11, customer.getGender().ordinal());

			customerStatement.setLong(2, customer.getAadhaarNo());
			customerStatement.setString(3, customer.getPanNo());

			try {
				connection.setAutoCommit(false);
				int userAffectedRows = userStatement.executeUpdate();
				if (userAffectedRows > 0) {
					try (ResultSet resultSet = userStatement.getGeneratedKeys();) {
						if (resultSet.next()) {
							customerStatement.setInt(1, resultSet.getInt(1));
							customerStatement.execute();
							connection.commit();
						}
					}
				}
			} catch (SQLException e) {
				connection.rollback();
				throw new CustomException("Customer Creation failed!", e);
			} finally {
				connection.setAutoCommit(true);
			}

		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Customer Creation failed!", e);
		}
	}

	@Override
	public void setCustomerStatus(int customerId, ActiveStatus status) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement("UPDATE user SET status = ? where id = ?");) {
			statement.setInt(1, status.ordinal());
			statement.setInt(2, customerId);
			statement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Customer Status Modification failed!", e);
		}
	}

	@Override
	public void removeCustomer(int customerId) throws CustomException {
		setCustomerStatus(customerId, ActiveStatus.INACTIVE);
	}

	@Override
	public Customer getCustomer(int id) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"SELECT u.*,aadhaarNo,panNo FROM user u JOIN customer c on u.id=c.id WHERE u.id = ?")) {
			statement.setInt(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSetToCustomer(resultSet);
				}
				throw new InvalidValueException("Invalid Customer id!");
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Customer fetch failed!", e);
		}
	}

	@Override
	public Map<Integer, Customer> getCustomers(int branchId, int offset, int limit, ActiveStatus status)
			throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"SELECT u.*,panNo,aadhaarNo FROM user u JOIN customer c on u.id=c.id JOIN account a on a.customerId=u.id WHERE branchId = ? AND u.status = ? LIMIT ?,?")) {
			statement.setInt(1, branchId);
			statement.setInt(2, status.ordinal());
			statement.setInt(3, offset);
			statement.setInt(4, limit);
			try (ResultSet resultSet = statement.executeQuery()) {
				Map<Integer, Customer> customers = new HashMap<>();
				while (resultSet.next()) {
					Customer customer = resultSetToCustomer(resultSet);
					customers.put(customer.getUserId(), customer);
				}
				return customers;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Customer fetch failed!", e);
		}
	}

	@Override
	public int getCustomerId(long aadhaarNo) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT id FROM customer where aadhaarNo = ?");) {
			statement.setLong(1, aadhaarNo);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("id");
			}
			return -1;
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Validation failed!", e);
		}
	}

	public static Customer resultSetToCustomer(ResultSet customerRecord) throws SQLException {
		Customer customer = new Customer();
		customer.setUserId(customerRecord.getInt("id"));
		customer.setDob(customerRecord.getLong("dob"));
		customer.setLocation(customerRecord.getString("location"));
		customer.setCity(customerRecord.getString("city"));
		customer.setState(customerRecord.getString("state"));
		customer.setStatus(ActiveStatus.values()[customerRecord.getInt("status")]);
		customer.setName(customerRecord.getString("name"));
		customer.setEmail(customerRecord.getString("email"));
		customer.setNumber(customerRecord.getLong("number"));
		customer.setType(UserType.values()[customerRecord.getInt("type")]);
		customer.setPanNo(customerRecord.getString("panNo"));
		customer.setAadhaarNo(customerRecord.getLong("aadhaarNo"));
		customer.setGender(Gender.values()[customerRecord.getInt("gender")]);
		return customer;
	}

	@Override
	public int getCustomersCount(int branchId, ActiveStatus status) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"SELECT COUNT(*) FROM user u JOIN customer c on u.id=c.id JOIN account a on a.customerId=u.id WHERE branchId = ? AND u.status = ?")) {
			statement.setInt(1, branchId);
			statement.setInt(2, status.ordinal());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				return 0;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Customer fetch failed!", e);
		}
	}

	@Override
	public List<Integer> getCustomerBranches(int customerId) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT branchId FROM account WHERE customerId = ?")) {
			statement.setInt(1, customerId);
			try (ResultSet resultSet = statement.executeQuery()) {
				List<Integer> branches = new ArrayList<>();
				while (resultSet.next()) {
					branches.add(resultSet.getInt(1));
				}
				return branches;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Customer fetch failed!", e);
		}
	}

}
