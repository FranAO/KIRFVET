package com.example.kirfvet.components;

public class MenuItem {
    private String title;
    private int imageResourceId;

    public MenuItem(String title, int imageResourceId) {
        this.title = title;
        this.imageResourceId = imageResourceId;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
} 