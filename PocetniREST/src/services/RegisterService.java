package services;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.UserDAO;
import dto.RegisterDTO;
import enumeration.UserGender;
import enumeration.UserRole;
import model.User;

@Path("Register")
public class RegisterService {

	@Context
	ServletContext context;
	
	public RegisterService() {
		
	}
	
	@PostConstruct
	public void init() {
		System.out.println("Usao u register init");
		if (context.getAttribute("users") == null) {
			System.out.println("Usao u if u register initu");
			context.setAttribute("users", new UserDAO(context.getRealPath("")));
		}
	}
	
	@POST
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean registerUser(RegisterDTO userDTO, @Context HttpServletRequest request) {
	
		UserDAO users = getUsers();
		ArrayList<User> usersList = new ArrayList<User>();

		if (users.getUsers().size() != 0) {
			usersList.addAll(users.findAll());
		}
		if (users.getUsers().size() == 0 ||CheckNameAvailabity(userDTO.getUserName(), usersList)) {
			User newUser = new User();
			newUser.setId(createId(usersList));
			newUser.setBlocked(false);
			newUser.setDeleted(false);
			newUser.setUserName(userDTO.getUserName());
			newUser.setPassword(userDTO.getPassword());
			newUser.setName(userDTO.getName());
			newUser.setSurname(userDTO.getSurname());
			
			if (userDTO.getUserGender().equals("MALE")) {
				newUser.setUserGender(UserGender.MALE);
			} else {
				newUser.setUserGender(UserGender.FEMALE);
			}
			newUser.setUserRole(UserRole.GUEST);
			newUser.setAvailableApartments(0);
			newUser.setRentedApartmaets(0);
			users.getUsers().put(newUser.getId(), newUser);
			saveUsers(users);
			System.out.println("Uspesna registracija");
			return true;
		} else {
			return false;
		}
		
	}
	
	public boolean CheckNameAvailabity(String userName, ArrayList<User> usersList) {
		if (usersList.isEmpty()) {
			return true;
		}
		
		for(User currentUser: usersList) {
			if (currentUser.getUserName().equals(userName)) {
				return false;
			}
		} 
		return true;
	}
	
	public UserDAO getUsers() {
		UserDAO users = (UserDAO) context.getAttribute("users");
		return users;
	}
	
	public void saveUsers(UserDAO users) {
		users.saveUsers(context.getRealPath(""));
	}
	public String createId(ArrayList<User> usersList) {
		String id = String.valueOf(usersList.size() + 1);
		return id;
	}
}