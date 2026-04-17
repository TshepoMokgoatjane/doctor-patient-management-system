package za.co.doctorpatient.management.system.web.jdbc.test.connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// Define datasource/connection pool for Resource Injection
	@Resource(name="jdbc/web_doctor_patient_management_system")
	private DataSource dataSource;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Step 1: Set up the printWriter
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");
		
			// Step 3: Create a SQL statement
			String sql = "SELECT * FROM doctor";
		
		// Step 2: Get a connection to the database
		try (Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql)) {
			
			// Step 4: Execute SQL query
			
			// Step 5: Process the result set
			while (resultSet.next()) {
				String email = resultSet.getString("email");
				out.println("Email: " + email);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
