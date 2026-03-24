package com.example.comicreaderapp;

import android.graphics.Bitmap;

public class ComicPage {

    Bitmap bitmap;

    public ComicPage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}