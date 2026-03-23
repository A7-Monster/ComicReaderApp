package com.example.comicreaderapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder> {

    private List<Comic> comicList;
    private OnComicClickListener listener;

    public ComicAdapter(List<Comic> comicList, OnComicClickListener listener) {
        this.comicList = comicList;
        this.listener = listener;
    }
    public interface OnComicClickListener {
        void onComicClick(Comic comic);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comic_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comic comic = comicList.get(position);
        holder.itemView.setOnClickListener(v -> {
            listener.onComicClick(comic);
        });
        holder.title.setText(comic.getTitle());
        holder.progressBar.setProgress(comic.getProgress());
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvComicName);
            progressBar = itemView.findViewById(R.id.progressRead);
        }
    }
}