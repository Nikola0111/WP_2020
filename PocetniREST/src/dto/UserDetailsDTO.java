package dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserDetailsDTO implements Serializable{

	private String id;
	private String userName;
	private String name;
	private String surname;
	private String userGender;
	private int availableApartments;
	private int rentedApartments;
	private int numberOfReservationsMade;
	private String userRole;
	
	public UserDetailsDTO() {
		
	}

	public String getUserRole() {
		return userRole;
	}



	public void setUserRole(String userRole) {
		this.userRole = userRole;
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

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
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

	public int getNumberOfReservationsMade() {
		return numberOfReservationsMade;
	}

	public void setNumberOfReservationsMade(int numberOfReservationsMade) {
		this.numberOfReservationsMade = numberOfReservationsMade;
	}

	@Override
	public String toString() {
		return "UserDetailsDTO [id=" + id + ", userName=" + userName + ", name=" + name + ", surname=" + surname
				+ ", userGender=" + userGender + ", availableApartments=" + availableApartments + ", rentedApartments="
				+ rentedApartments + ", numberOfReservationsMade=" + numberOfReservationsMade + ", userRole=" + userRole
				+ "]";
	}
	
	
	
}
