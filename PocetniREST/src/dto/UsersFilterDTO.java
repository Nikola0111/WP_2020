package dto;

public class UsersFilterDTO {

	private String userRole;
	private String userGender;
	
	public UsersFilterDTO() {
		
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}
	
	
}
