package model;

import java.io.Serializable;
import java.util.UUID;

@SuppressWarnings("serial")
public class Comment implements Serializable{
	
	private String id;
	private String caption;
	private String content;
	private String userId;
	private String apartmentId;
	private String reservationId;
	private Double rating;
	private boolean showed;
	private boolean deleted;
	
	public Comment() {
		super();
	}

	public Comment(String caption, String content, String user, String apartmentId, String reservationId, Double rating, boolean showed,
			boolean deleted) {
		super();
		UUID uuid = UUID.randomUUID();
		this.id = uuid.toString();
		this.caption = caption;
		this.content = content;
		this.userId = user;
		this.apartmentId = apartmentId;
		this.reservationId = reservationId;
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

	public String getUserID() {
		return userId;
	}

	public void setUserID(String user) {
		this.userId = user;
	}
	
	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public String getApartmentId() {
		return apartmentId;
	}

	public void setApartmentId(String apartmentId) {
		this.apartmentId = apartmentId;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
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

	@Override
	public String toString() {
		return "Comment [id=" + id + ", caption=" + caption + ", content=" + content + ", userId=" + userId
				+ ", apartmentId=" + apartmentId + ", reservationId=" + reservationId + ", rating=" + rating
				+ ", showed=" + showed + ", deleted=" + deleted + "]";
	}
	
	
}
