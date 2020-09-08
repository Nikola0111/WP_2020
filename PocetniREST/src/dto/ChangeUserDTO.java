package dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ChangeUserDTO implements Serializable{

	private String previousUserName;
	private String newUserName;
	private String password;
	private String name;
	private String surname;
	
	public ChangeUserDTO() {
		
	}

	public String getPreviousUserName() {
		return previousUserName;
	}



	public void setPreviousUserName(String previousUserName) {
		this.previousUserName = previousUserName;
	}



	public String getNewUserName() {
		return newUserName;
	}



	public void setNewUserName(String newUserName) {
		this.newUserName = newUserName;
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
	
	
	
}
