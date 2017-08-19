package com.example.tomsu.onews.control;

import java.io.Serializable;
import java.sql.Date;

public class Passage implements Serializable{


	private String title;
	private String content;
	private Date historyDate;
	private String picture;
	private int authorId;
	private int passageId;
	private int likeTimes;

	public int getLikeTimes() {
		return likeTimes;
	}

	public void setLikeTimes(int likeTimes) {
		this.likeTimes = likeTimes;
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(Date historyDate) {
		this.historyDate = historyDate;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public int getPassageId() {
		return passageId;
	}

	public void setPassageId(int passageId) {
		this.passageId = passageId;
	}





}
