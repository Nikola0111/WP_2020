package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import model.Amenity;
import model.Apartment;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@Path("Amenity")
public class AmenityService {

	@Context
	ServletContext context;
	
	public AmenityService() {
		
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
	
	@GET
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Amenity> getAllAmenities(@Context HttpServletRequest request) {
		
		AmenityDAO amenities = getAmenities();
		ArrayList<Amenity> allAmenities = new ArrayList<Amenity>(amenities.findAll());
		
		ArrayList<Amenity> amenitiesToSend = new ArrayList<Amenity>();
		
		for (Amenity amenity : allAmenities) {
			if (!amenity.isDeleted()) {
				amenitiesToSend.add(amenity);
			}
		}
		return amenitiesToSend;
	}
	
	@GET
	@Path("delete/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean deleteAmenity(@Context HttpServletRequest request, @PathParam("id") String id) {
		
		AmenityDAO amenities = getAmenities();
		ApartmentDAO apartments = getApartments();
		
		ArrayList<Apartment> allApartments = new ArrayList<Apartment>(apartments.findAll());
		
		for (Apartment apartment : allApartments) {
			for (String amenityId : apartment.getAmenityIds()) {
				if (amenityId.equals(id)) {
					return false;
				}
			}
		}
		amenities.find(id).setDeleted(true);
		saveAmenities(amenities);
		return true;
	}
	@GET
	@Path("getAmenity/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Amenity getAmenity(@Context HttpServletRequest request, @PathParam("id") String id) {
		
		AmenityDAO amenities = getAmenities();
		
		return amenities.find(id);
	
	}
	
	@PUT
	@Path("changeAmenity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean changeAmenity(@Context HttpServletRequest request, Amenity amenity) {
		
		AmenityDAO amenities = getAmenities();
		
		Amenity amenityToChange = amenities.find(amenity.getId());
		amenityToChange.setCaption(amenity.getCaption());
		amenityToChange.setDescription(amenity.getDescription());
		amenityToChange.setType(amenity.getType());
		
		saveAmenities(amenities);
		return true;
	}
	
	@POST
	@Path("createAmenity")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean createAmenity(@Context HttpServletRequest request, Amenity amenity) {
		
		AmenityDAO amenities = getAmenities();
		
		Amenity newAmenity = new Amenity();
		newAmenity.setId(String.valueOf(amenities.findAll().size() + 1));
		newAmenity.setCaption(amenity.getCaption());
		newAmenity.setDescription(amenity.getDescription());
		newAmenity.setType(amenity.getType());
		newAmenity.setDeleted(false);
		
		amenities.getAmenities().put(newAmenity.getId(), newAmenity);
		
		saveAmenities(amenities);
		return true;
	}
	
	public AmenityDAO getAmenities() {
		AmenityDAO amenities = (AmenityDAO) context.getAttribute("amenities");
		return amenities;
	}
	
	public void saveAmenities(AmenityDAO amenities) {
		amenities.saveAmenities(context.getRealPath(""));
	}
	
	public ApartmentDAO getApartments() {
		ApartmentDAO apartments = (ApartmentDAO) context.getAttribute("apartments");
		return apartments;
	}
}
