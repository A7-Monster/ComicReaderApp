package com.example.comicreaderapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private List<Comic> comics;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        comics = new ArrayList<>();

        comics.add(new Comic("Naruto", 30));
        comics.add(new Comic("One Piece", 70));
        comics.add(new Comic("Bleach", 50));
        comics.add(new Comic("Solo Leveling", 90));
        comics.add(new Comic("Jujutsu Kaisen", 10));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerContinueReading);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        recyclerView.setAdapter(
                new ComicAdapter(comics, comic -> {

                    Intent intent = new Intent(getContext(), ReaderActivity.class);
                    intent.putExtra("title", comic.getTitle());
                    startActivity(intent);

                })
        );

        return view;
    }
}