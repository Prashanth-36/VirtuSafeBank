package persistentdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import model.User;
import persistentlayer.SessionManager;
import utility.UserType;
import utility.Utils;

public class SessionDao implements SessionManager {

	@Override
	public User authenticate(int userId, String password)
			throws CustomException, InvalidValueException, InvalidOperationException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("SELECT password,type,status FROM user WHERE id = ?");) {
			statement.setObject(1, userId);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					String hashedPassword = Utils.hashPassword(password);
					if (!result.getString("password").equals(hashedPassword)) {
						throw new InvalidValueException("Invalid user id and password!");
					}
					if (result.getInt("status") == 0) {
						throw new InvalidOperationException("User is currently INACTIVE!");
					}
					UserType type = UserType.values()[result.getInt("type")];
					if (type == UserType.USER) {
						try (PreparedStatement userStatement = connection.prepareStatement(
								"SELECT u.*,aadhaarNo,panNo FROM user u JOIN customer c ON u.id=c.id WHERE u.id = ?");) {
							userStatement.setObject(1, userId);
							try (ResultSet customerRecord = userStatement.executeQuery()) {
								if (customerRecord.next()) {
									User customer = CustomerDao.resultSetToCustomer(customerRecord);
									return customer;
								}
							}
						}
					} else {
						try (PreparedStatement employeeStatement = connection.prepareStatement(
								"SELECT u.*,branchId FROM user u JOIN employee e ON u.id=e.id WHERE u.id = ?");) {
							employeeStatement.setObject(1, userId);
							try (ResultSet employeeRecord = employeeStatement.executeQuery()) {
								if (employeeRecord.next()) {
									User employee = EmployeeDao.resultSetToEmployee(employeeRecord);
									return employee;
								}
							}
						}
					}
				}
				throw new InvalidValueException("Invalid user id!");
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Authentication failed!", e);
		}
	}

}