package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Address implements Serializable{

	private String street;
	private String number;
	private String city;
	private String postalCode;
	private String country;
	
	
	public Address() {
		super();
	}
	
	public Address(String street, String number, String city, String postalCode) {
		super();
		this.street = street;
		this.number = number;
		this.city = city;
		this.postalCode = postalCode;
	}
	
	
	
	public Address(String street, String number, String city, String postalCode, String country) {
		super();
		this.street = street;
		this.number = number;
		this.city = city;
		this.postalCode = postalCode;
		this.country = country;
	}

	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
}
