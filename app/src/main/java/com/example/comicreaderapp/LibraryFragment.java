package com.example.comicreaderapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LibraryFragment extends Fragment {

    List<LibraryComic> libraryList = new ArrayList<>();
    LibraryAdapter adapter;

    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == getActivity().RESULT_OK) {

                        Intent data = result.getData();

                        if (data != null) {

                            Uri uri = data.getData();

                            String name = getFileName(uri);

                            // ⭐ EXTRACT COVER
                            Bitmap cover = extractCover(uri);

                            libraryList.add(new LibraryComic(name, uri, cover));

                            adapter.notifyDataSetChanged();

                            Toast.makeText(getContext(), "Comic Imported!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private String getFileName(Uri uri) {

        String result = "Comic";

        Cursor cursor = getActivity().getContentResolver()
                .query(uri, null, null, null, null);

        if (cursor != null) {

            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

            if (cursor.moveToFirst()) {
                result = cursor.getString(nameIndex);
            }

            cursor.close();
        }

        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_library, container, false);

        RecyclerView recycler = view.findViewById(R.id.recyclerLibrary);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new LibraryAdapter(libraryList, comic -> {

            Intent intent = new Intent(getContext(), ReaderActivity.class);
            intent.putExtra("uri", comic.getUri());
            startActivity(intent);
        });

        recycler.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabImport);

        fab.setOnClickListener(v -> pickComicFile());

        return view;
    }

    private void pickComicFile() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");

        String[] mimeTypes = {
                "application/zip",
                "application/x-cbz"
        };

        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        filePickerLauncher.launch(intent);
    }

    // ⭐ COVER EXTRACTION
    private Bitmap extractCover(Uri uri) {

        try {

            InputStream is = getActivity().getContentResolver().openInputStream(uri);
            ZipInputStream zis = new ZipInputStream(is);

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {

                String name = entry.getName().toLowerCase();

                if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".webp")) {

                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                    byte[] data = new byte[4096];
                    int n;

                    while ((n = zis.read(data)) != -1) {
                        buffer.write(data, 0, n);
                    }

                    byte[] imageBytes = buffer.toByteArray();

                    zis.close();

                    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                }

                zis.closeEntry();
            }

            zis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}