package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.UserDAO;
import dto.ChangePasswordDTO;
import dto.ChangeUserDTO;
import model.User;

@Path("User")
public class UserService {

	@Context
	ServletContext context;
	
	public UserService() {
		
	}
	
	@PostConstruct
	public void init() {
		System.out.println("Usao u user init");
		if (context.getAttribute("users") == null) {
			System.out.println("Usao u if u user initu");
			context.setAttribute("users", new UserDAO(context.getRealPath("")));
		}
	}
	
	@PUT
	@Path("changePassword")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean changePassword(ChangePasswordDTO userInfo, @Context HttpServletRequest request) {
		
		UserDAO users = getUsers();
		User currentUser = users.findByUsernameAndPassword(userInfo.getUserName(), userInfo.getPassword());
		if (currentUser != null) {
			currentUser.setPassword(userInfo.getNewPassword());
			saveUsers(users);
			return true;
		} else {
			return false;
		}
		
	}
	
	@PUT
	@Path("changeUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean changeUser(ChangeUserDTO userDTO, @Context HttpServletRequest request) {
		
		UserDAO users = getUsers();
		User currentUser = users.findByUsernameAndPassword(userDTO.getPreviousUserName(), userDTO.getPassword());
		if (currentUser != null) {
			currentUser.setName(userDTO.getName());
			currentUser.setUserName(userDTO.getNewUserName());
			currentUser.setSurname(userDTO.getSurname());
			saveUsers(users);
			return true;
		} else {
			return false;
		}
	}
	
	@GET
	@Path("getUsers")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getAllUsers(@Context HttpServletRequest request) {
		
		UserDAO users = getUsers();
		ArrayList<User> usersToSend = new ArrayList<User>(users.findAll());
		return usersToSend;
	}
	
	public UserDAO getUsers() {
		UserDAO users = (UserDAO) context.getAttribute("users");
		return users;
	}
	
	public void saveUsers(UserDAO users) {
		users.saveUsers(context.getRealPath(""));
	}
}
