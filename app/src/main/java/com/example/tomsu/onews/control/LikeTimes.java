package com.example.tomsu.onews.control;

import java.io.Serializable;

public class LikeTimes implements Serializable {
	
	private boolean statusCode;
	private int userId;
	private int passageId;
	private int likeId;
	
	
	public boolean isStatusCode() {
		return statusCode;
	}
	public void setStatusCode(boolean statusCode) {
		this.statusCode = statusCode;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getPassageId() {
		return passageId;
	}
	public void setPassageId(int passageId) {
		this.passageId = passageId;
	}
	public int getLikeId() {
		return likeId;
	}
	public void setLikeId(int likeId) {
		this.likeId = likeId;
	}
	

}
