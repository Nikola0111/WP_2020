package dto;

public class CommentEligibilityDataDTO {
	private String userId;
	private String apartmentId;
	
	public CommentEligibilityDataDTO() {
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


	@Override
	public String toString() {
		return "CommentEligibilityDataDTO [userId=" + userId + ", apartmentId=" + apartmentId + "]";
	}
	
	
}
