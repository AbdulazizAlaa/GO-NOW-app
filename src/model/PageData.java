package model;

public class PageData {

	private String page_id, user_id, name, product, description, location,
			founded, rides_ids, follwers;

	public PageData(String page_id, String user_id, String name,
			String product, String description, String location,
			String founded, String rides_ids, String follwers) {
		super();
		this.page_id = page_id;
		this.user_id = user_id;
		this.name = name;
		this.product = product;
		this.description = description;
		this.location = location;
		this.founded = founded;
		this.rides_ids = rides_ids;
		this.follwers = follwers;
	}

	public String getFollwers() {
		return follwers;
	}

	public void setFollwers(String follwers) {
		this.follwers = follwers;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFounded() {
		return founded;
	}

	public void setFounded(String founded) {
		this.founded = founded;
	}

	public String getRides_ids() {
		return rides_ids;
	}

	public void setRides_ids(String rides_ids) {
		this.rides_ids = rides_ids;
	}

}
