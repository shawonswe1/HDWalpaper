package com.techdynobd.hdwalpaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.techdynobd.hdwalpaper.FullScreenWallpaper;
import com.techdynobd.hdwalpaper.Model.WallpaperModel;
import com.techdynobd.hdwalpaper.R;

import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperAdapter.MyViewHolder> {

    Context context;
    List<WallpaperModel> wallpaperModels;

    public WallpaperAdapter(Context context, List<WallpaperModel> wallpaperModels) {
        this.context = context;
        this.wallpaperModels = wallpaperModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_image_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WallpaperModel currentList = wallpaperModels.get(position);

        Glide.with(context).load(currentList.getMediumUrl()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, FullScreenWallpaper.class)
                .putExtra("originalUrl",currentList.getOriginalUrl()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return wallpaperModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
