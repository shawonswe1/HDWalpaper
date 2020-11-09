package com.techdynobd.hdwalpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;

public class FullScreenWallpaper extends AppCompatActivity {

    String originalUrl = "";
    PhotoView photoView;
    Button setWallpaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_wallpaper);

        getSupportActionBar().hide();

        Intent intent = getIntent();
        originalUrl = intent.getStringExtra("originalUrl");

        photoView = findViewById(R.id.photoView);
        Glide.with(getApplicationContext()).load(originalUrl).into(photoView);

        setWallpaper = findViewById(R.id.setWallpaper);
        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullScreenWallpaper.this);
                Bitmap bitmap = ((BitmapDrawable)photoView.getDrawable()).getBitmap();

                try {
                    wallpaperManager.setBitmap(bitmap);
                    Toast.makeText(FullScreenWallpaper.this, "Wallpaper set successfully", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}