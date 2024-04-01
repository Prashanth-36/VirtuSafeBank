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
	public int addCustomer(Customer customer) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement userStatement = connection.prepareStatement(
						"INSERT INTO user(name, dob, number,password,status,type,location,city,state,email,gender) values(?,?,?,?,?,?,?,?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				PreparedStatement customerStatement = connection
						.prepareStatement("INSERT INTO customer(id,aadhaarNo,panNo) VALUES(?,?,?)");) {

			userStatement.setString(1, customer.getName());
			userStatement.setObject(2, customer.getDob());
			userStatement.setObject(3, customer.getNumber());
			String hashedPassword = Utils.hashPassword(customer.getPassword());
			userStatement.setString(4, hashedPassword);
			userStatement.setObject(5, ActiveStatus.ACTIVE.ordinal());
			userStatement.setObject(6, customer.getType().ordinal());
			userStatement.setString(7, customer.getLocation());
			userStatement.setString(8, customer.getCity());
			userStatement.setString(9, customer.getState());
			userStatement.setString(10, customer.getEmail());
			userStatement.setObject(11, customer.getGender().ordinal());

			customerStatement.setObject(2, customer.getAadhaarNo());
			customerStatement.setString(3, customer.getPanNo());
			int customerId = -1;
			try {
				connection.setAutoCommit(false);
				int userAffectedRows = userStatement.executeUpdate();
				if (userAffectedRows > 0) {
					try (ResultSet resultSet = userStatement.getGeneratedKeys();) {
						if (resultSet.next()) {
							customerId = resultSet.getInt(1);
							customerStatement.setObject(1, customerId);
							customerStatement.execute();
							connection.commit();
						}
					}
				}
				return customerId;
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
			statement.setObject(1, status.ordinal());
			statement.setObject(2, customerId);
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
			statement.setObject(1, id);
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
			statement.setObject(1, branchId);
			statement.setObject(2, status.ordinal());
			statement.setObject(3, offset);
			statement.setObject(4, limit);
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
	public Map<Integer, Customer> getAllCustomers(int offset, int limit) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"SELECT u.*,c.aadhaarNo,c.panNo from user u join customer c on c.id=u.id limit ?,?");) {
			statement.setObject(1, offset);
			statement.setObject(2, limit);
			try (ResultSet resultSet = statement.executeQuery();) {
				Map<Integer, Customer> customers = new HashMap<>();
				while (resultSet.next()) {
					Customer customer = resultSetToCustomer(resultSet);
					customers.put(customer.getUserId(), customer);
				}
				return customers;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Employee fetch failed!", e);
		}
	}

	@Override
	public int getCustomerId(long aadhaarNo) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT id FROM customer where aadhaarNo = ?");) {
			statement.setObject(1, aadhaarNo);
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
			statement.setObject(1, branchId);
			statement.setObject(2, status.ordinal());
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
	public int getAllCustomerCount() throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT COUNT(*) from user u join customer c on c.id=u.id");) {
			try (ResultSet resultSet = statement.executeQuery();) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				return 0;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Employee fetch failed!", e);
		}
	}

	@Override
	public List<Integer> getCustomerBranches(int customerId) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT branchId FROM account WHERE customerId = ?")) {
			statement.setObject(1, customerId);
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

	@Override
	public void updateCustomer(Customer customer) throws CustomException, InvalidValueException {
		Customer existingCustomer = getCustomer(customer.getUserId());
		List<Object> queryData = HelperDao.buildQuery(existingCustomer, customer);
		if (queryData.size() > 1) {
			StringBuffer sb = new StringBuffer();
			sb.append("UPDATE user u JOIN customer c on u.id=c.id SET");
			sb.append(queryData.get(0));
			sb.append(" where u.id = ?");
			String query = sb.toString();
			try (Connection connection = DBConnection.getConnection();
					PreparedStatement statement = connection.prepareStatement(query)) {
				int size = queryData.size();
				for (int i = 1; i < size; i++) {
					statement.setObject(i, queryData.get(i));
				}
				statement.setObject(size, customer.getUserId());
				System.out.println(statement.toString());
				statement.executeUpdate();
			} catch (ClassNotFoundException | SQLException e) {
				throw new CustomException("Customer Updation failed!", e);
			}
		}
	}

	@Override
	public void setPassword(int customerId, String currentPassword, String newPassword)
			throws InvalidValueException, CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("UPDATE user SET password = ? WHERE id = ? && password = ?")) {
			String hashedPassword = Utils.hashPassword(newPassword);
			statement.setString(1, hashedPassword);
			statement.setObject(2, customerId);
			String currentHashedPassword = Utils.hashPassword(currentPassword);
			statement.setString(3, currentHashedPassword);
			int affected = statement.executeUpdate();
			if (affected == 0) {
				throw new InvalidValueException("Invalid Current Password!");
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Password Updation failed!", e);
		}
	}

}
