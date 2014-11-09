package com.sap.carAccident.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * Entity implementation class for Entity: Accident
 *
 */
@Entity
@Table(name="T_ACCIDENT")

@NamedQueries({ @NamedQuery(name = "getAllOpenAccidents", query = "Select a from Accident a Where a.claimStatus = :claimStatus"), 
			    @NamedQuery(name = "GetAccidentById",     query = "Select a from Accident a Where a.accidentId = :accidentId")})

public class Accident implements Serializable {

	@Id 
	private Integer accidentId;
	
	private String userName;
	
	@Temporal(TemporalType.DATE)
	private Date accidentDate;
	
	private String description;
	private String geolocation;
	
	@Lob
	private Byte[] damage;
	
	private boolean towingNeeded;
	@Temporal(TemporalType.DATE)
	private Date towingETA;

	private boolean carreplacementNeeded;
	
	@Temporal(TemporalType.DATE)
	private Date carReplacementETA;

	private boolean claimSentToInsurance;
	private boolean injuries;
	// Should be enum but there is problems with enum inside HCP
	private String claimStatus;
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
		return this.accidentDate;
	}

	public void setDate(Date date) {
		this.accidentDate = date;
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
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
   
}
