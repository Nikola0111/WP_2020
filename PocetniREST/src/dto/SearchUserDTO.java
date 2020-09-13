package dto;

import java.io.Serializable;

import enumeration.UserGender;
import enumeration.UserRole;

@SuppressWarnings("serial")
public class SearchUserDTO implements Serializable{
	private UserRole userRole;
	private UserGender userGender;
	private String username;
	public SearchUserDTO(UserRole userRole, UserGender userGender, String username) {
		super();
		this.userRole = userRole;
		this.userGender = userGender;
		this.username = username;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
