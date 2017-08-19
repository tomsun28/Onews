package com.example.tomsu.onews.control;

public class Result {
	private int statusCode;
	private String message;

	public Result(){}
	
	public Result(int statusCodde, String message){
		this.statusCode = statusCodde;
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
