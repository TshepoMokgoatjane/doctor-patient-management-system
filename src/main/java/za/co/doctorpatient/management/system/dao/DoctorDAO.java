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
	
	public List<Doctor> getDoctors(int offset, int limit) throws Exception {
		
		LOGGER.info("Attempting to fetch a list of Doctors");
		
		List<Doctor> doctors = new ArrayList<>();
		
		String sql = """
				SELECT id, first_name, last_name, specialization, email 
				FROM doctor
				ORDER BY last_name
				LIMIT ? OFFSET ?
				""";
		
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
}
