package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import dao.AmenityDAO;
import dao.ApartmentDAO;
import dao.CommentDAO;
import dao.ReservationDAO;
import dao.UserDAO;
import model.User;


@Path("login")
public class LoginService {

	@Context
	ServletContext context;
	
	public LoginService() {
		
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
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User login(User user, @Context HttpServletRequest request) {
		
		UserDAO users = (UserDAO) context.getAttribute("users");
		User loggedUser = users.findByUsernameAndPassword(user.getUserName(), user.getPassword());
		
		if (loggedUser.getBlocked()) {
			return null;
		}
		
		request.getSession().setAttribute("loggedUser", loggedUser);
		
		return loggedUser;	
	}
	
	@GET
	@Path("logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User logout(@Context HttpServletRequest request) {
		AmenityDAO amenities = (AmenityDAO) context.getAttribute("amenities");
		ApartmentDAO apartments = (ApartmentDAO) context.getAttribute("apartments");
		CommentDAO comments = (CommentDAO) context.getAttribute("comments");
		ReservationDAO reservations = (ReservationDAO) context.getAttribute("reservations");
		UserDAO users = (UserDAO) context.getAttribute("users");
		
		amenities.saveAmenities(context.getRealPath(""));
		apartments.saveApartments(context.getRealPath(""));
		comments.SaveComments(context.getRealPath(""));
		reservations.saveReservations(context.getRealPath(""));
		users.saveUsers(context.getRealPath(""));
		
		User user = (User) request.getSession().getAttribute("loggedUser");
		if (user == null) {
			System.out.println("NULL JE PEDERCINA!");
		}
		System.out.println("Sesija: " + request.getRequestedSessionId());
		User userToSend = new User();
		userToSend.setUserName(user.getUserName());
		request.getSession().invalidate();
		System.out.println("Someone logged out successfully!");
		System.out.println("Odjava:" + userToSend.toString());
		return userToSend;
	}
	
	@GET
	@Path("loggedUser")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public User getLoggedUser(@Context HttpServletRequest request) {
				
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		System.out.println("Sesija kada gettujem ulogovanog usera: " + request.getRequestedSessionId());
		System.out.println(loggedUser.toString());
		
		return loggedUser;
	}
	
}
