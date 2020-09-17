package dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CommentForOneApartmentDTO implements Serializable{

	private String id;
	private String caption;
	private String content;
	private String guestUserName;
	private double rating;
	private boolean showed;
	
	public CommentForOneApartmentDTO() {
		
	}

	public CommentForOneApartmentDTO(String id, String caption, String content, String guestUserName, double rating,
			boolean showed) {
		super();
		this.id = id;
		this.caption = caption;
		this.content = content;
		this.guestUserName = guestUserName;
		this.rating = rating;
		this.showed = showed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getGuestUserName() {
		return guestUserName;
	}

	public void setGuestUserName(String guestUserName) {
		this.guestUserName = guestUserName;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public boolean isShowed() {
		return showed;
	}

	public void setShowed(boolean showed) {
		this.showed = showed;
	}
	
	
}
