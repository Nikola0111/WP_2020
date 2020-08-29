package services;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import dao.ApartmentDAO;
import dao.UserDAO;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

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
	
}
