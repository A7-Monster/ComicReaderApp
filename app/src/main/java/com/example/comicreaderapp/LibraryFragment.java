package com.example.comicreaderapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {

    RecyclerView recycler;
    FloatingActionButton fab;

    List<LibraryComic> libraryList = new ArrayList<>();
    LibraryAdapter adapter;

    ActivityResultLauncher<String[]> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(),
                    uri -> {
                        if (uri != null) {

                            String name = getFileName(uri);

                            libraryList.add(new LibraryComic(name, uri));
                            adapter.notifyDataSetChanged();
                        }
                    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_library, container, false);

        recycler = view.findViewById(R.id.recyclerLibrary);
        fab = view.findViewById(R.id.fabImport);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 3));

        adapter = new LibraryAdapter(libraryList, comic -> {

            Intent intent = new Intent(getContext(), ReaderActivity.class);

            // ⭐ SEND URI OBJECT (NOT STRING)
            intent.putExtra("uri", comic.getUri());

            startActivity(intent);

        });

        recycler.setAdapter(adapter);

        fab.setOnClickListener(v -> pickComicFile());

        return view;
    }

    private void pickComicFile() {

        filePickerLauncher.launch(new String[]{
                "application/zip",
                "application/x-cbz",
                "application/pdf",
                "image/*"
        });
    }

    private String getFileName(Uri uri) {

        String result = "Comic";

        Cursor cursor = getContext().getContentResolver()
                .query(uri, null, null, null, null);

        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            cursor.moveToFirst();
            result = cursor.getString(nameIndex);
            cursor.close();
        }

        return result;
    }
}