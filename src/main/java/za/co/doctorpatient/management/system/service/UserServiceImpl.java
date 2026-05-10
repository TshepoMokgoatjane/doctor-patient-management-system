package za.co.doctorpatient.management.system.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import za.co.doctorpatient.management.system.dao.UserDAO;
import za.co.doctorpatient.management.system.model.User;

public class UserServiceImpl implements UserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	private UserDAO userDAO;
	
	public UserServiceImpl(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public User authenticate(String username, String password) throws Exception {
		
		LOGGER.info("Attempting to authenticate user in service layer");
		
		return userDAO.authenticate(username, password);
	}
	
	@Override
	public List<User> getAllUsers() throws Exception {
		return userDAO.getAllUsers();
	}
	
	@Override
	public int getUserCount() throws Exception {
		return userDAO.getUserCount();
	}
}
