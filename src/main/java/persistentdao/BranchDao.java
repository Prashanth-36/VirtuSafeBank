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
import model.Branch;
import persistentlayer.BranchManager;
import utility.ActiveStatus;

public class BranchDao implements BranchManager {

	@Override
	public void addBranch(Branch branch) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(
						"INSERT INTO branch(location,city,state,status) values(?,?,?,?)",
						Statement.RETURN_GENERATED_KEYS);
				PreparedStatement updateStatement = connection
						.prepareStatement("UPDATE branch SET ifsc = ? WHERE id = ?")) {
			statement.setString(1, branch.getLocation());
			statement.setString(2, branch.getCity());
			statement.setString(3, branch.getState());
			statement.setInt(4, ActiveStatus.ACTIVE.ordinal());
			try {
				connection.setAutoCommit(false);
				int rows = statement.executeUpdate();
				if (rows > 0) {
					try (ResultSet resultSet = statement.getGeneratedKeys()) {
						if (resultSet.next()) {
							int id = resultSet.getInt(1);
							String ifsc = "VSB" + String.format("%04d", id);
							branch.setIfsc(ifsc);
							updateStatement.setString(1, ifsc);
							updateStatement.setInt(2, id);
							updateStatement.executeUpdate();
							connection.commit();
						}
					}
				}
			} catch (SQLException e) {
				connection.rollback();
				throw e;
			} finally {
				connection.setAutoCommit(true);
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Branch Creation failed!", e);
		}
	}

	@Override
	public Branch getBranch(int branchId) throws CustomException, InvalidValueException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM branch WHERE id = ?")) {
			statement.setInt(1, branchId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSetToBranch(resultSet);
				}
				throw new InvalidValueException("Invalid branch id!");
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Branch fetch failed!", e);
		}
	}

	@Override
	public Map<Integer, Branch> getBranches(ActiveStatus status) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM branch WHERE status = ?")) {
			statement.setInt(1, status.ordinal());
			try (ResultSet resultSet = statement.executeQuery()) {
				Map<Integer, Branch> branches = new HashMap<>();
				while (resultSet.next()) {
					Branch branch = resultSetToBranch(resultSet);
					branches.put(branch.getId(), branch);
				}
				return branches;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Branch fetch failed!", e);
		}
	}

	@Override
	public void removeBranch(int branchId) throws CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection
						.prepareStatement("UPDATE branch SET status = ? WHERE id = ?")) {
			statement.setInt(1, ActiveStatus.INACTIVE.ordinal());
			statement.setInt(2, branchId);
			statement.executeUpdate();
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Branch Deletion failed!", e);
		}
	}

	private Branch resultSetToBranch(ResultSet resultSet) throws SQLException {
		Branch branch = new Branch();
		branch.setId(resultSet.getInt("id"));
		branch.setIfsc(resultSet.getString("ifsc"));
		branch.setLocation(resultSet.getString("location"));
		branch.setCity(resultSet.getString("city"));
		branch.setState(resultSet.getString("state"));
		branch.setStatus(ActiveStatus.values()[resultSet.getInt("status")]);
		return branch;
	}

	@Override
	public boolean isValidBranch(int branchId) throws InvalidValueException, CustomException {
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM branch WHERE id = ?")) {
			statement.setInt(1, branchId);
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					if (resultSet.getInt(1) == 1) {
						return true;
					}
				}
				return false;
			}
		} catch (SQLException | ClassNotFoundException e) {
			throw new CustomException("Branch fetch failed!", e);
		}
	}

}
