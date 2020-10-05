package com.example.journal_app.modals;

import com.google.firebase.Timestamp;

public class Journal {
    private String title;
    private String thought;
    private String imageUrl;
    private String userId;
    private Timestamp createdAt;
    private String username;

    public Journal() { }

    public Journal(String title, String thought, String imageUrl, String userId, Timestamp createdAt, String username) {
        this.title = title;
        this.thought = thought;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.createdAt = createdAt;
        this.username = username;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public String getThought() {
        return thought;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getUsername() {
        return username;
    }
}
