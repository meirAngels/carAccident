package com.sap.carAccident.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.sap.carAccident.persistence.Accident;
import com.sap.security.core.server.csi.IXSSEncoder;
import com.sap.security.core.server.csi.XSSEncoder;



/**
 * Servlet implementation class AccidentServlet
 */
public class AccidentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccidentServlet.class);
       
    private DataSource ds;
    private EntityManagerFactory emf;
   
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
     * @see HttpServlet#HttpServlet()
     */
    public AccidentServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        try {
        	System.out.println("2");
        	getFromAccidentTable(response);
        } catch (Exception e) {
            response.getWriter().println("Persistence operation failed with reason: " + e.getMessage());
            LOGGER.error("Persistence operation failed", e);
        }
		

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String s = "{accident:{\"accidentId\":\"123456\", \"userId\":\"23455\", \"date\":\"October 10, 2014\", \"description\":\"Meir crashed the bus in Nepal\", \"geolocation\":\"26.5333 N, 86.7333 E\", \"damage\":[{ \"image1\" : \"/images/car1.gif\"},{ \"image1\" : \"/images/car2.gif\"},{ \"image1\" : \"/images/car3.gif\"}],\"thirdparty\":[{\"thirdpartyid\" : \"1234\", \"name\" : \"john\" , \"phonenumber\" : \"054-1234567\" , \"insurancepolicynumber\" : \"1234\" , \"platenumber\" : \"123-45-4554\"}]}}";
	    JsonObject json = (JsonObject)new JsonParser().parse(s);

	    JsonObject jsonAccidentObject = json.getAsJsonObject("accident");
	    JsonPrimitive accidentIdPrimitive = jsonAccidentObject.getAsJsonPrimitive("accidentId");
	    JsonPrimitive descriptionPrimitive = jsonAccidentObject.getAsJsonPrimitive("description");
	    Accident carAccident =  new Accident();
	    //get accident values from jsonPrimitivs
	    int accidentId = accidentIdPrimitive.getAsInt();
	    String description = descriptionPrimitive.getAsString();
	   
	  /*  String accidentDate = jsonPrimitive.getAsString();
	    Date date = null;
		try {
			date = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(accidentDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	    
	   
	    
	    //set all properties to Accident instance
	    carAccident.setAccidentId(accidentId);
	    carAccident.setDescription(description);
	    //carAccident.setDate(date);
	    
	    // Save accident in DB
	    EntityManager em = emf.createEntityManager();
	    em.getTransaction().begin();
        em.persist(carAccident);
        em.getTransaction().commit();
	    
	}
	
	private void getFromAccidentTable(HttpServletResponse response) throws SQLException, IOException {
        // Append table that lists all users
        EntityManager em = emf.createEntityManager();
        try 
        {
            @SuppressWarnings("unchecked")
            List<Accident> resultList = em.createNamedQuery("GetAllAccidents").getResultList();
            response.getWriter().println(
                    "<p><table border=\"1\"><tr><th colspan=\"3\">"
                            + (resultList.isEmpty() ? "" : resultList.size() + " ")
                            + "Accidents in the Database</th></tr>");
            if (resultList.isEmpty()) {
                response.getWriter().println("<tr><td colspan=\"3\">Database is empty</td></tr>");
            } 
            
            IXSSEncoder xssEncoder = XSSEncoder.getInstance();
            for (Accident acc : resultList) {
                response.getWriter().println(
                        "<tr><td>" + xssEncoder.encodeHTML(acc.getUserName()) + "</td><td>"
                                + xssEncoder.encodeHTML(acc.getDescription()) + "</td><td>" + acc.getAccidentId() + "</td></tr>");
            }
            response.getWriter().println("</table></p>");
        } 
        finally {
            em.close();
        }
    }	


}
