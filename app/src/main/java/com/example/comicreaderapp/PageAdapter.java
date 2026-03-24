package com.example.comicreaderapp;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.PageVH> {

    List<ComicPage> pages;

    public PageAdapter(List<ComicPage> pages) {
        this.pages = pages;
    }

    @NonNull
    @Override
    public PageVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ImageView img = new ImageView(parent.getContext());
        img.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        img.setAdjustViewBounds(true);

        return new PageVH(img);
    }

    @Override
    public void onBindViewHolder(@NonNull PageVH holder, int position) {
        holder.img.setImageBitmap(pages.get(position).getBitmap());
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    static class PageVH extends RecyclerView.ViewHolder {

        ImageView img;

        public PageVH(@NonNull ImageView itemView) {
            super(itemView);
            img = itemView;
        }
    }
}