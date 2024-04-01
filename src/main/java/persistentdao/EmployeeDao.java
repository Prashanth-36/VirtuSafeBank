package persistentdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customexceptions.CustomException;
import customexceptions.InvalidValueException;
import model.Employee;
import persistentlayer.EmployeeManager;
import utility.ActiveStatus;
import utility.Gender;
import utility.UserType;
import utility.Utils;

public class EmployeeDao implements EmployeeManager {

	@Override
	public void addEmployee(Employee employee) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement userStatement = connection.prepareStatement(
						"INSERT INTO user(name, dob, number,password,status,type,location,city,state,email,gender) values(?,?,?,?,?,?,?,?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				PreparedStatement statement = connection
						.prepareStatement("INSERT INTO employee(id,branchId) VALUES (?,?)");) {
			userStatement.setString(1, employee.getName());
			userStatement.setObject(2, employee.getDob());
			userStatement.setObject(3, employee.getNumber());
			String hashedPassword = Utils.hashPassword(employee.getPassword());
			userStatement.setString(4, hashedPassword);
			userStatement.setObject(5, ActiveStatus.ACTIVE.ordinal());
			userStatement.setObject(6, employee.getType().ordinal());
			userStatement.setString(7, employee.getLocation());
			userStatement.setString(8, employee.getCity().toLowerCase());
			userStatement.setString(9, employee.getState().toLowerCase());
			userStatement.setString(10, employee.getEmail());
			userStatement.setObject(11, employee.getGender().ordinal());

			statement.setObject(2, employee.getBranchId());

			try {
				connection.setAutoCommit(false);
				int rows = userStatement.executeUpdate();
				if (rows > 0) {
					try (ResultSet resultSet = userStatement.getGeneratedKeys()) {
						if (resultSet.next()) {
							statement.setObject(1, resultSet.getInt(1));
							statement.executeUpdate();
							connection.commit();
						}
					}
				}
			} catch (SQLException e) {
				connection.rollback();
				throw new CustomException("Employee Creation failed!", e);
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Employee creation failed!", e);
		}
	}

	@Override
	public Employee getEmployee(int id) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"SELECT u.*,branchId FROM user u JOIN employee e on u.id=e.id WHERE u.id = ?")) {
			statement.setObject(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSetToEmployee(resultSet);
				}
				throw new InvalidValueException("Invalid Employee id!");
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Employee fetch failed!", e);
		}
	}

	@Override
	public Map<Integer, Employee> getEmployees(int branchId, int offset, int limit, ActiveStatus status)
			throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"SELECT u.*,e.branchId from user u join employee e on e.id=u.id where branchId = ? AND status = ? limit ?,?");) {
			statement.setObject(1, branchId);
			statement.setObject(2, status.ordinal());
			statement.setObject(3, offset);
			statement.setObject(4, limit);
			try (ResultSet resultSet = statement.executeQuery();) {
				Map<Integer, Employee> employees = new HashMap<>();
				while (resultSet.next()) {
					Employee employee = resultSetToEmployee(resultSet);
					employees.put(employee.getUserId(), employee);
				}
				return employees;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Employee fetch failed!", e);
		}
	}

	@Override
	public Map<Integer, Employee> getAllEmployees(int offset, int limit) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"SELECT u.*,e.branchId from user u join employee e on e.id=u.id ORDER BY e.branchId,u.id limit ?,? ");) {
			statement.setObject(1, offset);
			statement.setObject(2, limit);
			try (ResultSet resultSet = statement.executeQuery();) {
				Map<Integer, Employee> employees = new HashMap<>();
				while (resultSet.next()) {
					Employee employee = resultSetToEmployee(resultSet);
					employees.put(employee.getUserId(), employee);
				}
				return employees;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Employee fetch failed!", e);
		}
	}

	@Override
	public void setEmployeeStatus(int employeeId, ActiveStatus status) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement("UPDATE user SET status = ? WHERE id = ?")) {
			statement.setObject(1, status.ordinal());
			statement.setObject(2, employeeId);
			statement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Employee deletion failed!", e);
		}
	}

	@Override
	public void removeEmployee(int id) throws CustomException {
		setEmployeeStatus(id, ActiveStatus.INACTIVE);
	}

	public static Employee resultSetToEmployee(ResultSet employeeRecord) throws SQLException {
		Employee employee = new Employee();
		employee.setUserId(employeeRecord.getInt("id"));
		employee.setDob(employeeRecord.getLong("dob"));
		employee.setLocation(employeeRecord.getString("location"));
		employee.setCity(employeeRecord.getString("city"));
		employee.setState(employeeRecord.getString("state"));
		employee.setStatus(ActiveStatus.values()[employeeRecord.getInt("status")]);
		employee.setName(employeeRecord.getString("name"));
		employee.setNumber(employeeRecord.getLong("number"));
		employee.setEmail(employeeRecord.getString("email"));
		employee.setType(UserType.values()[employeeRecord.getInt("type")]);
		employee.setBranchId(employeeRecord.getInt("branchId"));
		employee.setGender(Gender.values()[employeeRecord.getInt("gender")]);
		return employee;
	}

	@Override
	public int getEmployeesCount(int branchId, ActiveStatus status) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"SELECT COUNT(*) from user u join employee e on e.id=u.id where branchId = ? AND status = ?");) {
			statement.setObject(1, branchId);
			statement.setObject(2, status.ordinal());
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
	public int getAllEmployeesCount() throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT COUNT(*) from user u join employee e on e.id=u.id");) {
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
	public void updateEmployee(Employee employee) throws CustomException, InvalidValueException {
		Employee existingEmployee = getEmployee(employee.getUserId());
		List<Object> queryData = HelperDao.buildQuery(existingEmployee, employee);
		if (queryData.size() > 1) {
			StringBuffer sb = new StringBuffer();
			sb.append("UPDATE user u JOIN employee e on u.id=e.id SET");
			sb.append(queryData.get(0));
			sb.append(" where u.id = ?");
			String query = sb.toString();
			try (Connection connection = DBConnection.getConnection();
					PreparedStatement statement = connection.prepareStatement(query)) {
				int size = queryData.size();
				for (int i = 1; i < size; i++) {
					statement.setObject(i, queryData.get(i));
				}
				statement.setObject(size, employee.getUserId());
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
