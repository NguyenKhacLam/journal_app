package com.example.journal_app.utils;

import android.app.Application;

public class JournalApi extends Application {
    private String userId;
    private String username;
    private static JournalApi instance;

    public static JournalApi getInstance(){
        if (instance == null)
            instance = new JournalApi();
        return instance;
    }

    public JournalApi(){};

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
