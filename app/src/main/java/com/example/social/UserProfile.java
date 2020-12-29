package com.example.social;

import java.util.List;

public class UserProfile {
    String uId;
    String photoUrl;
    String DisplayName;
    String message;
    List<String> posts;
    String emailId;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    String timeStamp;

    public UserProfile(String uId, String photoUrl, String displayName, String message, List<String> posts, String emailId, String timeStamp) {
        if(uId!=null)
             this.uId = uId;
        if(photoUrl!=null)
            this.photoUrl = photoUrl;
        if(displayName!=null)
            DisplayName = displayName;
        if(message!=null)
            this.message = message;
        if(posts!=null)
            this.posts = posts;
        if(emailId!=null)
            this.emailId = emailId;
        if(timeStamp!=null)
        this.timeStamp = timeStamp;
    }
}
