package za.co.doctorpatient.management.system.exceptions;

import java.util.Map;

public class ValidationException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Map<String, String> errors;
	
	public ValidationException(Map<String, String> errors) {
		this.errors = errors;
	}
	
	public Map<String, String> getErrors() {
		return errors;
	}
}