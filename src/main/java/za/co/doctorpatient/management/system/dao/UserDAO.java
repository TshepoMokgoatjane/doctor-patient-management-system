package za.co.doctorpatient.management.system.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import za.co.doctorpatient.management.system.model.User;
import za.co.doctorpatient.management.system.roles.Role;
import za.co.doctorpatient.management.system.security.PasswordHasher;

public class UserDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);
	
	private DataSource dataSource;
	
	public UserDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public User authenticate(String username, String password) throws Exception {
		
		LOGGER.info("Attempting to authenticate a user.");
		
		String sql = """
				SELECT id, username, password, role
				FROM users
				WHERE username = ?
				AND is_active = TRUE
				""";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			
			preparedStatement.setString(1, username);

			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					String storedHash = resultSet.getString("password");
					
					if (PasswordHasher.verifyPassword(password, storedHash)) {
						return new User(
								resultSet.getInt("id"),
								resultSet.getString("username"),
								Role.valueOf(resultSet.getString("role"))
							);
					}
				}
			}
		}
		return null; // Invalid credentials
	}
	
	public List<User> getAllUsers() throws Exception {
		
		LOGGER.info("Fetching all users");
		
		String sql = "SELECT id, username, role FROM users ORDER BY username";
		
		List<User> users = new ArrayList<>();
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery()) {
			
			while (resultSet.next()) {
				User user = new User(
						resultSet.getInt("id"),
						resultSet.getString("username"),
						Role.valueOf(resultSet.getString("role"))
				);
				users.add(user);
			}
			
		} catch (SQLException e) {
			LOGGER.error("Failed to fetch users", e);
			throw new Exception("Unable to retrieve users", e);
		}
		return users;
	}
	
	public int getUserCount() throws Exception {
		
		String sql = "SELECT COUNT(*) FROM users WHERE is_active = TRUE";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery()) {
			
			resultSet.next();
			return resultSet.getInt(1);
			
		} catch (SQLException e) {
			LOGGER.error("Failed to count users", e);
			throw new Exception("Unable to count users", e);
		}
	}

}
