package com.sap.carAccident.persistence;

import java.io.Serializable;
import java.lang.String;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: EmergencyNumbers
 *
 */
@Entity
@Table(name="T_EMERGENCY_NUMBERS")
@NamedQuery(name = "GetAllEmergencyNumbers", query = "Select e from EmergencyNumbers e")
public class EmergencyNumbers implements Serializable {

	   
	@Id
	private String phoneNumber;
	private String name;
	private static final long serialVersionUID = 1L;

	public EmergencyNumbers() {
		super();
	}   
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
   
}
