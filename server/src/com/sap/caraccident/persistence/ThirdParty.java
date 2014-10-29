package com.sap.caraccident.persistence;

import java.io.Serializable;
import java.lang.String;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: ThirdParty
 *
 */
@Entity
@Table(name="T_THIRDPARTY")
@NamedQuery(name = "GetThirdPartiesForAccident", query = "Select t from ThirdParty t Where t.thirdPartyId = :accidentThirdPartyId")
public class ThirdParty implements Serializable {
   
	@Id
	private int thirdPartyId;
	private String phoneNumber;
	private int insurancePolicyNumber;
	private int plateNumber;
	private static final long serialVersionUID = 1L;

	public ThirdParty() {
		super();
	}   
	public int getThirdPartyId() {
		return this.thirdPartyId;
	}

	public void setThirdPartyId(int thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}   
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}   
	public int getInsurancePolicyNumber() {
		return this.insurancePolicyNumber;
	}

	public void setInsurancePolicyNumber(int insurancePolicyNumber) {
		this.insurancePolicyNumber = insurancePolicyNumber;
	}   
	public int getPlateNumber() {
		return this.plateNumber;
	}

	public void setPlateNumber(int plateNumber) {
		this.plateNumber = plateNumber;
	}
   
}
