package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.AmenityDAO;
import dao.UserDAO;
import model.Amenity;

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
				context.setAttribute("amenities", new AmenityDAO(context.getRealPath("")));
			}
	}
	
	@GET
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Amenity> getAllAmenities(@Context HttpServletRequest request) {
		
		AmenityDAO amenities = getAmenities();
		ArrayList<Amenity> allAmenities = new ArrayList<Amenity>(amenities.findAll());
		return allAmenities;
	}
	
	
	public AmenityDAO getAmenities() {
		AmenityDAO amenities = (AmenityDAO) context.getAttribute("users");
		return amenities;
	}
	
	public void saveAmenities(AmenityDAO amenities) {
		amenities.saveAmenities(context.getRealPath(""));
	}
}
