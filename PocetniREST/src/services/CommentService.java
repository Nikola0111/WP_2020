package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.AmenityDAO;
import dao.ApartmentDAO;
import dao.CommentDAO;
import dao.ReservationDAO;
import dao.UserDAO;
import dto.CommentDTO;
import dto.CommentEligibilityDataDTO;
import dto.CommentForOneApartmentDTO;
import enumeration.ReservationStatus;
import model.Apartment;
import model.Comment;
import model.Reservation;
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
	@Path("saveCommentAndGrade")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean saveCommentAndGrade(CommentDTO commentDTO, @Context HttpServletRequest request) {
//		try {
			ApartmentDAO apartments = getApartments();
			Comment comment = new Comment(commentDTO.getCaption(), commentDTO.getContent(), commentDTO.getUserId(), commentDTO.getApartmentId(), commentDTO.getReservationId(), commentDTO.getRating(), false, false);
			//sa fronta se salje id apartmana koji je ocenjen i dodeljen mu komentar
	        Apartment current = apartments.getApartments().get(commentDTO.getApartmentId());
	        
	        CommentDAO comments = getComments();
	        
	        comment.setId((comments.getComments().size() + 1) + "");
	        comment.setShowed(false);
	        comment.setDeleted(false);
	        current.getGrades().add(comment.getRating());
	        current.getCommentIds().add(comment.getId());
	        
	        comments.getComments().put(comment.getId(), comment);
	        apartments.getApartments().put(current.getId(), current);
	        
	        saveComments(comments);
	    	saveApartments(apartments);
	    	
	    	return true;
//		} catch(Exception e) {
//			return false;
//		}
    }
	
	//metoda koja proverava da li je korisnik vec ocenio apartman
	//uzimaju se rezervacije korisnika, uzimaju se svi komentari
	//proverava se da li je id neke rezervacije iz lista rezervacija korisnika
	//jednak id-ju rezervacije kojoj pripada komentar
	
	@POST
	@Path("checkRatingEligibility")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String checkRatingEligibility(@Context HttpServletRequest request, CommentEligibilityDataDTO data) {
		
		ApartmentDAO apartments = getApartments();
		CommentDAO comments = getComments();
		
		List<Comment> apartmentsComments = new ArrayList<Comment>();
		Apartment apartment = apartments.find(data.getApartmentId());
		
		//Ubacujemo sve komentare apartmana u listu
		for(int i = 0; i < apartment.getCommentIds().size(); i++) {
			for(Map.Entry<String, Comment> entry : comments.getComments().entrySet()) {
				System.out.println("Porede se:" + entry.getValue().getId() + " i " + apartment.getCommentIds().get(i));
				if(entry.getValue().getId().equals(apartment.getCommentIds().get(i))) {
					apartmentsComments.add(entry.getValue());
					break;
				}
			}
		}

		System.out.println("Komentari na apartmane: " + apartmentsComments + "\n\n");
		
		List<Comment> apartmentsCommentsFromUser = new ArrayList<Comment>();
		
		for(Comment temp : apartmentsComments) {
			if(temp.getUserID().equals(data.getUserId())) {
				apartmentsCommentsFromUser.add(temp);
			}
		}
		
		System.out.println("Komentari na apartmane od strane korisnika: " + apartmentsCommentsFromUser + "\n\n");
		
		ReservationDAO reservations = getReservations();
		List<Reservation> usersReservationsToApartment = new ArrayList<Reservation>();
		
		for(Map.Entry<String, Reservation> entry: reservations.getReservations().entrySet()) {
			if(entry.getValue().getGuestId().equals(data.getUserId()) && 
					entry.getValue().getApartmentId().equals(data.getApartmentId())
					&& (entry.getValue().getReservationStatus() == ReservationStatus.ACCEPTED || entry.getValue().getReservationStatus() == ReservationStatus.DECLINED)) {
				usersReservationsToApartment.add(entry.getValue());
			}
		}
		
		System.out.println("Korisnikove rezervacije na apartman: " + usersReservationsToApartment + "\n\n");
		
		if(apartmentsCommentsFromUser.size() == 0 && usersReservationsToApartment.size() > 0) {
			return "0";
		}
		
		boolean hasComment = false;
		
		for(Reservation res : usersReservationsToApartment) {
			for(Comment com : apartmentsCommentsFromUser) {
				System.out.println("Porede se: " + res.getId() + " i " + com.getReservationId() + "\n\n");
				if(res.getId().equals(com.getReservationId())) {
					hasComment = true;
					break;
				}
			}
			
			if(!hasComment) {
				return res.getId();
			}
			hasComment = false;
		}
		
		return "-1";
	}
	
	@GET
	@Path("getHostsApartmentsComments/{hostId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<CommentForOneApartmentDTO> getHostsApartmentsComments(@PathParam("hostId") String hostId, @Context HttpServletRequest request) {
		List<Comment> hostsComments = new ArrayList<Comment>();
		ApartmentDAO apartments = getApartments();
		CommentDAO comments = getComments();
		UserDAO users = getUsers();
		System.out.println("Number of apartments:" + apartments.getApartments().size());
		
		List<Apartment> hostsApartments = new ArrayList<Apartment>();
		for(Map.Entry<String, Apartment> temp : apartments.getApartments().entrySet()) {
			System.out.println("Porede se: " + temp.getValue().getHostId() + "i " + hostId);
			if(temp.getValue().getHostId().equals(hostId)) {
				
				hostsApartments.add(temp.getValue());
			}
		}
		
		System.out.println("Number of hosts apartments:" + hostsApartments.size());
		
		for(Apartment temp: hostsApartments) {
			for(Map.Entry<String, Comment> entry : comments.getComments().entrySet()) {
				System.out.println("Porede se: " + entry.getValue().getApartmentId() + " i " + temp.getId());
				if(temp.getId().equals(entry.getValue().getApartmentId()) && !entry.getValue().isDeleted()) {
					hostsComments.add(entry.getValue());
				}
			}
		}
		
		List<CommentForOneApartmentDTO> ret = new ArrayList<CommentForOneApartmentDTO>();
		for(Comment comment : hostsComments) {
			CommentForOneApartmentDTO temp = new CommentForOneApartmentDTO();
			temp.setId(comment.getId());
			temp.setCaption(comment.getCaption());
			temp.setContent(comment.getContent());
			temp.setRating(comment.getRating());
			temp.setShowed(comment.isShowed());
			
			User commentUser = users.findById(comment.getUserID());
			
			temp.setGuestUserName(commentUser.getUserName());
			
			ret.add(temp);
		}
		
		System.out.println("Number of comments:" + hostsComments.size());
		
		return ret;
    }  
	
	@GET
	@Path("getComments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<CommentForOneApartmentDTO> getAllComments(@Context HttpServletRequest request){
		CommentDAO commentDAO = getComments();
		List<Comment> comments = new ArrayList<Comment>();
		
		for(Map.Entry<String, Comment> temp : commentDAO.getComments().entrySet()) {
			comments.add(temp.getValue());
		}
		
		UserDAO users = getUsers();
		
		List<CommentForOneApartmentDTO> ret = new ArrayList<CommentForOneApartmentDTO>();
		for(Comment comment : comments) {
			CommentForOneApartmentDTO temp = new CommentForOneApartmentDTO();
			temp.setId(comment.getId());
			temp.setCaption(comment.getCaption());
			temp.setContent(comment.getContent());
			temp.setRating(comment.getRating());
			temp.setShowed(comment.isShowed());
			
			User commentUser = users.findById(comment.getUserID());
			
			temp.setGuestUserName(commentUser.getUserName());
			
			ret.add(temp);
		}
		
		return ret;
	}
	
	@GET
	@Path("enableComment/{commentId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean enableComment(@PathParam("commentId") String commentId, @Context HttpServletRequest request) {
		
		try {
		CommentDAO commentDAO = getComments();
		Comment comment = commentDAO.find(commentId);
		
		if(comment != null) {
			comment.setShowed(true);
			saveComments(commentDAO);
			return true;
		}
			return false;
		} catch(Exception e) {
			return false;
		}
	}
	
	@GET
	@Path("disableComment/{commentId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean disableComments(@PathParam("commentId") String commentId, @Context HttpServletRequest request) {
		
		try {
		CommentDAO commentDAO = getComments();
		Comment comment = commentDAO.find(commentId);
		
		if(comment != null) {
			comment.setShowed(false);
			saveComments(commentDAO);
			return true;
		}
			return false;
		} catch(Exception e) {
			return false;
		}
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
	
	public ReservationDAO getReservations() { 
		ReservationDAO reservations = (ReservationDAO) context.getAttribute("reservations");
		return reservations;
	}
}
