package com.example.comicreaderapp;

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

public class ReaderActivity extends AppCompatActivity {

    Uri comicUri;

    LinearLayout menuLayout;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        comicUri = getIntent().getParcelableExtra("uri");

        // ⭐ MENU OVERLAY
        menuLayout = findViewById(R.id.layoutMenu);

        FrameLayout root = findViewById(R.id.readerRoot);

        root.setOnClickListener(v -> {
            if (menuLayout.getVisibility() == View.GONE)
                menuLayout.setVisibility(View.VISIBLE);
            else
                menuLayout.setVisibility(View.GONE);
        });

        // ⭐ RECYCLER
        recycler = findViewById(R.id.recyclerPages);

        // ⭐ MODE BUTTONS
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