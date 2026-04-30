package za.co.doctorpatient.management.system.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import za.co.doctorpatient.management.system.dao.DoctorDAO;
import za.co.doctorpatient.management.system.model.Doctor;
import za.co.doctorpatient.management.system.validation.ValidationUtility;

public class DoctorServiceImpl implements DoctorService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DoctorServiceImpl.class);
	
	private final DoctorDAO doctorDAO;
	
	public DoctorServiceImpl(DoctorDAO doctorDAO) {
		this.doctorDAO = doctorDAO;
	}
	
	public List<Doctor> getDoctorsByPage(int page, int pageSize, String sortField, String sortDir, String searchTerm) throws Exception {
		
		LOGGER.debug("Retrieving doctors for page {} with page size {}, sorted by {} {}", page, pageSize, sortField, sortDir);
		
		int offset = (page - 1) * pageSize;
		
		return doctorDAO.getDoctors(offset, pageSize, sortField, sortDir, searchTerm);
	}
	
	public int getTotalPages(int pageSize) throws Exception {
		
		LOGGER.debug("Calculating total number of pages with page size {}", pageSize);
		
		int total = doctorDAO.getDoctorCount();
		return (int) Math.ceil((double) total / pageSize);
	}

	@Override
	public void deleteDoctor(int doctorId) throws Exception {
		doctorDAO.softDeleteDoctor(doctorId);
	}
	
	@Override
	public int addDoctor(Doctor doctor) throws Exception {
		
		ValidationUtility.validateDoctor(doctor);
		
		ValidationUtility.validateNewDuplicateEmailChecks(doctor, doctorDAO);
		
		return doctorDAO.addDoctor(doctor);
	}
	
	@Override
	public void updateDoctor(Doctor doctor) throws Exception {
		
		ValidationUtility.validateDoctor(doctor);
		
		ValidationUtility.validateUpdateDuplicateEmailChecks(doctor, doctorDAO);

		doctorDAO.updateDoctor(doctor);
	}

	@Override
	public Doctor getDoctorById(int doctorId) throws Exception {
		return doctorDAO.getDoctorById(doctorId);
	}
}