package za.co.doctorpatient.management.system.validation;

import java.util.HashMap;
import java.util.Map;

import za.co.doctorpatient.management.system.exceptions.ValidationException;
import za.co.doctorpatient.management.system.model.Doctor;

/**
 * Pure validation utility — performs field-level checks only.
 * Business rules (like duplicate email) belong in the service layer.
 */
public class ValidationUtility {
	
	public static void validateDoctor(Doctor doctor) throws ValidationException {
		
		Map<String, String> errors = new HashMap<>();
		
		if (doctor.getFirstName() == null || doctor.getFirstName().trim().isEmpty()) {
			errors.put("firstName", "First name is required!");
		}
		
		if (doctor.getLastName() == null || doctor.getLastName().trim().isEmpty()) {
			errors.put("lastName", "Last name is required!");
		}
		
		if (doctor.getSpecialization() == null || doctor.getSpecialization().trim().isEmpty()) {
			errors.put("specialization", "Specialization is required!");
		}
		
		if (doctor.getEmail() == null || doctor.getEmail().trim().isEmpty()) {
			errors.put("email", "Email is required!");
		} else if (!doctor.getEmail().contains("@")) {
			errors.put("email", "Invalid e-mail address!");
		}
		
		if (!errors.isEmpty()) {
			throw new ValidationException(errors);
		}
	}
}
