package dto;

import java.io.Serializable;
import java.util.List;

import enumeration.ApartmentType;

@SuppressWarnings("serial")
public class FilterApartmentDTO implements Serializable{
	
	private ApartmentType apartmentType;
	private List<String> amenityIds;
	private boolean activityStatus;
	
	public FilterApartmentDTO() {
		super();
	}
	public FilterApartmentDTO(ApartmentType apartmentType, List<String> amenityIds) {
		super();
		this.apartmentType = apartmentType;
		this.amenityIds = amenityIds;
	}
	public FilterApartmentDTO(ApartmentType apartmentType, List<String> amenityIds, boolean activityStatus) {
		super();
		this.apartmentType = apartmentType;
		this.amenityIds = amenityIds;
		this.activityStatus = activityStatus;
	}
	public ApartmentType getApartmentType() {
		return apartmentType;
	}
	public void setApartmentType(ApartmentType apartmentType) {
		this.apartmentType = apartmentType;
	}
	public List<String> getAmenityIds() {
		return amenityIds;
	}
	public void setAmenityIds(List<String> amenityIds) {
		this.amenityIds = amenityIds;
	}
	public boolean isActivityStatus() {
		return activityStatus;
	}
	public void setActivityStatus(boolean activityStatus) {
		this.activityStatus = activityStatus;
	}
}
