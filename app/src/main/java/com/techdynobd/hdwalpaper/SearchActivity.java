package com.techdynobd.hdwalpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techdynobd.hdwalpaper.Adapter.WallpaperAdapter;
import com.techdynobd.hdwalpaper.Model.WallpaperModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    WallpaperAdapter wallpaperAdapter;
    List<WallpaperModel> wallpaperModels = new ArrayList<>();

    int pageNumber = 1;

    Boolean isScrolling = false;
    int currentItems,totalItems,scrollOutItems;

    ProgressBar progressBar;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        query = getIntent().getStringExtra("query");
        getSupportActionBar().setTitle(query);

        //Add back button------------------
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fetchWallpaper(query);

        wallpaperAdapter = new WallpaperAdapter(this,wallpaperModels);
        recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(wallpaperAdapter);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems+scrollOutItems == totalItems))
                {
                    isScrolling = false;
                    fetchWallpaper(query);
                }
            }
        });
    }

    private void fetchWallpaper(String query)
    {
        StringRequest request = new StringRequest(Request.Method.GET, "https://api.pexels.com/v1/search/?page="+pageNumber+"&per_page=80&query="+query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            progressBar.setVisibility(View.GONE);
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("photos");

                            int length = jsonArray.length();
                            for (int i=0;i<length;i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                int id = object.getInt("id");

                                JSONObject objectImages = object.getJSONObject("src");

                                String originalUrl = objectImages.getString("original");
                                String mediumUrl = objectImages.getString("medium");

                                WallpaperModel wallpaperModel = new WallpaperModel(id,originalUrl,mediumUrl);
                                wallpaperModels.add(wallpaperModel);
                            }

                            wallpaperAdapter.notifyDataSetChanged();
                            pageNumber++;

                        }catch (JSONException e)
                        {
                            Log.e("Error",e.getLocalizedMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Authorization","563492ad6f91700001000001f26b27a403814e31bedb293cf336c662");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    //Enable Back Button---------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home)
        {
            startActivity(new Intent(SearchActivity.this,MainActivity.class));
//            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}