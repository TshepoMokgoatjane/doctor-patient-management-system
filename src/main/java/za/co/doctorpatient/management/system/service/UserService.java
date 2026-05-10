package za.co.doctorpatient.management.system.service;

import java.util.List;

import za.co.doctorpatient.management.system.model.User;

public interface UserService {

	User authenticate(String username, String password) throws Exception;
	
	List<User> getAllUsers() throws Exception;
	
	int getUserCount() throws Exception;
}
