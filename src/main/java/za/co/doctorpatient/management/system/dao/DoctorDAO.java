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
	
	private String resolveSortColumn(String sortField) {
		
		if (sortField == null) {
			return "last_name"; // default
		}
		
		switch (sortField) {
		case "firstName":
			return "first_name";
		case "lastName":
			return "last_name";
		case "specialization":
			return "specialization";
		case "email":
			return "email";
			default:
				return "last_name"; // fallback safety
		}
	}
	
	public List<Doctor> getDoctors(int offset, int limit, String sortField, String sortDir, String searchTerm) throws Exception {
		
		LOGGER.info("Fetching doctors with pagination, sorting and search");
		
		// Whitelist sortable columns
		String orderByColumn = resolveSortColumn(sortField);
		String orderDirection = "desc".equalsIgnoreCase(sortDir) ? "DESC" : "ASC";
		
		String sql = """
				SELECT id, first_name, last_name, specialization, email
				FROM doctor
				WHERE is_deleted = FALSE
				AND (
						LOWER(first_name) LIKE ?
						OR LOWER(last_name) LIKE ?
						OR LOWER(specialization) LIKE ?
						OR LOWER(email) LIKE ?
					)
					ORDER BY %s %s
					LIMIT ? OFFSET ?
				""".formatted(orderByColumn, orderDirection);
		
		String likeTerm = "%" + (searchTerm == null ? "" : searchTerm.toLowerCase()) + "%";
		
		List<Doctor> doctors = new ArrayList<>();
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(sql);				
		) {
			statement.setString(1, likeTerm);
			statement.setString(2, likeTerm);
			statement.setString(3, likeTerm);
			statement.setString(4, likeTerm);
			statement.setInt(5, limit);
			statement.setInt(6, offset);
			
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
		String sql = "SELECT COUNT(*) FROM doctor WHERE is_deleted = FALSE";
		
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
	
	public int getDoctorCount(String searchTerm) throws Exception {
		
		String sql = """
				SELECT COUNT(*)
				FROM doctor
				WHERE is_deleted = FALSE;
				AND (
					LOWER(first_name) LIKE ?
					OR LOWER(last_name) LIKE ?
					OR LOWER(specialization) LIKE ?
					OR LOWER(email) LIKE ?
				)
				""";
		
		String likeTerm = "%" + (searchTerm == null ? "" : searchTerm.toLowerCase()) + "%";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			
			preparedStatement.setString(1,  likeTerm);
			preparedStatement.setString(2,  likeTerm);
			preparedStatement.setString(3,  likeTerm);
			preparedStatement.setString(4,  likeTerm);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			return resultSet.getInt(1);
			
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
	
	public boolean softDeleteDoctor(int doctorId) throws Exception {
		
		LOGGER.info("Attempting to delete doctor with ID {}", doctorId);
		
		String sql = "UPDATE doctor SET is_deleted=TRUE, deleted_at=CURRENT_TIMESTAMP WHERE id=?";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			
			preparedStatement.setInt(1, doctorId);
			int rows = preparedStatement.executeUpdate();
			
			return rows > 0;
			
		} catch (SQLException e) {
			LOGGER.error("Failed to delete doctor with ID {} because {}", doctorId, e);
			throw new Exception("Unabled to delete", e);
		}
	}

	public void updateDoctor(Doctor doctor) throws Exception {
		
		LOGGER.info("Attempting to edit doctor's record in the database with ID {}", doctor.getId());
		
		String sql = "UPDATE doctor SET first_name=?, last_name=?, specialization=?, email=? WHERE id=?";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
						
			preparedStatement.setString(1, doctor.getFirstName());
			preparedStatement.setString(2, doctor.getLastName());
			preparedStatement.setString(3, doctor.getSpecialization());
			preparedStatement.setString(4, doctor.getEmail());
			preparedStatement.setInt(5, doctor.getId());
			
			preparedStatement.executeUpdate();
			
			LOGGER.info("Successfully updated the details of the doctor with ID {}", doctor.getId());
			
		} catch (SQLException e) {
			LOGGER.error("Failed to edit doctor's record.");
			throw new Exception("Unable to update doctor", e);
		}
	}
	
	public Doctor getDoctorById(int doctorId) throws Exception {
		
		LOGGER.info("Attempting to retrieve doctor by ID {}", doctorId);
		
		String sql = "SELECT id, first_name, last_name, specialization, email from doctor WHERE is_deleted = FALSE AND id=?";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			
			preparedStatement.setInt(1, doctorId);
			
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return new Doctor(
							resultSet.getInt("id"),
							resultSet.getString("first_name"),
							resultSet.getString("last_name"),
							resultSet.getString("specialization"),
							resultSet.getString("email")
							);
				}
				LOGGER.info("Successfully retrieved the doctor's record with ID {}", doctorId);
			}
		}
		
		LOGGER.error("Failed to load doctor with ID {}", doctorId);
		
		throw new Exception("Doctor not found with ID: " + doctorId);
	}
	
	public boolean checkIfEmailAlreadyExists(String email) throws Exception {
		
		LOGGER.info("Attempting to check if email {} already exists in our database", email);
		
		String sql = "SELECT COUNT(*) FROM doctor WHERE is_deleted = FALSE AND email=?";
		
		try (Connection connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			
			preparedStatement.setString(1, email);
			
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				
				resultSet.next();
				
				LOGGER.info("Email already exists in our database {}", email);
				
				return resultSet.getInt(1) > 0;
			}
			
		} catch (SQLException e) {
			
			LOGGER.error("Failed while checking if email already exists: {}", email, e);
			
			throw new Exception("Email existence check failed", e);
		}
		
	}
	
	public boolean emailExistForOtherDoctor(String email, int doctorId) throws Exception {
		
		LOGGER.info("Attempting to check if email exists for other doctrs {} {}", email, doctorId);
		
		String sql = "SELECT COUNT(*) FROM doctor WHERE is_deleted = FALSE AND email = ? AND id <> ?";
		
		try (Connection connection = dataSource.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			
			preparedStatement.setString(1, email);
			preparedStatement.setInt(2, doctorId);
			
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				
				resultSet.next();
				
				LOGGER.info("Successfully checked false positives");
				
				return resultSet.getInt(1) > 0;
			}
		} catch (SQLException e) {
			
			LOGGER.error("Failed checking duplicate email for update", e);
			
			throw new Exception("Duplicate email check failed", e);
		}
	}
}
