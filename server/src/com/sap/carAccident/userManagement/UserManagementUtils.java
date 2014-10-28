package com.sap.carAccident.userManagement;

import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.User;

public class UserManagementUtils {
	
  public static final String CAR_ACCIDENT_ADMIN_ROLE_NAME = "carAccidentAdmin";

public boolean isUserAdmin (){
	try {
		User currentUser = UserManagementAccessor.getUserProvider().getCurrentUser();
		if (currentUser == null)
		{
			System.out.println("logged in user is NULL");
			return false;
		}
		
		System.out.println("loged in user is " + currentUser.getName());
		return currentUser.hasRole(CAR_ACCIDENT_ADMIN_ROLE_NAME);
		
	} catch (PersistenceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	return false ; 
  }
}
