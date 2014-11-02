package com.sap.carAccident.servlets;

import java.io.BufferedReader;
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
import com.sap.carAccident.persistence.Accident;
import com.sap.carAccident.persistence.ThirdParty;



/**
 * Servlet implementation class AccidentServlet
 */
public class AccidentServlet extends HttpServlet {
	private static final String ACCIDENT_ID = "accidentId";
	private static final String DATE = "date";
	private static final String DESCRIPTION = "description";
	private static final String GEOLOCATION = "geolocation";
	private static final String TOWINGNEEDED = "towingneeded";
	private static final String CARREPLACEMENTNEEDED = "carreplacementneeded";
	private static final String INJURIES = "injuries";
	private static final String PHONENUMBER = "phonenumber";
	private static final String INSURANCEPOLICYNUMBER = "insurancepolicynumber";
	private static final String PLATENUMBER = "platenumber";
	private static final String NAME = "name";
	private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccidentServlet.class);

       
    private DataSource ds;
    private EntityManagerFactory emf;
	
   
    /** {@inheritDoc} */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void init() throws ServletException {
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
        	String accidentId = request.getParameter("accidentId");
        	if(accidentId != "Undefined" && accidentId != null && !accidentId.isEmpty()){
        		
        		getAccident(response, Integer.parseInt(accidentId));	
        	}
        	else{
        		getAllAccidents(response);
        	}
        	
        } catch (Exception e) {
            response.getWriter().println("Persistence operation failed with reason: " + e.getMessage());
            LOGGER.error("Persistence operation failed", e);
        }
		

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //Read JSon from request
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = request.getReader();
    try {
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append('\n');
        }
    } finally {
        reader.close();
    }
		EntityManager em = emf.createEntityManager();
	    JsonObject json = (JsonObject)new JsonParser().parse(sb.toString());

	    JsonObject jsonAccidentObject = json.getAsJsonObject("accident");
	    JsonPrimitive accidentIdJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("accidentId");
	    JsonPrimitive dateJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("date");
	    JsonPrimitive descriptionJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("description");
	    JsonPrimitive geolocationJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("geolocation");
	    JsonPrimitive towingneededJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("towingneeded");
	    JsonPrimitive carreplacementneededJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("carreplacementneeded");
	    JsonPrimitive injuriesJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("injuries");
	    
	    Accident carAccident =  new Accident();
	    
	    //get accident values from jsonPrimitivs
	    int accidentId = accidentIdJsonPrimitive.getAsInt();
	    String date = dateJsonPrimitive.getAsString();
	    java.sql.Date sqlFormatDate = java.sql.Date.valueOf(date);
	    String description = descriptionJsonPrimitive.getAsString();
	    String geolocation = geolocationJsonPrimitive.getAsString();
	    boolean isTowingneeded = towingneededJsonPrimitive.getAsBoolean();
	    boolean isCarreplacementneeded = carreplacementneededJsonPrimitive.getAsBoolean();
	    boolean isInjuries = injuriesJsonPrimitive.getAsBoolean();
	    
	    JsonArray thirdpartyJsonArray = jsonAccidentObject.getAsJsonArray("thirdparty");
	    ThirdParty thirdParty = null;
	    for(JsonElement thirdpartyElement : thirdpartyJsonArray)
	    {
	    	thirdParty =  new ThirdParty();
	    	JsonPrimitive nameJsonPrimitive = thirdpartyElement.getAsJsonObject().getAsJsonPrimitive("name");
	    	JsonPrimitive phonenumberJsonPrimitive = thirdpartyElement.getAsJsonObject().getAsJsonPrimitive("phonenumber");
	    	JsonPrimitive insurancepolicynumberJsonPrimitive = thirdpartyElement.getAsJsonObject().getAsJsonPrimitive("insurancepolicynumber");
	    	JsonPrimitive platenumberJsonPrimitive = thirdpartyElement.getAsJsonObject().getAsJsonPrimitive("platenumber");
	    	
	    	//get thirdPartyId values from jsonPrimitivs
	    	String name = nameJsonPrimitive.getAsString();
	    	String phoneNumber = phonenumberJsonPrimitive.getAsString();
	    	int insurancePolicyNumber = insurancepolicynumberJsonPrimitive.getAsInt();
	    	String plateNumber = platenumberJsonPrimitive.getAsString();
	    	
	    	
	    	
	    	thirdParty.setName(name);
	    	thirdParty.setAccidentId(accidentId);
	    	thirdParty.setThirdPartyId(System.currentTimeMillis());
	    	thirdParty.setPhoneNumber(phoneNumber);
	    	thirdParty.setInsurancePolicyNumber(insurancePolicyNumber);
	    	thirdParty.setPlateNumber(plateNumber);
	    	
	    	 // Save thirdParty in DB
		    em.getTransaction().begin();
	    	em.persist(thirdParty);
	        em.getTransaction().commit();
	    }
	    
	   
	    //set all properties to Accident instance
	    carAccident.setAccidentId(accidentId);
	    carAccident.setDate(sqlFormatDate);
	    carAccident.setDescription(description);
	    carAccident.setGeolocation(geolocation);
	    carAccident.setTowingNeeded(isTowingneeded);
	    carAccident.setCarreplacementNeeded(isCarreplacementneeded);
	    carAccident.setInjuries(isInjuries);
	    
	   
	    
	    // Save accident in DB
	    em.getTransaction().begin();
        em.persist(carAccident);
        em.getTransaction().commit();
	    
	}
	
	private void getAllAccidents(HttpServletResponse response) throws SQLException, IOException {
		EntityManager em = emf.createEntityManager();
		try 
        {
			@SuppressWarnings("unchecked")
			List<Accident> accidentResultList = em.createNamedQuery("GetAllAccidents").getResultList();
			JsonObject accidentsJSon = new JsonObject();
            JsonArray accidentsArray = new JsonArray();
            accidentsJSon.add("accidents", accidentsArray);
            
            for (Accident accident : accidentResultList) {
                // create JSon
                JsonObject accidentJSon = new JsonObject();
                accidentJSon.addProperty(ACCIDENT_ID, accident.getAccidentId());
                accidentJSon.addProperty(DATE, accident.getDate().toString());
                accidentJSon.addProperty(DESCRIPTION, accident.getDescription());
                accidentJSon.addProperty(GEOLOCATION, accident.getGeolocation());     
                accidentJSon.addProperty(TOWINGNEEDED, accident.getTowingNeeded());
                accidentJSon.addProperty(CARREPLACEMENTNEEDED,accident.getCarreplacementNeeded());
                accidentJSon.addProperty(INJURIES, accident.getInjuries());
                accidentsArray.add(accidentJSon);
                
                @SuppressWarnings("unchecked")
				List<ThirdParty> thirdpartyResultList = em.createNamedQuery("GetThirdPartiesForAccident").setParameter("accidentThirdPartyId", 1231).getResultList();
                JsonArray thirdpartyArray = new JsonArray();
                accidentJSon.add("thirdparty", thirdpartyArray);
                for (ThirdParty thirdparty : thirdpartyResultList) {
	                //Create thirdParty json objects inside accident object
	                JsonObject thirdpartyJSon = new JsonObject();
	                thirdpartyJSon.addProperty(NAME, thirdparty.getName());
	                thirdpartyJSon.addProperty(PHONENUMBER, thirdparty.getPhoneNumber());
	                thirdpartyJSon.addProperty(INSURANCEPOLICYNUMBER, thirdparty.getInsurancePolicyNumber());
	                thirdpartyJSon.addProperty(PLATENUMBER, thirdparty.getPlateNumber());
	                //Add thirdpartyJSonObject with  it's properties to the thirdParty array
	                thirdpartyArray.add(thirdpartyJSon);
                }
            }
            response.getWriter().println(accidentsJSon.toString());
           
        }
		finally {
            em.close();
        }
    }
	private void getAccident(HttpServletResponse response, int accidentId) throws SQLException, IOException {
		EntityManager em = emf.createEntityManager();
		try 
        {
			Accident accident = (Accident) em.createNamedQuery("GetAccidentById").setParameter("accidentId", accidentId).getSingleResult();
			JsonObject accidentOuterJSon = new JsonObject();
			JsonObject accidentJSon = new JsonObject();
			accidentOuterJSon.add("accident", accidentJSon);
             accidentJSon.addProperty(ACCIDENT_ID, accident.getAccidentId());
             accidentJSon.addProperty(DATE, accident.getDate().toString());
             accidentJSon.addProperty(DESCRIPTION, accident.getDescription());
             accidentJSon.addProperty(GEOLOCATION, accident.getGeolocation());     
             accidentJSon.addProperty(TOWINGNEEDED, accident.getTowingNeeded());
             accidentJSon.addProperty(CARREPLACEMENTNEEDED,accident.getCarreplacementNeeded());
             accidentJSon.addProperty(INJURIES, accident.getInjuries());
             System.out.println(accidentOuterJSon.toString());
             response.getWriter().println(accidentOuterJSon.toString());
        }
		finally {
            em.close();
        }
		
	}
}
