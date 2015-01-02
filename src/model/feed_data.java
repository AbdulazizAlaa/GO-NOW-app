package model;

import android.graphics.Bitmap;

public class feed_data {

	private String text, pageName, day, feed_id, like_ids;
	private int numLikes, numComments;
	Bitmap coverImg, pageImg;

	public feed_data(String text, String pageName, String day, String feed_id,
			String like_ids, int numLikes, int numComments, Bitmap coverImg,
			Bitmap pageImg) {
		super();
		this.text = text;
		this.pageName = pageName;
		this.day = day;
		this.feed_id = feed_id;
		this.like_ids = like_ids;
		this.numLikes = numLikes;
		this.numComments = numComments;
		this.coverImg = coverImg;
		this.pageImg = pageImg;
	}

	public String getLike_ids() {
		return like_ids;
	}

	public void setLike_ids(String like_ids) {
		this.like_ids = like_ids;
	}

	public String getFeed_id() {
		return feed_id;
	}

	public void setFeed_id(String feed_id) {
		this.feed_id = feed_id;
	}

	public Bitmap getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(Bitmap coverImg) {
		this.coverImg = coverImg;
	}

	public Bitmap getPageImg() {
		return pageImg;
	}

	public void setPageImg(Bitmap pageImg) {
		this.pageImg = pageImg;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getNumLikes() {
		return numLikes;
	}

	public void setNumLikes(int numLikes) {
		this.numLikes = numLikes;
	}

	public int getNumComments() {
		return numComments;
	}

	public void setNumComments(int numComments) {
		this.numComments = numComments;
	}

}
