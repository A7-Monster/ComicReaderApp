package com.example.comicreaderapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {

    // ⭐ LIST + ADAPTER
    List<LibraryComic> libraryList = new ArrayList<>();
    LibraryAdapter adapter;

    // ⭐ NEW MODERN PICKER LAUNCHER
    private ActivityResultLauncher<Intent> filePickerLauncher;

    public LibraryFragment() {
        // Required empty constructor
    }

    // ⭐ REGISTER PICKER HERE
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

                            libraryList.add(new LibraryComic(name, uri));

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

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 3));

        adapter = new LibraryAdapter(libraryList);

        recycler.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabImport);

        fab.setOnClickListener(v -> pickComicFile());

        return view;
    }

    // ⭐ UPDATED PICKER METHOD
    private void pickComicFile() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");

        String[] mimeTypes = {
                "application/pdf",
                "application/zip",
                "application/x-cbz",
                "image/*"
        };

        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        filePickerLauncher.launch(intent);
    }
}