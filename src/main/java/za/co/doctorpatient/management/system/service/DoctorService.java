package za.co.doctorpatient.management.system.service;

import java.util.List;

import za.co.doctorpatient.management.system.model.Doctor;

public interface DoctorService {
	
	List<Doctor> getAllDoctors() throws Exception;
}
