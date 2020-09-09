package dto;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class SearchApartmentDTO implements Serializable{

	private Date startDate;
	private Date endDate;
	private String city;
	private String country;
	private Double price;
	private int numberOfRooms;
	private int numberOfPeople;
	
	public SearchApartmentDTO() {
		super();
	}
	
	public SearchApartmentDTO(Date startDate, Date endDate, String city, String country, Double price, int numberOfRooms,
			int numberOfPeople) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.city = city;
		this.country = country;
		this.price = price;
		this.numberOfRooms = numberOfRooms;
		this.numberOfPeople = numberOfPeople;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String street) {
		this.country = street;
	}


	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public int getNumberOfRooms() {
		return numberOfRooms;
	}
	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}
	public int getNumberOfPeople() {
		return numberOfPeople;
	}
	public void setNumberOfPeople(int numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
	}
	
	
	
}
