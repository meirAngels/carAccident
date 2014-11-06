package com.sap.carAccident.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
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
import com.sap.carAccident.persistence.ClaimStatus;
import com.sap.carAccident.persistence.ThirdParty;
import com.sap.security.auth.login.LoginContextFactory;
import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.User;


/**
 * Servlet implementation class AccidentServlet
 */
public class AccidentServlet extends HttpServlet {
	private static final String ACCIDENT_ID = "accidentId";
	private static final String DATE = "date";
	private static final String DESCRIPTION = "description";
	private static final String GEOLOCATION = "geolocation";
	private static final String TOWINGNEEDED = "towingneeded";
	private static final String TOWINGETA = "towingETA";
	private static final String CARREPLACEMENTNEEDED = "carreplacementneeded";
	private static final String CARREPLACEMENTETA = "carReplacementETA";
	private static final String INJURIES = "injuries";
	private static final String PHONENUMBER = "phonenumber";
	private static final String INSURANCEPOLICYNUMBER = "insurancepolicynumber";
	private static final String PLATENUMBER = "platenumber";
	private static final String CLAIMSTATUS = "claimStatus"; 
	private static final String CLAIMSENTTOINSURANCE = "claimSentToInsurance"; 
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
		
		/*String user = request.getRemoteUser();
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
			response.getWriter().println("loged in user is " + currentUser.getName());
		} catch (PersistenceException e1) {
			e1.printStackTrace();
		}*/
		
		
	    // call get specific accident or all accidents
        try {
        	String accidentId = request.getParameter("accidentId");
        	if(accidentId != "Undefined" && accidentId != null && !accidentId.isEmpty()){
        		
        		getAccident(response, Integer.parseInt(accidentId));	
        	}
        	else{
        		getAllOpenAccidents(response);
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
		
		/*String user = request.getRemoteUser();
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
			response.getWriter().println("loged in user is " + currentUser.getName());
		} catch (PersistenceException e1) {
			e1.printStackTrace();
		}*/

    //Read JSon from request
    StringBuilder sb = new StringBuilder();
    BufferedReader reader = request.getReader();
    try {
        	String line;
        	while ((line = reader.readLine()) != null) {
        		sb.append(line).append('\n');
        	}
    	} 
    finally{
    		reader.close();
    	}
    
		EntityManager em = emf.createEntityManager();
	    JsonObject json = (JsonObject)new JsonParser().parse(sb.toString());

		String header = request.getHeader("OPERATION");
		if (header != null && !header.equals(Operation.CREATE.toString()))
		{
			perfromUpdate(request, response, json);
			return;
		}
		
		//set all properties to Accident instance	    
	    Accident carAccident =  new Accident();	   				
	    JsonObject jsonAccidentObject = json.getAsJsonObject("accident");
	    
	    int accidentId = 0;
	    JsonPrimitive accidentIdJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("accidentId");
	    if (accidentIdJsonPrimitive != null){
	    	accidentId = accidentIdJsonPrimitive.getAsInt();
	    	carAccident.setAccidentId(accidentId);
	    }	    
	    
	    JsonPrimitive dateJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("date");
	    if (dateJsonPrimitive != null){
	    	String strdate = dateJsonPrimitive.getAsString();
	    	DateFormat formatter = new SimpleDateFormat("d-MM-yyyy");
	    	try {
				java.util.Date date = formatter.parse(strdate);
				carAccident.setDate(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
	    }
	    
	    JsonPrimitive descriptionJsonPrimitive 	= jsonAccidentObject.getAsJsonPrimitive("description");
	    if (descriptionJsonPrimitive != null){
	    	String description = descriptionJsonPrimitive.getAsString();
		    carAccident.setDescription(description);   		    	
	    }
	    
	    JsonPrimitive geolocationJsonPrimitive 	= jsonAccidentObject.getAsJsonPrimitive("geolocation");
	    if( geolocationJsonPrimitive != null){
	    	String geolocation = geolocationJsonPrimitive.getAsString();
		    carAccident.setGeolocation(geolocation); 
	    }
	    
	    JsonPrimitive towingneededJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("towingneeded");
	    if (towingneededJsonPrimitive != null){
	    	boolean isTowingneeded = towingneededJsonPrimitive.getAsBoolean();
		    carAccident.setTowingNeeded(isTowingneeded);
	    }
	    
	    JsonPrimitive towingnETAJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("towingETA");
	    if (towingnETAJsonPrimitive != null){
	    	String towingETA = towingnETAJsonPrimitive.getAsString();
	    	java.sql.Date sqlFormatDate = java.sql.Date.valueOf(towingETA);
		    carAccident.setTowingETA(sqlFormatDate);
	    }
	    
	    JsonPrimitive carreplacementneededJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("carreplacementneeded");
	    if (carreplacementneededJsonPrimitive != null){
	    	 boolean isCarreplacementneeded = carreplacementneededJsonPrimitive.getAsBoolean();
	    	 carAccident.setCarreplacementNeeded(isCarreplacementneeded);
	    }	     
	    	    
	    JsonPrimitive carReplacementETAJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("carReplacementETA");
	    if (carReplacementETAJsonPrimitive != null){
	    	String strCarReplacementETA = carReplacementETAJsonPrimitive.getAsString();
	    	DateFormat formatter = new SimpleDateFormat("d-MM-yyyy");
	    	try {
				java.util.Date date = formatter.parse(strCarReplacementETA);
				carAccident.setDate(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
	    }	    
	    
	    JsonPrimitive injuriesJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("injuries");
	    if (injuriesJsonPrimitive != null){
		    boolean isInjuries = injuriesJsonPrimitive.getAsBoolean();
		    carAccident.setInjuries(isInjuries);		    
	    }	    
	    
	    JsonPrimitive claimSentToInsuranceJsonPrimitive = jsonAccidentObject.getAsJsonPrimitive("claimSentToInsurance");
	    if (claimSentToInsuranceJsonPrimitive != null){
	    	 boolean claimSentToInsurance = claimSentToInsuranceJsonPrimitive.getAsBoolean();
	    	 carAccident.setCarreplacementNeeded(claimSentToInsurance);
	    }
	    
	    JsonPrimitive claimStatusPrimitive = jsonAccidentObject.getAsJsonPrimitive("claimStatus");
	    if (claimStatusPrimitive != null){
	    	String claimStatus = claimStatusPrimitive.getAsString();
	    	carAccident.setClaimStatus(claimStatus);	    
	    }	    	  
	    else{
	    	carAccident.setClaimStatus("OPEN");
	    }
	    	    
	    JsonArray thirdpartyJsonArray = jsonAccidentObject.getAsJsonArray("thirdparty");
	    if (thirdpartyJsonArray != null){
	    //Set third Party
		    for(JsonElement thirdpartyElement : thirdpartyJsonArray)
		    {	
		    	setThirdPartyElement(em, accidentId, thirdpartyElement);
		    }	    	   	    	    
	    }
	    
	    // Save accident in DB
	    em.getTransaction().begin();
        em.persist(carAccident);
        em.getTransaction().commit();	    
	}

	private void setThirdPartyElement(EntityManager em, int accidentId,	JsonElement thirdpartyElement) {
				
		ThirdParty  thirdParty =  new ThirdParty();
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
	

	private void getAllOpenAccidents(HttpServletResponse response) throws SQLException, IOException {
		EntityManager em = emf.createEntityManager();
		try 
        {
			@SuppressWarnings("unchecked")
			List<Accident> accidentResultList = em.createNamedQuery("getAllOpenAccidents").setParameter("claimStatus", "OPEN").getResultList();
			JsonObject accidentsJSon = new JsonObject();
            JsonArray accidentsArray = new JsonArray();
            accidentsJSon.add("accidents", accidentsArray);
          
            for (Accident accident : accidentResultList) {
                // create JSon
                JsonObject accidentJSon = new JsonObject();
                accidentJSon.addProperty(ACCIDENT_ID,  accident.getAccidentId());
                accidentJSon.addProperty(DATE, 		   accident.getDate().toString());
                accidentJSon.addProperty(DESCRIPTION,  accident.getDescription());
                accidentJSon.addProperty(GEOLOCATION,  accident.getGeolocation());     
                accidentJSon.addProperty(TOWINGNEEDED, accident.getTowingNeeded());
                accidentJSon.addProperty(CLAIMSTATUS,  accident.getClaimStatus());
                accidentJSon.addProperty(CLAIMSENTTOINSURANCE,  accident.getClaimSentToInsurance());
                
                Date towingETA = accident.getTowingETA();
                String towingETAStr = "";
                if (towingETA != null)
                {
                	towingETAStr = towingETA.toString();
                }
                             
                accidentJSon.addProperty(TOWINGETA, towingETAStr);
                Date carReplacementETA = accident.getCarReplacementETA();
                String carReplacementETAStr = "";
                if (carReplacementETA != null)
                {
                	carReplacementETAStr = carReplacementETA.toString();
                }

                

                accidentJSon.addProperty(CARREPLACEMENTETA, carReplacementETAStr);
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
             accidentJSon.addProperty(CLAIMSTATUS,  accident.getClaimStatus());
             accidentJSon.addProperty(CLAIMSENTTOINSURANCE,  accident.getClaimSentToInsurance());
             System.out.println(accidentOuterJSon.toString());
             response.getWriter().println(accidentOuterJSon.toString());
        }
		finally {
            em.close();
        }
		
	}
	
	@SuppressWarnings("static-access")
	private void perfromUpdate(HttpServletRequest request, HttpServletResponse response, JsonObject requestJson) throws IllegalArgumentException, IOException 
	{
	    
	  //get Accident
	    JsonPrimitive accidentIdPrimitive = requestJson.getAsJsonPrimitive("accidentId"); 
	    EntityManager em = emf.createEntityManager();
	    @SuppressWarnings("rawtypes")
		List resultList = em.createNamedQuery("GetAccidentById").setParameter("accidentId", accidentIdPrimitive.getAsInt() ).getResultList();
		@SuppressWarnings("unchecked")
		List<Accident> accident = resultList;
	    
		
		if(accident.isEmpty()){
			 response.getWriter().println("accidentId " + accidentIdPrimitive.getAsInt() + " not found in DB");
			 return;
		}
		
	    Accident currAccident = accident.get(0);
	    
	    if(currAccident != null){
	    	    	   
	    	// branch according to update type
	    	Operation operation = Operation.valueOf(request.getHeader("OPERATION"));
	    	DateFormat formatter = null;
		    switch (operation) {
				case SET_TOWING_ETA:
					
					//get
					JsonPrimitive towingEtaPrimitive = requestJson.getAsJsonPrimitive("towingETA");		//towingNeeded
					
					//convert
					String strTowingEta = towingEtaPrimitive.getAsString();
				    formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			    	try {
						java.util.Date date = formatter.parse(strTowingEta);
						
						  //set
						currAccident.setTowingETA(date);													
						currAccident.setTowingNeeded(true);	
					} catch (ParseException e) {
						e.printStackTrace();
					}
		            
		            
		          
										
					break;
					
				case SET_CAR_REPLACEMENT_ETA:
					
					//get
					JsonPrimitive carReplacementETAPrimitive = requestJson.getAsJsonPrimitive("carReplacementETA");	
					
					//convert
		            String strCarReplacementETA =  carReplacementETAPrimitive.getAsString();
		            
		            formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			    	try {
						java.util.Date date = formatter.parse(strCarReplacementETA);
						  //set
						currAccident.setCarReplacementETA(date);
						currAccident.setCarreplacementNeeded(true);
					} catch (ParseException e) {
						e.printStackTrace();
					}
		            
					break;
				case SET_CLAIM_SENT:
					JsonPrimitive claimStatusSentPrimitive = requestJson.getAsJsonPrimitive("claimSentToInsurance");					
					Boolean claimStatusSent = claimStatusSentPrimitive.getAsBoolean();							
					currAccident.setClaimSentToInsurance(claimStatusSent);																			
					break;
					
				case SET_CLAIM_STATUS:
					JsonPrimitive claimetStatusPrimitive = requestJson.getAsJsonPrimitive("claimStatus");					
					String claimStatusPrimitive = claimetStatusPrimitive.getAsString(); 					
					currAccident.setClaimStatus(claimStatusPrimitive);
					break;
					
				default:
					break;										
					
			}
		    
		  //commit
			commitTransactionForAccident(em, currAccident);
	    }
	    
	}
	
	private void commitTransactionForAccident(EntityManager em, Accident carAccident) {
		
		em.getTransaction().begin();
        em.persist(carAccident);
        em.getTransaction().commit();
		
	}
}
