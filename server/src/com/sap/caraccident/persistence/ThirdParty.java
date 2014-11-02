package com.sap.carAccident.persistence;

import java.io.Serializable;
import java.lang.String;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ThirdParty
 *
 */
@Entity
@Table(name="T_THIRDPARTY")
@NamedQuery(name = "GetThirdPartiesForAccident", query = "Select t from ThirdParty t Where t.accidentId = :accidentThirdPartyId")
public class ThirdParty implements Serializable {
   
	@Id
	private long thirdPartyId;
	private int accidentId;
	private String name;
	private String phoneNumber;
	private int insurancePolicyNumber;
	private String plateNumber;
	private static final long serialVersionUID = 1L;

	public ThirdParty() {
		super();
	}
	
	public int getAccidentId() {
		return this.accidentId;
	}

	public void setAccidentId(int accidentId) {
		this.accidentId = accidentId;
	}
	
	public long getThirdPartyId() {
		return this.thirdPartyId;
	}

	public void setThirdPartyId(long thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
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
	public int getInsurancePolicyNumber() {
		return this.insurancePolicyNumber;
	}

	public void setInsurancePolicyNumber(int insurancePolicyNumber) {
		this.insurancePolicyNumber = insurancePolicyNumber;
	}   
	public String getPlateNumber() {
		return this.plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
   
}
