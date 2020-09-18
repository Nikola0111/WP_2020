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

import dao.AmenityDAO;
import dao.ApartmentDAO;
import dao.CommentDAO;
import dao.ReservationDAO;
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
		
		if (context.getAttribute("amenities") == null) {
			System.out.println("Inicijalizaovao amenities");
			context.setAttribute("amenities", new AmenityDAO(context.getRealPath("")));
		}
		
		if (context.getAttribute("apartments") == null) {
			context.setAttribute("apartments", new ApartmentDAO(context.getRealPath("")));
		}
		
		if (context.getAttribute("comments") == null) {
			context.setAttribute("comments", new CommentDAO(context.getRealPath("")));
		}
		
		if (context.getAttribute("reservations") == null) {
			context.setAttribute("reservations", new ReservationDAO(context.getRealPath("")));
		}
		
		if (context.getAttribute("users") == null) {
			context.setAttribute("users", new UserDAO(context.getRealPath("")));
		}
	
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean registerUser(RegisterDTO userDTO, @Context HttpServletRequest request) {

		UserDAO users = getUsers();
		ArrayList<User> usersList = new ArrayList<User>();

		if (users.getUsers().size() != 0) {
			usersList.addAll(users.findAll());
		}

		System.out.println(userDTO);

		if (!CheckNameAvailabity(userDTO.getUserName(), usersList)) {
			return false;
		}

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
		
		if (userDTO.getUserRole().equals("GUEST")) {
			newUser.setUserRole(UserRole.GUEST);
		} else {
			newUser.setUserRole(UserRole.HOST);
		}
		
		newUser.setAvailableApartments(0);
		newUser.setRentedApartments(0);
		users.getUsers().put(newUser.getId(), newUser);
		saveUsers(users);
		return true;

	}

	public boolean CheckNameAvailabity(String userName, ArrayList<User> usersList) {
		if (usersList.isEmpty()) {
			return true;
		}

		for (User currentUser : usersList) {
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
