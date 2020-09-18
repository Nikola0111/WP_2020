package dto;

public class SearchReservationsDTO {
	private String guestUsername;
	private String hostId;
	public SearchReservationsDTO() {
		
	}
	public String getGuestUsername() {
		return guestUsername;
	}
	public void setGuestUsername(String guestUsername) {
		this.guestUsername = guestUsername;
	}
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	
}
