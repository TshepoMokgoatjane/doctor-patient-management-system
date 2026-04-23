package za.co.doctorpatient.management.system.service;

import java.util.List;

import za.co.doctorpatient.management.system.model.Doctor;

public interface DoctorService {

	List<Doctor> getDoctorsByPage(int page, int pageSize, String sortField, String sortDir) throws Exception;

	int getTotalPages(int pageSize) throws Exception;

	void deleteDoctor(int doctorId);
	
	int addDoctor(Doctor doctor) throws Exception;
}
