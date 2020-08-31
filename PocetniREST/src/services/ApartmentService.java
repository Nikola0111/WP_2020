package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.ApartmentDAO;
import dao.UserDAO;
import model.Apartment;
import model.User;

import java.nio.file.Paths;
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
		if (context.getAttribute("apartments") == null) {
			context.setAttribute("apartments", new ApartmentDAO(context.getRealPath("")));
		}
	}
	
	@POST
	@Path("registerApartment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void registerApartment(@Context HttpServletRequest request, Apartment apartment) {
		ApartmentDAO apartments = getApartments();
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		apartment.setId((apartments.getApartments().size() + 1) + "");
		System.out.println(loggedUser);
		
		apartment.setHostId(loggedUser == null ? "1" : loggedUser.getId());
		
		apartments.getApartments().put(apartment.getId(), apartment);
		saveApartments(apartments);
	}
	
	public ApartmentDAO getApartments() {
        ApartmentDAO apartments = (ApartmentDAO) context.getAttribute("apartments");
        return apartments;
    }
	
	public void saveApartments(ApartmentDAO apartments) {
		apartments.saveApartments(context.getRealPath(""));
    }
}
