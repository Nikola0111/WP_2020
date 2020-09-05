package model;

import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import enumeration.UserGender;
import enumeration.UserRole;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	private String id;
	private String userName;
	private String password;
	private String name;
	private String surname;
	private UserRole userRole;
	private UserGender userGender;
	private int availableApartments;
	private int rentedApartments;
	private ArrayList<String> reservationsIds;
	private boolean deleted;
	private boolean blocked;
	
	public User() {
		super();
		this.reservationsIds = new ArrayList<String>();
	}
	
	

	public User(String userName, String password, String name, String surname, UserRole userRole,
			UserGender userGender, int availableApartments, 
			ArrayList<String> reservationsIds,int rentedApartments,
			boolean deleted, boolean blocked) {
		super();
		UUID uuid = UUID.randomUUID();
		this.id = uuid.toString();
		this.userName = userName;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.userRole = userRole;
		this.userGender = userGender;
		this.availableApartments = availableApartments;
		this.rentedApartments = rentedApartments;
		this.reservationsIds = reservationsIds;
		this.rentedApartments = rentedApartments;
		this.deleted = deleted;
		this.blocked = blocked;
	}



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public int getAvailableApartments() {
		return availableApartments;
	}

	public void setAvailableApartments(int availableApartments) {
		this.availableApartments = availableApartments;
	}

	public int getRentedApartments() {
		return rentedApartments;
	}

	public void setRentedApartments(int rentedApartments) {
		this.rentedApartments = rentedApartments;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	public UserGender getUserGender() {
		return userGender;
	}

	public void setUserGender(UserGender userGender) {
		this.userGender = userGender;
	}



	public ArrayList<String> getReservationsIds() {
		return reservationsIds;
	}



	public void setReservations(ArrayList<String> reservationsIds) {
		this.reservationsIds = reservationsIds;
	}
	
	
}
	
	
