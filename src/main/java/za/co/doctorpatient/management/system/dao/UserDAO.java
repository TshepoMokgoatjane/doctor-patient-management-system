package za.co.doctorpatient.management.system.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

}
