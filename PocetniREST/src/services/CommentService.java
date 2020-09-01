package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.ApartmentDAO;
import dao.CommentDAO;
import dao.UserDAO;
import dto.CommentForOneApartmentDTO;
import model.Apartment;
import model.Comment;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Path("comment")
public class CommentService {

	@Context
	ServletContext context;
	
	public CommentService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (context.getAttribute("comments") == null) {
			context.setAttribute("comments", new CommentDAO(context.getRealPath("")));
		}
		if (context.getAttribute("users") == null) {
			context.setAttribute("users", new CommentDAO(context.getRealPath("")));
		}
		if (context.getAttribute("apartments") == null) {
			context.setAttribute("apartments", new CommentDAO(context.getRealPath("")));
		}
	}
	
	@POST
	@Path("saveCommentAndGrade")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean saveCommentAndGrade(Comment comment, @Context HttpServletRequest request) {
		try {
			ApartmentDAO apartments = getApartments();
			User loggedUser = (User) request.getSession().getAttribute("loggedUser");
			//sa fronta se salje id apartmana koji je ocenjen i dodeljen mu komentar
	        Apartment current = apartments.getApartments().get(comment.getApartmentId());
	        
	        CommentDAO comments = getComments();
	        
	        comment.setId((comments.getComments().size() + 1) + "");
	        comment.setUserID(loggedUser == null ? "1" : loggedUser.getId());
	        
	        current.getGrades().add(comment.getRating());
	        current.getCommentIds().add(comment.getId());
	        
	        comments.getComments().put(comment.getId(), comment);
	        apartments.getApartments().put(current.getId(), current);
	        
	        saveComments(comments);
	    	saveApartments(apartments);
	    	
	    	return true;
		} catch(Exception e) {
			return false;
		}
    }
	
	//metoda koja proverava da li je korisnik vec ocenio apartman
	//uzimaju se rezervacije korisnika, uzimaju se svi komentari
	//proverava se da li je id neke rezervacije iz lista rezervacija korisnika
	//jednak id-ju rezervacije kojoj pripada komentar
	
	@POST
	@Path("getHostsApartmentsComments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getHostsApartmentsComments(@Context HttpServletRequest request) {
		List<Comment> hostsComments = new ArrayList<Comment>();
		ApartmentDAO apartments = getApartments();
		CommentDAO comments = getComments();
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		

		System.out.println("Number of apartments:" + apartments.getApartments().size());
		
		List<Apartment> hostsApartments = new ArrayList<Apartment>();
		for(Map.Entry<String, Apartment> temp : apartments.getApartments().entrySet()) {
			System.out.println("Porede se: " + temp.getValue().getHostId() + " i 1");
			if(temp.getValue().getHostId().equals("1")) {
				
				hostsApartments.add(temp.getValue());
			}
		}
		
		System.out.println("Number of hosts apartments:" + hostsApartments.size());
		
		for(Apartment temp: hostsApartments) {
			for(Map.Entry<String, Comment> entry : comments.getComments().entrySet()) {
				System.out.println("Porede se: " + entry.getValue().getApartmentId() + " i " + temp.getId());
				if(temp.getId().equals(entry.getValue().getApartmentId())) {
					hostsComments.add(entry.getValue());
				}
			}
		}
		
		System.out.println("Number of comments:" + hostsComments.size());
		
		return hostsComments;
    }  
	
	@POST
	@Path("getComments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getAllComments(@Context HttpServletRequest request){
		CommentDAO commentDAO = getComments();
		List<Comment> comments = new ArrayList<Comment>();
		
		for(Map.Entry<String, Comment> temp : commentDAO.getComments().entrySet()) {
			comments.add(temp.getValue());
		}
		
		return comments;
	}
	
	@GET
	@Path("apartmentComments/{apartmentId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<CommentForOneApartmentDTO> getApartmentComments(@PathParam("apartmentId") String apartmentId, @Context HttpServletRequest request) {
		
		UserDAO users = getUsers();
		CommentDAO comments = getComments();
		
		
		
		ArrayList<CommentForOneApartmentDTO> commentsToSend = new ArrayList<CommentForOneApartmentDTO>();
		ArrayList<Comment> commentsByApartmentId = comments.findAllByApartmentId(apartmentId);
		
		for (Comment comment : commentsByApartmentId) {
			User user = users.findById(comment.getUserID());
			commentsToSend.add(convertCommentToDTO(comment, user.getUserName()));
		}
		return commentsToSend;
		
	}
	
	public CommentForOneApartmentDTO convertCommentToDTO(Comment comment, String userName) {
		CommentForOneApartmentDTO dto = new CommentForOneApartmentDTO();
		
		dto.setId(comment.getId());
		dto.setCaption(comment.getCaption());
		dto.setContent(comment.getContent());
		dto.setGuestUserName(userName);
		dto.setRating(comment.getRating());
		return dto;
	}
	
	public CommentDAO getComments() {
		CommentDAO comments = (CommentDAO) context.getAttribute("comments");
        return comments;
    }
	
	public void saveComments(CommentDAO comments) {
		comments.SaveComments(context.getRealPath(""));
    }
	
	public ApartmentDAO getApartments() {
        ApartmentDAO apartments = (ApartmentDAO) context.getAttribute("apartments");
        return apartments;
    }
	
	public UserDAO getUsers() {
		UserDAO users = (UserDAO) context.getAttribute("users");
		return users;
	}
	
	public void saveApartments(ApartmentDAO apartments) {
		apartments.saveApartments(context.getRealPath(""));
    }
}
