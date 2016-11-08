package com.kacyber.message;

public class VideoMessageBody extends FileMessageBody { 
	private static final long serialVersionUID = 2468207574867047746L;
	String title;
	String thumbUrl;
	String description;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
