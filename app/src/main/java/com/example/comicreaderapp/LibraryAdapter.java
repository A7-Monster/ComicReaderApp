package com.example.comicreaderapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    List<LibraryComic> comics;
    OnComicClickListener listener;

    // ⭐ CLICK INTERFACE
    public interface OnComicClickListener {
        void onComicClick(LibraryComic comic);
    }

    public LibraryAdapter(List<LibraryComic> comics, OnComicClickListener listener) {
        this.comics = comics;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_library_comic, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LibraryComic comic = comics.get(position);

        holder.title.setText(comic.getTitle());

        holder.itemView.setOnClickListener(v -> {
            listener.onComicClick(comic);
        });
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvComicName);
        }
    }
}