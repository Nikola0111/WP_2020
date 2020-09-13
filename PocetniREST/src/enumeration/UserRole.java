package enumeration;

import java.io.Serializable;

public enum UserRole implements Serializable{
	HOST,
    GUEST,
    ADMINISTRATOR;
    
    public static UserRole toEnum(String role) {
		switch(role.toUpperCase()) {
		case "ADMINISTRATOR":
			return ADMINISTRATOR;
		case "HOST":
			return HOST;
		case "GUEST":
			return GUEST;
		default:
			throw new IllegalArgumentException("User role " + role + "does not exist.");
		}
	}
}
