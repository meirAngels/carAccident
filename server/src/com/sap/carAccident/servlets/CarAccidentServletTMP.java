package com.sap.carAccident.servlets;

import java.io.IOException;
import java.security.Principal;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.carAccident.userManagement.UserManagementUtils;
import com.sap.security.auth.login.LoginContextFactory;
import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

/**
 * Servlet implementation class CarAccidentServlet
 */
public class CarAccidentServletTMP extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserManagementUtils userManagementUtils  = new UserManagementUtils();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CarAccidentServletTMP() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			login();
			response.getWriter().println("<p>Welcome " + getUserAttributes(request.getUserPrincipal()) + "</p>");
			boolean userAdmin = userManagementUtils.isUserAdmin(request.getUserPrincipal().getName());
			response.getWriter().println("is user admin: " + userAdmin);
		} catch (PersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedUserAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	private String getUserAttributes(Principal principal) throws PersistenceException, UnsupportedUserAttributeException {
		// Get user from user storage based on principal name
		UserProvider userProvider = UserManagementAccessor.getUserProvider();
		User user = userProvider.getUser(principal.getName());
		
		// Extract and return user name and e-mail address if present
		String firstName = user.getAttribute("firstname");
		String lastName = user.getAttribute("lastname");
		String eMail = user.getAttribute("email");
		return (firstName != null && lastName != null ? firstName + " " + lastName + " [" + principal.getName() + "]"
		        : principal.getName()) + (eMail != null ? " (" + eMail + ")" : "");
	}
	
	 private void login() throws LoginException {
	        LoginContext loginContext = LoginContextFactory.createLoginContext();
	        loginContext.login();
	    }

}
