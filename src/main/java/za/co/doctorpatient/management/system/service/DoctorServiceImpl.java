package za.co.doctorpatient.management.system.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import za.co.doctorpatient.management.system.dao.DoctorDAO;
import za.co.doctorpatient.management.system.model.Doctor;

public class DoctorServiceImpl implements DoctorService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DoctorServiceImpl.class);
	
	private final DoctorDAO doctorDAO;
	
	public DoctorServiceImpl(DoctorDAO doctorDAO) {
		this.doctorDAO = doctorDAO;
	}
	
	public List<Doctor> getDoctorsByPage(int page, int pageSize) throws Exception {
		
		LOGGER.debug("Retrieving doctors for page {} with page size {}", page, pageSize);
		
		int offset = (page - 1) * pageSize;
		return doctorDAO.getDoctors(offset, pageSize);
	}
	
	public int getTotalPages(int pageSize) throws Exception {
		
		LOGGER.debug("Calculating total number of pages with page size {}", pageSize);
		
		int total = doctorDAO.getDoctorCount();
		return (int) Math.ceil((double) total / pageSize);
	}
}