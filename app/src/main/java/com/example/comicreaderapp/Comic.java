package com.example.comicreaderapp;

public class Comic {

    private String title;
    private int progress;

    public Comic(String title, int progress) {
        this.title = title;
        this.progress = progress;
    }

    public String getTitle() {
        return title;
    }

    public int getProgress() {
        return progress;
    }
}