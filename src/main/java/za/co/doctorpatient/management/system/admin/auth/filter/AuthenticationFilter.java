package za.co.doctorpatient.management.system.admin.auth.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import za.co.doctorpatient.management.system.model.User;

/**
 * Authentication filter that ensures only logged-in users can access
 * protected resources. The LoginController and static resources are
 * excluded so users can still reach the login page.
 */
@WebFilter(urlPatterns = {"/DoctorController"})
public class AuthenticationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		HttpSession session = request.getSession(false);
		User user = (session != null) ? (User) session.getAttribute("loggedInUser") : null;

		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/LoginController");
			return;
		}

		chain.doFilter(request, response);
	}
}
