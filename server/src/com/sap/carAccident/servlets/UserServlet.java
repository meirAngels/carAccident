package com.sap.carAccident.servlets;

import java.io.IOException;
import java.util.Set;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.sap.security.auth.login.LoginContextFactory;
import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.User;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final String FIRSTNAME = "firstname";
	private static final String LASTNAME = "lastname";
	private static final String EMAIL = "email";
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getRemoteUser();
		if (user == null) {
			// if user is not logedon
			 LoginContext loginContext;
			 try {
				loginContext = LoginContextFactory.createLoginContext("FORM");
				loginContext.login();
				String userName = request.getUserPrincipal().getName();
			} catch (LoginException e) {
				e.printStackTrace();
			}
		}
		//if user is loged on      
		 String userName = request.getUserPrincipal().getName();
	     try {
			User currentUser = UserManagementAccessor.getUserProvider().getUser(userName);
			try {
				JsonObject currentUserJson = new JsonObject();
				currentUserJson.addProperty(FIRSTNAME,  currentUser.getAttribute("firstname"));
				currentUserJson.addProperty(LASTNAME,   currentUser.getAttribute("lastname"));
				currentUserJson.addProperty(EMAIL,  currentUser.getAttribute("email"));
				Set<String> rolestList = currentUser.getRoles();
				currentUserJson.addProperty("isDriver", false);
				currentUserJson.addProperty("isRepresentative", false);
				for ( String role : rolestList) {
					if(role.equals("driver")){
						currentUserJson.addProperty("isDriver", true);
					}
					if(role.equals("representative")){
						currentUserJson.addProperty("isRepresentative", true);
					}
				}
                response.getWriter().println(currentUserJson.toString());
			
			} catch (UnsupportedUserAttributeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (PersistenceException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getRemoteUser();
		if (user == null) {
			// if user is not logedon
			 LoginContext loginContext;
			 try {
				loginContext = LoginContextFactory.createLoginContext("FORM");
				loginContext.login();
			} catch (LoginException e) {
				e.printStackTrace();
			}
		}
		//if user is loged on      
		 String userName = request.getUserPrincipal().getName();
	     try {
			User currentUser = UserManagementAccessor.getUserProvider().getUser(userName);
		} catch (PersistenceException e1) {
			e1.printStackTrace();
		}
	}
}
