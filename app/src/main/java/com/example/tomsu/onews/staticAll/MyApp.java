package com.example.tomsu.onews.staticAll;

import android.app.Application;

/**
 * Created by tomsu on 2017/4/29.
 */

public class MyApp extends Application {
    private int passageId;
    private int userId;
    private String userPhoto;
    private String userEmail;
    private String userName;
    private boolean there;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public boolean isThere() {
        return there;
    }

    public void setThere(boolean there) {
        this.there = there;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }



}
