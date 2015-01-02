package model;

public class profile_session_data {
	private String session_id, user_id, routeLat, routeLng, date;
	private int distance, speed, time;

	public profile_session_data(String session_id, String user_id,
			String routeLat, String routeLng, String date, int distance,
			int speed, int time) {
		super();
		this.session_id = session_id;
		this.user_id = user_id;
		this.routeLat = routeLat;
		this.routeLng = routeLng;
		this.date = date;
		this.distance = distance;
		this.speed = speed;
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getRouteLat() {
		return routeLat;
	}

	public void setRouteLat(String routeLat) {
		this.routeLat = routeLat;
	}

	public String getRouteLng() {
		return routeLng;
	}

	public void setRouteLng(String routeLng) {
		this.routeLng = routeLng;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
