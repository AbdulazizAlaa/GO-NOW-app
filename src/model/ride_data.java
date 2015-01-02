package model;

import android.graphics.Bitmap;

public class ride_data {

	private String ride_id, page_id, user_id, pageTitle, day, month, time,
			city, country, description, ticketPrice, ticketCount, numGoing,
			numMaybe, numDecline, going_ids, maybe_ids, decline_ids, route_lat,
			route_lng;
	private Bitmap cover;

	public ride_data(String ride_id, String page_id, String user_id,
			String pageTitle, String day, String month, String time,
			String city, String country, String description,
			String ticketPrice, String ticketCount, String numGoing,
			String numMaybe, String numDecline, String going_ids,
			String maybe_ids, String decline_ids, String route_lat,
			String route_lng, Bitmap cover) {
		super();
		this.ride_id = ride_id;
		this.page_id = page_id;
		this.user_id = user_id;
		this.pageTitle = pageTitle;
		this.day = day;
		this.month = month;
		this.time = time;
		this.city = city;
		this.country = country;
		this.description = description;
		this.ticketPrice = ticketPrice;
		this.ticketCount = ticketCount;
		this.numGoing = numGoing;
		this.numMaybe = numMaybe;
		this.numDecline = numDecline;
		this.going_ids = going_ids;
		this.maybe_ids = maybe_ids;
		this.decline_ids = decline_ids;
		this.route_lat = route_lat;
		this.route_lng = route_lng;
		this.cover = cover;
	}

	public String getRoute_lat() {
		return route_lat;
	}

	public void setRoute_lat(String route_lat) {
		this.route_lat = route_lat;
	}

	public String getRoute_lng() {
		return route_lng;
	}

	public void setRoute_lng(String route_lng) {
		this.route_lng = route_lng;
	}

	public String getPage_id() {
		return page_id;
	}

	public void setPage_id(String page_id) {
		this.page_id = page_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getGoing_ids() {
		return going_ids;
	}

	public void setGoing_ids(String going_ids) {
		this.going_ids = going_ids;
	}

	public String getMaybe_ids() {
		return maybe_ids;
	}

	public void setMaybe_ids(String maybe_ids) {
		this.maybe_ids = maybe_ids;
	}

	public String getDecline_ids() {
		return decline_ids;
	}

	public void setDecline_ids(String decline_ids) {
		this.decline_ids = decline_ids;
	}

	public String getRide_id() {
		return ride_id;
	}

	public void setRide_id(String ride_id) {
		this.ride_id = ride_id;
	}

	public String getNumGoing() {
		return numGoing;
	}

	public void setNumGoing(String numGoing) {
		this.numGoing = numGoing;
	}

	public String getNumMaybe() {
		return numMaybe;
	}

	public void setNumMaybe(String numMaybe) {
		this.numMaybe = numMaybe;
	}

	public String getNumDecline() {
		return numDecline;
	}

	public void setNumDecline(String numDecline) {
		this.numDecline = numDecline;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(String ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public String getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(String ticketCount) {
		this.ticketCount = ticketCount;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
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

	public void setCountry(String country) {
		this.country = country;
	}

	public Bitmap getCover() {
		return cover;
	}

	public void setCover(Bitmap cover) {
		this.cover = cover;
	}

}
