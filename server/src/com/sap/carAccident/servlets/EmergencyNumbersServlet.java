package com.sap.carAccident.servlets;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.sap.carAccident.persistence.EmergencyNumbers;
import com.sap.security.core.server.csi.IXSSEncoder;
import com.sap.security.core.server.csi.XSSEncoder;

/**
 * Servlet implementation class EmergencyNumbersServlet
 */
public class EmergencyNumbersServlet extends HttpServlet {
	
	private static final String EMERGENCY_NUMBER_PHONE = "phoneNumber";
	private static final String EMERGENCY_NUMBER_NAME = "name";
	private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmergencyNumbersServlet.class);
       
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
    public EmergencyNumbersServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			getAllEmergencyNumbers(response);
		}
		catch(Exception e){
			response.getWriter().println("Persistence operation failed with reason: " + e.getMessage());
	        LOGGER.error("Persistence operation failed", e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String emergencyNumbers = "{\"emergencynumbers\": "+ 
		    "[" + 
		      "{\""+EMERGENCY_NUMBER_NAME+"\" : \"police\", \""+EMERGENCY_NUMBER_PHONE+"\" : \"101\"}, " +
		      "{\""+EMERGENCY_NUMBER_NAME+"\" : \"ambulance\", \""+EMERGENCY_NUMBER_PHONE+"\" : \"102\"} " +
		    "]}";

	    JsonObject emergencyNumbersJSon = (JsonObject)new JsonParser().parse(emergencyNumbers);

	    persistEmergencyNumber(emergencyNumbersJSon);

	}

	private void persistEmergencyNumber(JsonObject json) {
		JsonArray jsonEmergencyNumbersArray = json.getAsJsonArray("emergencynumbers");

		for (int i = 0; i < jsonEmergencyNumbersArray.size(); i++) 
		{
			JsonElement emergencyNumberElem = jsonEmergencyNumbersArray.get(i);
		    JsonPrimitive phoneNumberPrimitive = emergencyNumberElem.getAsJsonObject().getAsJsonPrimitive(EMERGENCY_NUMBER_PHONE);
		    JsonPrimitive namePrimitive        = emergencyNumberElem.getAsJsonObject().getAsJsonPrimitive(EMERGENCY_NUMBER_NAME);

		    EmergencyNumbers emergencyNumbers =  new EmergencyNumbers();
		    
		    //get emergency Numbers values from jsonPrimitivs
		    String phoneNumber = phoneNumberPrimitive.getAsString();
		    String name        = namePrimitive.getAsString();
		   	    
		    //set all properties to emergency Numbers instance
		    emergencyNumbers.setPhoneNumber(phoneNumber);
		    emergencyNumbers.setName(name);
		    
		    // Save emergency Numbers in DB
		    EntityManager em = emf.createEntityManager();
		    em.getTransaction().begin();
	        em.persist(emergencyNumbers);
	        em.getTransaction().commit();
		}
	}

	private void getAllEmergencyNumbers(HttpServletResponse response) throws SQLException, IOException {
        // Append table that lists all users
        EntityManager em = emf.createEntityManager();
        try 
        {
            @SuppressWarnings("unchecked")
            List<EmergencyNumbers> resultList = em.createNamedQuery("GetAllEmergencyNumbers").getResultList();

            JsonObject emergencyNumbersJSon = new JsonObject();
            JsonArray jsonEmergencyNumbersArray = new JsonArray();
            emergencyNumbersJSon.add("emergencyNumbers", jsonEmergencyNumbersArray);

            for (EmergencyNumbers emergencyNumber : resultList) {
                // create JSon
                JsonObject emergencyNumberJSon = new JsonObject();
                emergencyNumberJSon.addProperty(EMERGENCY_NUMBER_PHONE, emergencyNumber.getPhoneNumber());
                emergencyNumberJSon.addProperty(EMERGENCY_NUMBER_NAME, emergencyNumber.getName());
                jsonEmergencyNumbersArray.add(emergencyNumberJSon);
                
            }
            response.getWriter().println(emergencyNumbersJSon.toString());
        } 
        finally {
            em.close();
        }
    }		
	
}
