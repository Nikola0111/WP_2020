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
import dto.ApartmentForFrontDTO;
import dto.CommentForOneApartmentDTO;
import enumeration.ApartmentType;
import enumeration.ReservationStatus;
import dto.ApartmentDTO;
import dto.ApartmentDetailsDTO;
import dto.FilterApartmentDTO;
import dto.SearchApartmentDTO;
import enumeration.UserRole;
import model.Amenity;
import model.Apartment;
import model.Comment;
import model.Reservation;
import model.User;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Path("apartments")
public class ApartmentService {

	@Context
	ServletContext context;
	
	public ApartmentService() {
		
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
	@Path("registerApartment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean registerApartment(@Context HttpServletRequest request, ApartmentDTO apartmentDTO) {
			ApartmentDAO apartments = getApartments();
	
			Apartment apartment = new Apartment(ApartmentType.APARTMENT, apartmentDTO.getNumberOfRooms(), apartmentDTO.getNumberOfGuests(),
					apartmentDTO.getLocation(), apartmentDTO.getStartDates(), apartmentDTO.getEndDates(), apartmentDTO.getPhotos(), apartmentDTO.getPricePerNight(),
					apartmentDTO.getCheckInTime(), apartmentDTO.getCheckOutTime());
			
			System.out.println(apartmentDTO);
			
			for(Amenity temp : apartmentDTO.getAmenities()) {
				apartment.getAmenityIds().add(temp.getId());
			}
			
			if(apartmentDTO.getApartmentType().equals("apartment")) {
				apartment.setApartmentType(ApartmentType.APARTMENT);
			} else {
				apartment.setApartmentType(ApartmentType.ROOM);
			}
	
			apartment.setHostId(apartmentDTO.getHostId());
			apartment.setId(apartments.getApartments().size() + 1 + "");
			
			apartments.getApartments().put(apartment.getId(), apartment);
			saveApartments(apartments);
			return true;
		
	}
	
	@POST
	@Path("editDetails")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean editDetails(@Context HttpServletRequest request, ApartmentDetailsDTO apartmentDTO) {
			ApartmentDAO apartments = getApartments();
			UserDAO users = getUsers();
			Apartment editedApartment = apartments.find(apartmentDTO.getId());
			User user = users.findByUsername(apartmentDTO.getUserName());
			///////////////ovde je za slike update///////////////////
			//editedApartment.setPhotos(apartmentDTO.getPhotos());
			/////////////////////////////////////////////////////////
			editedApartment.setLocation(apartmentDTO.getLocation());
			if(apartmentDTO.getApartmentType().toLowerCase().equals("apartment")) {
				editedApartment.setApartmentType(ApartmentType.APARTMENT);
			} else {
				editedApartment.setApartmentType(ApartmentType.ROOM);
			}
			editedApartment.setNumberOfGuests(apartmentDTO.getNumberOfGuests());
			editedApartment.setNumberOfRooms(apartmentDTO.getNumberOfRooms());
			editedApartment.setLocation(editedApartment.getLocation());
			editedApartment.setStartDates(apartmentDTO.getStartDates());
			editedApartment.setEndDates(apartmentDTO.getEndDates());
			editedApartment.setHostId(user.getId());
			for(CommentForOneApartmentDTO comment : apartmentDTO.getComments()) {
				editedApartment.getCommentIds().add(comment.getId());
			}
			editedApartment.setPricePerNight(apartmentDTO.getPricePerNight());
			if(apartmentDTO.getActivityStatus().toLowerCase().equals("active")) {
				editedApartment.setActivityStatus(true);
			} else {
				editedApartment.setActivityStatus(false);
			}
			editedApartment.setCheckInTime(apartmentDTO.getCheckInTime());
			editedApartment.setCheckOutTime(apartmentDTO.getCheckOutTime());
			editedApartment.setAmenityIds(new ArrayList<String>());
			for(Amenity temp : apartmentDTO.getAmenities()) {
				editedApartment.getAmenityIds().add(temp.getId());
			}
			
			apartments.getApartments().put(editedApartment.getId(), editedApartment);
			saveApartments(apartments);
			return true;
		
	}
	
	@POST
	@Path("searchApartment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Apartment> searchApartment(@Context HttpServletRequest request, SearchApartmentDTO apartmentDTO) {
		ApartmentDAO apartmentDAO = getApartments();
		
		return apartmentDAO.findBySearchApartmentDTOFields(apartmentDTO);
	}
	
	@POST
	@Path("filterApartments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Apartment> filterApartments(@Context HttpServletRequest request, FilterApartmentDTO apartmentDTO) {
		ApartmentDAO apartmentDAO = getApartments();
		//Koristnik filtrira apartmane samo po tipu i sadrzaju, jer im se uvek prikazuju samo aktivni apartmani, a admin moze i po statusu, mozda i host?
		List<Apartment> ret =  apartmentDAO.findByFilterApartmentDTOFields(apartmentDTO);
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		
		if(loggedUser.getUserRole() != UserRole.GUEST) {
			for(Apartment temp : ret) {
				if(temp.isActivityStatus() != apartmentDTO.isActivityStatus()) {
					ret.remove(temp);
				}
			}
		}
		
		return ret;
	}
	
	@GET
	@Path("ActiveApartments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ApartmentForFrontDTO> getAllActiveApartments(@Context HttpServletRequest request) {
		
		ApartmentDAO apartments = getApartments();
		UserDAO users = getUsers();
		
		ArrayList<ApartmentForFrontDTO> apartmentsToSend = new ArrayList<ApartmentForFrontDTO>();
		ArrayList<Apartment> apartmentsList = new ArrayList<Apartment>(apartments.findAll());
		
		for (Apartment apartment : apartmentsList) {
			if (apartment.isActivityStatus() && !apartment.isDeleted()) {
				User host = users.findById(apartment.getHostId());
				apartmentsToSend.add(convertApartmentToDTO(apartment, host));
			}
		}
		
		return apartmentsToSend;
	}
	
	@GET
	@Path("Apartments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ApartmentForFrontDTO> getAllApartments(@Context HttpServletRequest request) {
		ApartmentDAO apartments = getApartments();
		UserDAO users = getUsers();
		
		ArrayList<ApartmentForFrontDTO> apartmentsToSend = new ArrayList<ApartmentForFrontDTO>();
		ArrayList<Apartment> apartmentsList = new ArrayList<Apartment>(apartments.findAll());
		
		for (Apartment apartment : apartmentsList) {
			System.out.println(apartment.toString());
			User host = users.findById(apartment.getHostId());
			apartmentsToSend.add(convertApartmentToDTO(apartment, host));
		}
		
		return apartmentsToSend;
	}
	
	@GET
	@Path("HostActiveApartments/{hostId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ApartmentForFrontDTO> getAllActiveApartmentsFromOneHost(@PathParam("hostId") String hostId, @Context HttpServletRequest request) {
		
		ArrayList<ApartmentForFrontDTO> activeApartments = new ArrayList<ApartmentForFrontDTO>();
		ApartmentDAO apartments = getApartments();
		UserDAO users = getUsers();
		
		User host = users.findById(hostId);
		
		ArrayList<Apartment> activeApartmentsByHost = apartments.findAllByHostIdAndActivityStatus(hostId, true);
		
		for (Apartment apartment : activeApartmentsByHost) {
			ApartmentForFrontDTO dto = convertApartmentToDTO(apartment, host);
			activeApartments.add(dto);
		}
		return activeApartments;	
	}
	
	@GET
	@Path("HostApartments/{hostId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ApartmentForFrontDTO> getAllApartmentsFromOneHost(@PathParam("hostId") String hostId, @Context HttpServletRequest request) {
		System.out.println("Pogodio endpoint, id je: " + hostId);
		ArrayList<ApartmentForFrontDTO> activeApartments = new ArrayList<ApartmentForFrontDTO>();
		ApartmentDAO apartments = getApartments();
		UserDAO users = getUsers();
		
		User host = users.findById(hostId);
		
		ArrayList<Apartment> activeApartmentsByHost = apartments.findAllByHostId(hostId);
		System.out.println(activeApartmentsByHost);
		for (Apartment apartment : activeApartmentsByHost) {
			ApartmentForFrontDTO dto = convertApartmentToDTO(apartment, host);
			activeApartments.add(dto);
		}
		System.out.println(activeApartments);
		
		return activeApartments;	
	}
	
	@GET
	@Path("apartmentDetails/{apartmentId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApartmentDetailsDTO getApartmentDetails(@PathParam("apartmentId") String apartmentId, @Context HttpServletRequest request) {
		
		ApartmentDAO apartments = getApartments();
		UserDAO users = getUsers();
		ReservationDAO reservationsDAO = getReservations();
		AmenityDAO amenities = getAmenities();
		CommentDAO commentDAO = getComments();
		
		List<Reservation> apartmentReservations = new ArrayList<Reservation>();
		
		for(Map.Entry<String, Reservation> entry : reservationsDAO.getReservations().entrySet()) {
			if(entry.getValue().getApartmentId().equals(apartmentId) && !entry.getValue().isDeleted() && entry.getValue().getReservationStatus() == ReservationStatus.ACCEPTED) {
				apartmentReservations.add(entry.getValue());
			}
		}
		
		List<CommentForOneApartmentDTO> comments = new ArrayList<CommentForOneApartmentDTO>();
		
		for(Map.Entry<String, Comment> entry : commentDAO.getComments().entrySet()) {
			if(entry.getValue().getApartmentId().equals(apartmentId) && !entry.getValue().isDeleted()) {
				String username = users.findById(entry.getValue().getUserID()).getUserName();
				CommentForOneApartmentDTO temp = new CommentForOneApartmentDTO(entry.getValue().getId(), 
						entry.getValue().getCaption(), entry.getValue().getContent(), username, 
						entry.getValue().getRating(), entry.getValue().isShowed());
				
				comments.add(temp);
				
			}
		}
		
		Apartment apartment = apartments.find(apartmentId);
		
		List<Amenity> apartmentAmenities = new ArrayList<Amenity>();
		
		for(Map.Entry<String, Amenity> entry : amenities.getAmenities().entrySet()) {
			for(String amenityId : apartment.getAmenityIds()) {
				if(entry.getValue().getId().equals(amenityId)) {
					apartmentAmenities.add(entry.getValue());
					break;
				}
			}
		}
		
		
		User host = users.findById(apartment.getHostId());
		
		ApartmentDetailsDTO dto = convertToApartmentDetails(apartment, host);
		
		dto.setAmenities(apartmentAmenities);
		
		dto.setReservations(apartmentReservations);
		dto.setComments(comments);
		
		return dto;
	}
	
	@GET
	@Path("HostInactiveApartments/{hostId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ApartmentForFrontDTO> getAllInactiveApartmentsFromOneHost(@PathParam("hostId") String hostId, @Context HttpServletRequest request) {
		
		ArrayList<ApartmentForFrontDTO> inactiveApartments = new ArrayList<ApartmentForFrontDTO>();
		ApartmentDAO apartments = getApartments();
		UserDAO users = getUsers();
		
		User host = users.findById(hostId);
		
		ArrayList<Apartment> inactiveApartmentsByHost = apartments.findAllByHostIdAndActivityStatus(hostId, false);
		
		for (Apartment apartment : inactiveApartmentsByHost) {
			ApartmentForFrontDTO dto = convertApartmentToDTO(apartment, host);
			inactiveApartments.add(dto);
		}
		return inactiveApartments;	
	}
	
	@GET
	@Path("activateApartment/{apartmentId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean activateApartment(@PathParam("apartmentId") String apartmentId, @Context HttpServletRequest request) {
		
		try {
		ApartmentDAO apartmentDAO = getApartments();
		Apartment apartment = apartmentDAO.find(apartmentId);
		
		if(apartment != null) {
			apartment.setActivityStatus(true);
			saveApartments(apartmentDAO);
			return true;
		}
			return false;
		} catch(Exception e) {
			return false;
		}
	}
	
	@GET
	@Path("deactivateApartment/{apartmentId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deactivateApartment(@PathParam("apartmentId") String apartmentId, @Context HttpServletRequest request) {
		
		try {
		ApartmentDAO apartmentDAO = getApartments();
		Apartment apartment = apartmentDAO.find(apartmentId);
		
		if(apartment != null) {
			apartment.setActivityStatus(false);
			saveApartments(apartmentDAO);
			return true;
		}
			return false;
		} catch(Exception e) {
			return false;
		}
	}
	
	@GET
	@Path("deleteApartment/{apartmentID}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteApartment(@PathParam("apartmentID") String apartmentID, @Context HttpServletRequest request) {
		
		ApartmentDAO apartmentDAO = getApartments();
		if(apartmentDAO.getApartments().containsKey(apartmentID)) {
			apartmentDAO.getApartments().get(apartmentID).setDeleted(true);
			saveApartments(apartmentDAO);
			
			return true;
		}
		
		return false;
	}
	
	
	public ApartmentForFrontDTO convertApartmentToDTO(Apartment apartment, User host) {
		ApartmentForFrontDTO dto = new ApartmentForFrontDTO();
		
		dto.setId(apartment.getId());
		dto.setApartmentType(convertApartmentType(apartment.getApartmentType()));
		dto.setLocation(apartment.getLocation());
		dto.setHostUserName(host.getUserName());
		dto.setPricePerNight(apartment.getPricePerNight());
		dto.setStartDates(apartment.getStartDates());
		dto.setEndDates(apartment.getEndDates());
		
		if (apartment.isActivityStatus()) {
			dto.setActivityStatus("Active");
		} else {
			dto.setActivityStatus("Not active");
		}
		return dto;
	}
	
	public ApartmentDetailsDTO convertToApartmentDetails(Apartment apartment, User host) {
		ApartmentDetailsDTO dto = new ApartmentDetailsDTO();
		
		dto.setId(apartment.getId());
		if (apartment.getApartmentType().equals(ApartmentType.APARTMENT)) {
			dto.setApartmentType("Apartment");
		} else {
			dto.setApartmentType("Room");
		}
		dto.setNumberOfRooms(apartment.getNumberOfRooms());
		dto.setNumberOfGuests(apartment.getNumberOfGuests());
		dto.setLocation(apartment.getLocation());
		dto.setStartDates(apartment.getStartDates());
		dto.setEndDates(apartment.getEndDates());
		dto.setUserName(host.getUserName());
		dto.setPhotos(apartment.getPhotos());
		dto.setPricePerNight(apartment.getPricePerNight());
		dto.setCheckInTime(apartment.getCheckInTime());
		dto.setCheckOutTime(apartment.getCheckOutTime());
		if (apartment.isActivityStatus()) {
			dto.setActivityStatus("Active");
		} else {
			dto.setActivityStatus("Not active");
		}
		return dto;
	}
	
	public String convertApartmentType(ApartmentType type) {
		
		String convertedType = "";
		
		if (type.equals(ApartmentType.APARTMENT)) {
			convertedType = "Apartment";
		} else if (type.equals(ApartmentType.ROOM)) {
			convertedType = "Room";
		} 
		return convertedType;
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
	
	public AmenityDAO getAmenities() {
		AmenityDAO amenities = (AmenityDAO) context.getAttribute("amenities");
        return amenities;
    }
	
	public void saveAmenities(AmenityDAO amenities) {
		amenities.saveAmenities(context.getRealPath(""));
    }
	
	public ReservationDAO getReservations() { 
		ReservationDAO reservations = (ReservationDAO) context.getAttribute("reservations");
		return reservations;
	}
	
	public CommentDAO getComments() {
		CommentDAO comments = (CommentDAO) context.getAttribute("comments");
        return comments;
    }

}
