package dto;

public class FilterReservationsDTO {
	private String status;
	private String hostId;
	
	public FilterReservationsDTO() {
		
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	
	
}
