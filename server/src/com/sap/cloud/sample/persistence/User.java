package com.sap.cloud.sample.persistence;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@NamedQuery(name = "AllUsers", query = "Select p from User p")
@Table(name = "T_USER")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	public User() {
	}

	@Id
	@GeneratedValue
	private long id;
	private String firstName;
	private String secondName;
	private String phoneNumber;
	private String eMail;
	private String role;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String param) {
		this.firstName = param;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String param) {
		this.secondName = param;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String param) {
		this.phoneNumber = param;
	}

	public String getEMail() {
		return eMail;
	}

	public void setEMail(String param) {
		this.eMail = param;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String param) {
		this.role = param;
	}

}