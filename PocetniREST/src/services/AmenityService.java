package services;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import dao.AmenityDAO;
import dao.UserDAO;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

@Path("Amenity")
public class AmenityService {

	@Context
	ServletContext context;
	
	public AmenityService() {
		
	}
	
	@PostConstruct
	public void init() {	
			if (context.getAttribute("amenityDAO") == null) {
				context.setAttribute("amenityDAO", new AmenityDAO(context.getRealPath("")));
			}
	}
}
