package za.co.doctorpatient.management.system.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import za.co.doctorpatient.management.system.dao.UserDAO;
import za.co.doctorpatient.management.system.model.User;
import za.co.doctorpatient.management.system.service.UserService;
import za.co.doctorpatient.management.system.service.UserServiceImpl;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Injected from Tomcat context.xml
	 */
	@Resource(name = "jdbc/web_doctor_patient_management_system")
	private DataSource dataSource;
	
	private UserService userService;
	
	@Override
	public void init() throws ServletException {
		
		super.init();
		
		try {
			
			LOGGER.info("Initializing LoginController");
			
			UserDAO userDAO = new UserDAO(dataSource);
			userService = new UserServiceImpl(userDAO);
			
			LOGGER.info("Successfully initialized LoginController");
			
		} catch (Exception e) {
			LOGGER.error("Failed to initialise LoginController", e);
			throw new ServletException(e);
		}
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LOGGER.info("Attempting Login");
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		try {
			
			User user = userService.authenticate(username, password);
			
			if (user == null) {
				request.setAttribute("error", "Invalid username or password");
				request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
				return;
			}
			
			request.getSession().setAttribute("loggedInUser", user);
			response.sendRedirect(request.getContextPath() + "/DoctorController?command=LIST");
			
			LOGGER.info("Login successful for {}", username);
			
		} catch (Exception e) {
			
			LOGGER.error("User Login failed - {}", e);
			
			throw new ServletException(e);
		}
	}

}
