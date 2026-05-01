package za.co.doctorpatient.management.system.service;

import za.co.doctorpatient.management.system.model.User;

public interface UserService {

	User authenticate(String username, String password) throws Exception;
}
