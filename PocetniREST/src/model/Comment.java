package model;

import java.util.UUID;

public class Comment {
	
	private String id;
	private String caption;
	private String content;
	private User user;
	private String apartmentId;
	private int rating;
	private boolean showed;
	private boolean deleted;
	
	public Comment() {
		super();
	}

	public Comment(String caption, String content, User user, String apartmentId, int rating, boolean showed,
			boolean deleted) {
		super();
		UUID uuid = UUID.randomUUID();
		this.id = uuid.toString();
		this.caption = caption;
		this.content = content;
		this.user = user;
		this.apartmentId = apartmentId;
		this.rating = rating;
		this.showed = showed;
		this.deleted = deleted;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(String apartmentId) {
		this.apartmentId = apartmentId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public boolean isShowed() {
		return showed;
	}

	public void setShowed(boolean showed) {
		this.showed = showed;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	
	
	
	
}
