package dto;

public class ApartmentFilterDTO {

	private String rooms;
	private String guests;
	private String type;
	private int priceFrom;
	private int priceTo;
	public ApartmentFilterDTO() {
		super();
	}
	public String getRooms() {
		return rooms;
	}
	public void setRooms(String rooms) {
		this.rooms = rooms;
	}
	public String getGuests() {
		return guests;
	}
	public void setGuests(String guests) {
		this.guests = guests;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPriceFrom() {
		return priceFrom;
	}
	public void setPriceFrom(int priceFrom) {
		this.priceFrom = priceFrom;
	}
	public int getPriceTo() {
		return priceTo;
	}
	public void setPriceTo(int priceTo) {
		this.priceTo = priceTo;
	}
	@Override
	public String toString() {
		return "ApartmentFilterDTO [rooms=" + rooms + ", guests=" + guests + ", type=" + type + ", priceFrom="
				+ priceFrom + ", priceTo=" + priceTo + "]";
	}
	
	
}
