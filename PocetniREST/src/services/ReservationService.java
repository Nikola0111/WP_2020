package services;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import dao.ReservationDAO;
import dao.UserDAO;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

public class ReservationService {

	@Context
	ServletContext context;
	
	public ReservationService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (context.getAttribute("reservations") == null) {
			context.setAttribute("reservations", new ReservationDAO(context.getRealPath("")));
		}
	}
}
