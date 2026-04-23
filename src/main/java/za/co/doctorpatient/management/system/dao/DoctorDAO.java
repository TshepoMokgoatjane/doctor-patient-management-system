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

import za.co.doctorpatient.management.system.model.Doctor;

public class DoctorDAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDAO.class);

	private DataSource dataSource;
	
	public DoctorDAO(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public List<Doctor> getDoctors(int offset, int limit, String sortField, String sortDir) throws Exception {
		
		LOGGER.info("Fetching doctors with pagination and sorting");
		
		// Whitelist sortable columns
		String orderByColumn;
		
		switch (sortField) {
		case "firstName":
			orderByColumn = "first_name";
			break;
		case "lastName":
			orderByColumn = "last_name";
			break;
		case "specialization":
			orderByColumn = "specialization";
			break;
		case "email":
			orderByColumn = "email";
			break;
			default:
				orderByColumn = "last_name";
		}
		
		String orderDirection = "desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC";
		
		List<Doctor> doctors = new ArrayList<>();
		
		String sql = String.format("""
				SELECT id, first_name, last_name, specialization, email 
				FROM doctor
				ORDER BY %s %s
				LIMIT ? OFFSET ?
				""", orderByColumn, orderDirection);
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);				
		) {
			
			statement.setInt(1, limit);
			statement.setInt(2, offset);
			
			try (ResultSet resultSet = statement.executeQuery()) {
			
				while (resultSet.next()) {
					
					int id = resultSet.getInt("id");
					String firstName = resultSet.getString("first_name");
					String lastName = resultSet.getString("last_name");
					String specialization = resultSet.getString("specialization");
					String email = resultSet.getString("email");
					
					Doctor doctor = new Doctor(id, firstName, lastName, specialization, email);
					
					doctors.add(doctor);
				}
			}
			
			LOGGER.debug("Successfully fetched {} doctors: " , doctors.size());
			LOGGER.info("Successfully fetched doctors from databse - closing resources...");
			
		} catch (SQLException e) {
			LOGGER.error("Failed to fetch doctors from database", e);
			throw new Exception("Unable to retrieve doctors from database", e);
		}
		return doctors;
	}
	
	public int getDoctorCount() throws Exception {
		String sql = "SELECT COUNT(*) FROM doctor";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery()) {
			
			resultSet.next();
			return resultSet.getInt(1);
		} catch (SQLException e) {
			LOGGER.error("Failed to count doctors", e);
			throw new Exception("Unable to count doctors", e);
		}
	}
	
	public int addDoctor(Doctor doctor) throws Exception {
		
		LOGGER.info("Attempting to insert new doctor record in the database table");
		
		String sql = "INSERT INTO doctor (first_name, last_name, specialization, email) VALUES(?,?,?,?)";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				) {
			
			// ALL placeholders MUST be set
			preparedStatement.setString(1, doctor.getFirstName());
			preparedStatement.setString(2, doctor.getLastName());
			preparedStatement.setString(3, doctor.getSpecialization());
			preparedStatement.setString(4, doctor.getEmail());
			
			int rowsAffected = preparedStatement.executeUpdate();
			
			if (rowsAffected == 0 ) {
				LOGGER.error("Exception occurred, failed to insert new record");
				throw new SQLException("Insert failed, no rows affected.");
			}

			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				
				if (generatedKeys.next()) {
					int generatedId = generatedKeys.getInt(1);
					LOGGER.info("Doctor inserted successfully with ID {}", generatedId);
					return generatedId;
				} else {
					throw new SQLException("Insert succeeded but no ID obtained.");
				}
			}
			
			
			
		} catch (SQLException e) {
			LOGGER.error("Failed to insert new doctor record in database table", e);
			throw new Exception("Unable to insert new doctor", e);
		}
	}
}
