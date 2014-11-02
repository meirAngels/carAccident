package com.sap.carAccident.persistence;

import java.io.Serializable;
import java.lang.Byte;
import java.lang.Integer;
import java.lang.String;
import java.sql.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Accident
 *
 */
@Entity
@Table(name="T_ACCIDENT")

@NamedQueries({ @NamedQuery(name = "GetAllAccidents", query = "Select a from Accident a"), 
			    @NamedQuery(name = "GetAccidentById", query = "Select a from Accident a Where a.accidentId = :accidentId")})

public class Accident implements Serializable {

	@Id
	private Integer accidentId;
	
	private String userName;
	private Date date;
	private String description;
	private String geolocation;
	private Byte[] damage;
	
	private boolean towingNeeded;
	private Date towingETA;

	private boolean carreplacementNeeded;
	private Date carReplacementETA;

	private boolean claimSentToInsurance;
	private boolean injuries;
	private ClaimStatus claimStatus;
	private int thirdPartyId;

	private static final long serialVersionUID = 1L;

	public Accident() {
		super();
	}   
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}   
	public Integer getAccidentId() {
		return this.accidentId;
	}

	public void setAccidentId(Integer accidentId) {
		this.accidentId = accidentId;
	}   
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}   
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}   
	public String getGeolocation() {
		return this.geolocation;
	}

	public void setGeolocation(String geolocation) {
		this.geolocation = geolocation;
	}   
	public Byte[] getDamage() {
		return this.damage;
	}

	public void setDamage(Byte[] damage) {
		this.damage = damage;
	}   
	public boolean getTowingNeeded() {
		return this.towingNeeded;
	}

	public void setTowingNeeded(boolean towingNeeded) {
		this.towingNeeded = towingNeeded;
	}   
	public boolean getCarreplacementNeeded() {
		return this.carreplacementNeeded;
	}

	public void setCarreplacementNeeded(boolean carreplacementNeeded) {
		this.carreplacementNeeded = carreplacementNeeded;
	}   
	public boolean getClaimSentToInsurance() {
		return this.claimSentToInsurance;
	}

	public void setClaimSentToInsurance(boolean claimSentToInsurance) {
		this.claimSentToInsurance = claimSentToInsurance;
	}   
	public Date getTowingETA() {
		return this.towingETA;
	}

	public void setTowingETA(Date towingETA) {
		this.towingETA = towingETA;
	}   
	public Date getCarReplacementETA() {
		return this.carReplacementETA;
	}

	public void setCarReplacementETA(Date carReplacementETA) {
		this.carReplacementETA = carReplacementETA;
	}   
	public boolean getInjuries() {
		return this.injuries;
	}

	public void setInjuries(boolean injuries) {
		this.injuries = injuries;
	}   
	public int getThirdPartyId() {
		return this.thirdPartyId;
	}

	public void setThirdPartyId(int thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}
	public String getClaimStatus() {
		return claimStatus.toString();
	}
	public void setClaimStatus(ClaimStatus claimStatus) {
		this.claimStatus = claimStatus;
	}
   
}
