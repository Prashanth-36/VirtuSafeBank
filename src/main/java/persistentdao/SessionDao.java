package persistentdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import customexceptions.CustomException;
import customexceptions.InvalidOperationException;
import customexceptions.InvalidValueException;
import model.Audit;
import model.Token;
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

	@Override
	public void audit(Audit audit) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO audit(time,action,targetId,modifiedBy,description,status) VALUES(?,?,?,?,?,?)")) {
			statement.setLong(1, audit.getTime());
			statement.setString(2, audit.getAction());
			statement.setInt(3, audit.getTargetId());
			statement.setInt(4, audit.getModifiedBy());
			statement.setString(5, audit.getDescription());
			statement.setBoolean(6, audit.getStatus());
			statement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Audit Logging failed!", e);
		}
	}

	@Override
	public void generateToken(Token token) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement removeStatement = connection.prepareStatement("DELETE FROM token WHERE userId = ?");
				PreparedStatement statement = connection
						.prepareStatement("INSERT INTO token(userId,token,accessLevel,validTill) VALUES(?,?,?,?)");) {
			removeStatement.setInt(1, token.getUserId());
			removeStatement.executeUpdate();
			statement.setInt(1, token.getUserId());
			statement.setString(2, token.getToken());
			statement.setInt(3, token.getAccessLevel());
			statement.setLong(4, token.getValidTill());
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			throw new CustomException("Token Generation Failed!", e);
		}
	}

	@Override
	public Token getToken(String token) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM token WHERE token = ?");) {
			statement.setString(1, token);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					Token data = new Token();
					data.setToken(token);
					data.setUserId(resultSet.getInt("userId"));
					data.setValidTill(resultSet.getLong("validTill"));
					data.setAccessLevel(resultSet.getInt("accessLevel"));
					return data;
				}
				throw new InvalidValueException("Invalid Token!");
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new CustomException("Token Generation Failed!", e);
		}
	}

	@Override
	public Token getToken(int userId) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM token WHERE userId = ?");) {
			statement.setInt(1, userId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					Token data = new Token();
					data.setToken(resultSet.getString("token"));
					data.setUserId(resultSet.getInt("userId"));
					data.setValidTill(resultSet.getLong("validTill"));
					data.setAccessLevel(resultSet.getInt("accessLevel"));
					return data;
				}
				return null;
			}
		} catch (ClassNotFoundException | SQLException e) {
			throw new CustomException("Token Generation Failed!", e);
		}
	}

	@Override
	public void removeToken(String userToken) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement("DELETE FROM token WHERE token = ?");) {
			statement.setString(1, userToken);
			statement.executeUpdate();
		} catch (ClassNotFoundException | SQLException e) {
			throw new CustomException("Token Deletion Failed!", e);
		}
	}

}