package services;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import dao.CommentDAO;
import dao.UserDAO;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

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
	}
}