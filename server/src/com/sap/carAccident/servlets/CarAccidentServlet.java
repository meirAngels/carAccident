package com.sap.carAccident.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sap.carAccident.userManagement.UserManagementUtils;

/**
 * Servlet implementation class CarAccidentServlet
 */
public class CarAccidentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private UserManagementUtils userManagementUtils  = new UserManagementUtils();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CarAccidentServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean userAdmin = userManagementUtils.isUserAdmin();
		response.getWriter().println("is user admin: " + userAdmin);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
