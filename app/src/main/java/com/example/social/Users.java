package com.example.social;

import java.util.List;

public class Users {
    String DISPLAY_IMAGE_URL;
    String DisplayName;
    String EmailID;
    List<String> URL;
    String Uid;
    String timestamp;

    public Users() {
    }

    public String getDISPLAY_IMAGE_URL() {
        return DISPLAY_IMAGE_URL;
    }

    public void setDISPLAY_IMAGE_URL(String DISPLAY_IMAGE_URL) {
        this.DISPLAY_IMAGE_URL = DISPLAY_IMAGE_URL;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public List<String> getURL() {
        return URL;
    }

    public void setURL(List<String> URL) {
        this.URL = URL;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


 }
