package za.co.doctorpatient.management.system.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import za.co.doctorpatient.management.system.dao.DoctorDAO;
import za.co.doctorpatient.management.system.model.Doctor;
import za.co.doctorpatient.management.system.service.DoctorService;
import za.co.doctorpatient.management.system.service.DoctorServiceImpl;

/**
 * Servlet implementation class DoctorController
 */
@WebServlet(description = "Handles all Web HTTP Request and Response in the Application.", urlPatterns = { "/DoctorController" })
public class DoctorController extends HttpServlet {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DoctorController.class);
	
	private static final long serialVersionUID = 1L;	
	
	/**
	 * Injected from Tomcat context.xml
	 */
	@Resource(name = "jdbc/web_doctor_patient_management_system")
	private DataSource dataSource;
	
	
	private DoctorService doctorService;
	
	// --------------------------------------------------------
	// Servlet lifecycle
	// --------------------------------------------------------
	@Override
	public void init() throws ServletException {
		super.init();
		
		try {
			LOGGER.info("Initializing DoctorController");
			
			DoctorDAO doctorDAO = new DoctorDAO(dataSource);
			doctorService = new DoctorServiceImpl(doctorDAO);
		} catch (Exception e) {
			LOGGER.error("Failed to initialise DoctorServletController", e);
			throw new ServletException(e);
		}
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DoctorController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * HTTP entry points
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String command = request.getParameter("command");
		
		if (command == null) {
			command = "LIST";
		}
		
		LOGGER.info("DoctorController received command: {}", command);
		
		try {
			switch (command) {
				case "SHOW_ADD_DOCTOR_FORM":
					showAddDoctorForm(request, response);
					break;
				case "ADD":
					addDoctor(request, response);
					break;
				case "LIST":
					listDoctors(request, response);
					break;
				case "DELETE":
					deleteDoctor(request, response);
					break;
				case "LOAD":
					loadDoctor(request, response);
					break;
				case "UPDATE":
					updateDoctor(request, response);
					break;
				default:
					LOGGER.warn("Unknown command '{}', defaulting to LIST", command);
					listDoctors(request, response);
			}
		} catch (Exception e) {
			LOGGER.error("Error processing DoctorServletController request", e);
			throw new ServletException(e);
		}
	}
	
	private void loadDoctor(HttpServletRequest request, HttpServletResponse response) throws Exception {

		int doctorId = Integer.parseInt(request.getParameter("doctorId"));
		
		Doctor doctor = doctorService.getDoctorById(doctorId);
		
		request.setAttribute("doctor", doctor);
		
		request.getRequestDispatcher("/WEB-INF/views/edit-doctor-form.jsp").forward(request, response);
		
	}

	private void updateDoctor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int id = Integer.parseInt(request.getParameter("doctorId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String specialization = request.getParameter("specialization");
		String email = request.getParameter("email");
		
		Doctor doctor = new Doctor(id, firstName, lastName, specialization, email);
		
		doctorService.updateDoctor(doctor);
		
		// PRG pattern
		response.sendRedirect(request.getContextPath() + "/DoctorController?command=LIST&success=updated");
		
	}

	private void addDoctor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String specialization = request.getParameter("specialization");
		String email = request.getParameter("email");
		
		Doctor doctor = new Doctor(firstName, lastName, specialization, email);
		
		doctorService.addDoctor(doctor);
		
		// PRG pattern
		response.sendRedirect(request.getContextPath() + "/DoctorController?command=LIST&success=added");
	}

	private void showAddDoctorForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/add-doctor-form.jsp");
		
		requestDispatcher.forward(request, response);
		
	}

	private void deleteDoctor(HttpServletRequest request, HttpServletResponse response) throws Exception {

		int doctorId = Integer.parseInt(request.getParameter("doctorId"));
		int page = Integer.parseInt(request.getParameter("page"));
		String sortField = request.getParameter("sortField");
		String sortDir = request.getParameter("sortDir");
		
		doctorService.deleteDoctor(doctorId);
		
		// PRG pattern + state preservation
		response.sendRedirect(request.getContextPath() + "/DoctorController?command=LIST" +
		"&page=" + page + "&sortField=" + sortField + "&sortDir=" + sortDir + "&success=deleted");
		
	}

	// -------------------------------------------------------
	// Command handlers
	// -------------------------------------------------------
	private void listDoctors(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		LOGGER.debug("Handling LIST doctors request");
		
		// Pagination configuration
		int page = 1;
		int pageSize = 5; // doctors per page
		
		// Read page parameter
		String pageParam = request.getParameter("page");
		if (pageParam != null) {
			try {
				page = Integer.parseInt(pageParam);
			} catch (NumberFormatException e) {
				page = 1;
			}
		}
		
		// Sorting parameters
		String sortField = request.getParameter("sortField");
		String sortDir = request.getParameter("sortDir");
		
		if (sortField == null) {
			sortField = "lastName";
		}
		
		if (sortDir == null) {
			sortDir = "asc";
		}
		
		// Toggle direction for UI
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		// Fetch paginated data
		List<Doctor> doctors = doctorService.getDoctorsByPage(page, pageSize, sortField, sortDir);
		
		// Calculate total pages
		int totalPages = doctorService.getTotalPages(pageSize);
		
		// Expose to JSP
		request.setAttribute("doctors", doctors);
		request.setAttribute("currentPage", page);
		request.setAttribute("totalPages",  totalPages);
		request.setAttribute("sortField", sortField);
		request.setAttribute("sortDir", sortDir);
		request.setAttribute("reverseSortDir", reverseSortDir);
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/list-doctors.jsp");
		
		requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
