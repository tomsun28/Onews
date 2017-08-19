package com.example.tomsu.onews.control;

import java.io.Serializable;
import java.util.Date;


public class Comments implements Serializable {
	
	private int passageId;
	private int commentId;
	private Date commentDate;
	private int reviewer;
	private String content;
	private String userName;
	private String userPhoto;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}



	public int getPassageId() {
		return passageId;
	}
	public void setPassageId(int passageId) {
		this.passageId = passageId;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public int getReviewer() {
		return reviewer;
	}
	public void setReviewer(int reviewer) {
		this.reviewer = reviewer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCommentDate() {
		return commentDate;
	}
	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
		
	}

	

}
