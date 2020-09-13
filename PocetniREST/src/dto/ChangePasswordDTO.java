package dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ChangePasswordDTO implements Serializable{

	private String userName;
	private String password;
	private String newPassword;
	
	public ChangePasswordDTO() {
		
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "ChangePasswordDTO [userName=" + userName + ", password=" + password + ", newPassword=" + newPassword
				+ "]";
	}
	
	
}
