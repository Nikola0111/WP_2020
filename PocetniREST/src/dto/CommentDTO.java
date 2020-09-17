package dto;

public class CommentDTO {
	private String caption;
	private String content;
	private String userId;
	private String apartmentId;
	private String reservationId;
	private double rating;
	public CommentDTO() {
		
	}
	
	public CommentDTO(String caption, String content, String userId, String apartmentId, double rating) {
		super();
		this.caption = caption;
		this.content = content;
		this.userId = userId;
		this.apartmentId = apartmentId;
		this.rating = rating;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getApartmentId() {
		return apartmentId;
	}
	public void setApartmentId(String apartmentId) {
		this.apartmentId = apartmentId;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}
	
	
}
