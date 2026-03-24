package com.example.comicreaderapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReaderActivity extends AppCompatActivity {

    Uri comicUri;

    LinearLayout menuLayout;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        // ⭐ Get URI
        comicUri = Uri.parse(getIntent().getStringExtra("uri"));

        if (comicUri == null) {
            finish();
            return;
        }

        // ⭐ MENU OVERLAY
        menuLayout = findViewById(R.id.layoutMenu);

        FrameLayout root = findViewById(R.id.readerRoot);

        root.setOnClickListener(v -> {
            if (menuLayout.getVisibility() == View.GONE)
                menuLayout.setVisibility(View.VISIBLE);
            else
                menuLayout.setVisibility(View.GONE);
        });

        // ⭐ Recycler
        recycler = findViewById(R.id.recyclerPages);

        // ⭐ DEFAULT MODE
        recycler.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL, false)
        );

        // ⭐ LOAD REAL CBZ
        loadCBZ();

        // ⭐ Mode Buttons
        TextView btnVertical = findViewById(R.id.btnVertical);
        TextView btnHorizontal = findViewById(R.id.btnHorizontal);

        btnVertical.setOnClickListener(v -> {
            recycler.setLayoutManager(
                    new LinearLayoutManager(this,
                            LinearLayoutManager.VERTICAL, false)
            );
            menuLayout.setVisibility(View.GONE);
        });

        btnHorizontal.setOnClickListener(v -> {
            recycler.setLayoutManager(
                    new LinearLayoutManager(this,
                            LinearLayoutManager.HORIZONTAL, false)
            );
            menuLayout.setVisibility(View.GONE);
        });

        showReadingModeDialog();
    }

    private void loadCBZ() {

        new Thread(() -> {

            try {

                List<ComicPage> pages = new ArrayList<>();

                InputStream is = getContentResolver().openInputStream(comicUri);

                ZipInputStream zis = new ZipInputStream(is);

                ZipEntry entry;

                while ((entry = zis.getNextEntry()) != null) {

                    String name = entry.getName().toLowerCase();

                    if (name.endsWith(".jpg") ||
                            name.endsWith(".jpeg") ||
                            name.endsWith(".png") ||
                            name.endsWith(".webp")) {

                        Bitmap bmp = BitmapFactory.decodeStream(zis);

                        if (bmp != null) {
                            pages.add(new ComicPage(bmp));
                        }
                    }

                    zis.closeEntry();
                }

                runOnUiThread(() -> {
                    recycler.setAdapter(new PageAdapter(pages));
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    private void showReadingModeDialog() {

        String[] modes = {"Vertical Scroll", "Horizontal Swipe"};

        new AlertDialog.Builder(this)
                .setTitle("Choose Reading Mode")
                .setItems(modes, (dialog, which) -> {

                    if (which == 0) {
                        recycler.setLayoutManager(
                                new LinearLayoutManager(this,
                                        LinearLayoutManager.VERTICAL, false)
                        );
                    } else {
                        recycler.setLayoutManager(
                                new LinearLayoutManager(this,
                                        LinearLayoutManager.HORIZONTAL, false)
                        );
                    }

                })
                .setCancelable(false)
                .show();
    }
}