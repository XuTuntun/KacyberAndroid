/**
 * 
 */
package com.kacyber.message;

/**
 * @author luolishu
 *
 */
public class MusicMessageBody extends FileMessageBody {
	private static final long serialVersionUID = 2316875591263606502L;
	String title;
	String author;
	String description;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
