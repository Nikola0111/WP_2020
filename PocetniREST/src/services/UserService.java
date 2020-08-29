package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.UserDAO;
import dto.ChangePasswordDTO;
import model.User;

@Path("User")
public class UserService {

	@Context
	ServletContext context;
	
	public UserService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (context.getAttribute("users") == null) {
			context.setAttribute("users", new UserDAO(context.getRealPath("")));
		}
	}
	
	@PUT
	@Path("changePassword")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean changePassword(ChangePasswordDTO userInfo, @Context HttpServletRequest request) {
		
		UserDAO users = getUsers();
		User currentUser = users.find(userInfo.getUserName(), userInfo.getPassword());
		if (currentUser != null) {
			currentUser.setPassword(userInfo.getNewPassword());
			users.saveUsers(context.getRealPath(""));
			return true;
		} else {
			return false;
		}
		
	}
	
	public UserDAO getUsers() {
		UserDAO users = (UserDAO) context.getAttribute("users");
		return users;
	}
}
