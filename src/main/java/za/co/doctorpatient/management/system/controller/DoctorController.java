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
 * Servlet implementation class DoctorServletController
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
			LOGGER.info("Initializing DoctorServletController");
			
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
		
		LOGGER.info("DoctorServletController received command: {}", command);
		
		try {
			switch (command) {
				case "LIST":
					listDoctors(request, response);
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
		
		// Fetch paginated data
		List<Doctor> doctors = doctorService.getDoctorsByPage(page, pageSize);
		
		// Calculate total pages
		int totalPages = doctorService.getTotalPages(pageSize);
		
		// Expose to JSP
		request.setAttribute("doctors", doctors);
		request.setAttribute("currentPage", page);
		request.setAttribute("totalPages",  totalPages);
		
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
