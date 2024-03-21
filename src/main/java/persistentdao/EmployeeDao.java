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
			userStatement.setLong(2, employee.getDob());
			userStatement.setLong(3, employee.getNumber());
			String hashedPassword = Utils.hashPassword(employee.getPassword());
			userStatement.setString(4, hashedPassword);
			userStatement.setInt(5, ActiveStatus.ACTIVE.ordinal());
			userStatement.setInt(6, employee.getType().ordinal());
			userStatement.setString(7, employee.getLocation());
			userStatement.setString(8, employee.getCity().toLowerCase());
			userStatement.setString(9, employee.getState().toLowerCase());
			userStatement.setString(10, employee.getEmail());
			userStatement.setInt(11, employee.getGender().ordinal());

			statement.setInt(2, employee.getBranchId());

			try {
				connection.setAutoCommit(false);
				int rows = userStatement.executeUpdate();
				if (rows > 0) {
					try (ResultSet resultSet = userStatement.getGeneratedKeys()) {
						if (resultSet.next()) {
							statement.setInt(1, resultSet.getInt(1));
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
			statement.setInt(1, id);
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
			statement.setInt(1, branchId);
			statement.setInt(2, status.ordinal());
			statement.setInt(3, offset);
			statement.setInt(4, limit);
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
			statement.setInt(1, status.ordinal());
			statement.setInt(2, employeeId);
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
			statement.setInt(1, branchId);
			statement.setInt(2, status.ordinal());
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

}
