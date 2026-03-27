package com.example.comicreaderapp;

import android.graphics.Bitmap;
import android.net.Uri;

public class LibraryComic {

    String name;
    Uri uri;
    Bitmap cover; // ⭐ NEW

    // ⭐ UPDATED CONSTRUCTOR
    public LibraryComic(String name, Uri uri, Bitmap cover) {
        this.name = name;
        this.uri = uri;
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        return uri;
    }

    public Bitmap getCover() {
        return cover;
    }
}