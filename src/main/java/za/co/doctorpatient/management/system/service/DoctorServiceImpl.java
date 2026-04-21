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
	
	@Override
	public List<Doctor> getAllDoctors() throws Exception {
		
		LOGGER.info("Business request: retrieve all doctors");
		
		List<Doctor> doctors = doctorDAO.getDoctors();
		
		LOGGER.debug("Business result: {} doctors retrieved.", doctors.size());
		
		return doctors;
	}
}