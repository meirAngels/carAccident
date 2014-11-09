package com.sap.carAccident.userManagement;

import java.util.Iterator;
import java.util.Set;

import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.UnsupportedUserAttributeException;
import com.sap.security.um.user.User;

public class UserManagementUtils {
	
  public static final String CAR_ACCIDENT_ADMIN_ROLE_NAME = "carAccidentAdmin";

public boolean isUserAdmin (String userName){
	try 
	{
		User currentUser = UserManagementAccessor.getUserProvider().getUser(userName);
		if (currentUser == null)
		{
			System.out.println("logged in user is NULL");
			return false;
		}
		
		System.out.println("loged in user is " + currentUser.getName());
		
		Set<String> roles = currentUser.getRoles();
		Iterator<String> iterator = roles.iterator();
		while (iterator.hasNext()) {
			String role = (String) iterator.next();
			System.out.println("user role = " + role);		
		}

		Set<String> attributes = currentUser.listAttributes();
		Iterator<String> attributesIter = attributes.iterator();
		while (attributesIter.hasNext()) {
			String attr = (String) attributesIter.next();
			String attrVal = currentUser.getAttribute(attr);
			System.out.println("user attr = " + attr + ", value= " + attrVal);		
		}

		
		return currentUser.hasRole(CAR_ACCIDENT_ADMIN_ROLE_NAME);
		
	} catch (PersistenceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (UnsupportedUserAttributeException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	return false ; 
  }
}
