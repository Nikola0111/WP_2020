package services;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import dao.UserDAO;

@Path("User")
public class UserService {

	@Context
	ServletContext context;
	
	public UserService() {
		
	}
	
	@PostConstruct
	public void init() {
		if (context.getAttribute("users") == null) {
			context.setAttribute("users", new UserDAO(context.getRealPath("")));
		}
	}
}
