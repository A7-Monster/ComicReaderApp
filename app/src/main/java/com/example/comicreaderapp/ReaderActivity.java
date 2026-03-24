package com.example.comicreaderapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReaderActivity extends AppCompatActivity {

    Uri comicUri;

    LinearLayout menuLayout;
    RecyclerView recycler;

    List<Bitmap> pages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        // ⭐ RECEIVE URI
        comicUri = getIntent().getParcelableExtra("uri");

        if (comicUri == null) {
            finish();
            return;
        }

        // ⭐ MENU
        menuLayout = findViewById(R.id.layoutMenu);
        FrameLayout root = findViewById(R.id.readerRoot);

        root.setOnClickListener(v -> {
            if (menuLayout.getVisibility() == View.GONE)
                menuLayout.setVisibility(View.VISIBLE);
            else
                menuLayout.setVisibility(View.GONE);
        });

        recycler = findViewById(R.id.recyclerPages);

        // ⭐ DEFAULT MODE
        recycler.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.VERTICAL,
                        false)
        );

        // ⭐ LOAD REAL IMAGES
        loadCBZImages();

        // ⭐ SET ADAPTER
        recycler.setAdapter(new PageAdapter());

        // ⭐ MODE BUTTONS
        TextView btnVertical = findViewById(R.id.btnVertical);
        TextView btnHorizontal = findViewById(R.id.btnHorizontal);

        btnVertical.setOnClickListener(v -> {
            recycler.setLayoutManager(
                    new LinearLayoutManager(this,
                            LinearLayoutManager.VERTICAL,
                            false)
            );
            menuLayout.setVisibility(View.GONE);
        });

        btnHorizontal.setOnClickListener(v -> {
            recycler.setLayoutManager(
                    new LinearLayoutManager(this,
                            LinearLayoutManager.HORIZONTAL,
                            false)
            );
            menuLayout.setVisibility(View.GONE);
        });

        showReadingModeDialog();
    }

    private void loadCBZImages() {

        try {

            InputStream is = getContentResolver().openInputStream(comicUri);

            ZipInputStream zis = new ZipInputStream(is);

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {

                String name = entry.getName().toLowerCase();

                if (name.endsWith(".jpg") ||
                        name.endsWith(".png") ||
                        name.endsWith(".webp")) {

                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                    byte[] data = new byte[4096];
                    int n;

                    while ((n = zis.read(data)) != -1) {
                        buffer.write(data, 0, n);
                    }

                    byte[] imageBytes = buffer.toByteArray();

                    Bitmap bmp = BitmapFactory.decodeByteArray(
                            imageBytes,
                            0,
                            imageBytes.length
                    );

                    if (bmp != null)
                        pages.add(bmp);
                }

                zis.closeEntry();
            }

            zis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class PageAdapter extends RecyclerView.Adapter<PageAdapter.Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

            ImageView img = new ImageView(parent.getContext());

            img.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            img.setAdjustViewBounds(true);

            return new Holder(img);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.img.setImageBitmap(pages.get(position));
        }

        @Override
        public int getItemCount() {
            return pages.size();
        }

        class Holder extends RecyclerView.ViewHolder {

            ImageView img;

            Holder(View v) {
                super(v);
                img = (ImageView) v;
            }
        }
    }

    private void showReadingModeDialog() {

        String[] modes = {"Vertical Scroll", "Horizontal Swipe"};

        new AlertDialog.Builder(this)
                .setTitle("Choose Reading Mode")
                .setItems(modes, (dialog, which) -> {

                    if (which == 0) {

                        recycler.setLayoutManager(
                                new LinearLayoutManager(this,
                                        LinearLayoutManager.VERTICAL,
                                        false)
                        );

                    } else {

                        recycler.setLayoutManager(
                                new LinearLayoutManager(this,
                                        LinearLayoutManager.HORIZONTAL,
                                        false)
                        );

                    }

                })
                .setCancelable(false)
                .show();
    }
}