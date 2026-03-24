package com.example.comicreaderapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

        // ⭐ Get URI
        comicUri = getIntent().getParcelableExtra("uri");

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

        // ⭐ TEMP Adapter (Dummy Pages)
        recycler.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                TextView tv = new TextView(parent.getContext());
                tv.setTextColor(0xffffffff);
                tv.setTextSize(32);
                tv.setPadding(80, 400, 80, 400);

                return new RecyclerView.ViewHolder(tv) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView) holder.itemView).setText("Page " + (position + 1));
            }

            @Override
            public int getItemCount() {
                return 20;
            }
        });

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