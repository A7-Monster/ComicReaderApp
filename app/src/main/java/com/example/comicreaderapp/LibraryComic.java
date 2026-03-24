package com.example.comicreaderapp;

import android.net.Uri;

public class LibraryComic {

    String name;
    Uri uri;

    public LibraryComic(String name, Uri uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        return uri;
    }
}