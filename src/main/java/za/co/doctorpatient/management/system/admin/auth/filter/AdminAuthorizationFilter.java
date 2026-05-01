package za.co.doctorpatient.management.system.admin.auth.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import za.co.doctorpatient.management.system.model.User;

@WebFilter(urlPatterns = {"/DoctorController"})
public class AdminAuthorizationFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
				
		String command = request.getParameter("command");
		
		if (requiresAdmin(command)) {
			
			HttpSession session = request.getSession(false);
			
			User user = (session != null) ? (User) session.getAttribute("loggedInUser") : null;
			
			if (user == null || !user.isAdmin()) {
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/access-denied.jsp");
				requestDispatcher.forward(request, response);
				return;
			}
		}
		chain.doFilter(request, response);
	}
	
	private boolean requiresAdmin(String command) {
		return "DELETE".equals(command)
				|| "UPDATE".equals(command)
				|| "ADMIN_DASHBOARD".equals(command)
				|| "VIEW_DELETED".equals(command);
	}
}
