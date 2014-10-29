package com.sap.caraccident.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.sample.persistence.PersistenceWithJPAServlet;
import com.sap.cloud.sample.persistence.User;
import com.sap.security.core.server.csi.IXSSEncoder;
import com.sap.security.core.server.csi.XSSEncoder;

/**
 * Servlet implementation class CarAccidentService
 */
public class CarAccidentService extends HttpServlet {
	
	private static final long serialVersionUID = 1L;   
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceWithJPAServlet.class);

    private DataSource ds;
    private EntityManagerFactory emf;
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CarAccidentService() {
        super();
        // TODO Auto-generated constructor stub
    }
 
    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void init() throws ServletException {
        Connection connection = null;
        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/jdbc/DefaultDB");

            Map properties = new HashMap();
            properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
            emf = Persistence.createEntityManagerFactory("server", properties);
        } catch (NamingException e) {
            throw new ServletException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        emf.close();
    }    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	response.getWriter().println("<p>Persistence with JPA Sample!</p>");
        try {
        	System.out.println("2");
        	appendAccidentTable(response);
        } catch (Exception e) {
            response.getWriter().println("Persistence operation failed with reason: " + e.getMessage());
            LOGGER.error("Persistence operation failed", e);
        }
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	
	private void appendAccidentTable(HttpServletResponse response) throws SQLException, IOException {
        // Append table that lists all users
        EntityManager em = emf.createEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<User> resultList = em.createNamedQuery("GetAllAccidents").getResultList();
            response.getWriter().println(
                    "<p><table border=\"1\"><tr><th colspan=\"3\">"
                            + (resultList.isEmpty() ? "" : resultList.size() + " ")
                            + "Accidents in the Database</th></tr>");
            if (resultList.isEmpty()) {
                response.getWriter().println("<tr><td colspan=\"3\">Database is empty</td></tr>");
            } else {
                response.getWriter().println("<tr><th>First name</th><th>Last name</th><th>Id</th></tr>");
            }
            IXSSEncoder xssEncoder = XSSEncoder.getInstance();
            for (User p : resultList) {
                response.getWriter().println(
                        "<tr><td>" + xssEncoder.encodeHTML(p.getFirstName()) + "</td><td>"
                                + xssEncoder.encodeHTML(p.getSecondName()) + "</td><td>" + p.getId() + "</td></tr>");
            }
            response.getWriter().println("</table></p>");
        } finally {
            em.close();
        }
    }	
	
	
}
