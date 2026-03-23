package com.example.comicreaderapp;

import android.net.Uri;

public class LibraryComic {

    String title;
    Uri uri;

    public LibraryComic(String title, Uri uri) {
        this.title = title;
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public Uri getUri() {
        return uri;
    }
}