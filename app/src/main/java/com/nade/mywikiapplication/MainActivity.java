package com.nade.mywikiapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nade.mywikiapplication.api.JsonPlaceHolderApi;
import com.nade.mywikiapplication.models.Random;
import com.nade.mywikiapplication.models.WikiArticle;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataAdapter adapter;
    List<Random> readRandomData;
    private Button changeThemeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        themeChangeFunc();
    }

    private void initViews() {
        changeThemeButton = (Button) findViewById(R.id.theme_button);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadJSON();
    }

    private void loadJSON() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://en.wikipedia.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<WikiArticle> call = jsonPlaceHolderApi.getRandArticle();
        call.enqueue(new Callback<WikiArticle>() {
            @Override
            public void onResponse(Call<WikiArticle> call, Response<WikiArticle> response) {

                readRandomData= response.body().getQuery().getRandom();

                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Code: "+response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                adapter = new DataAdapter(readRandomData);
                recyclerView.setAdapter(adapter);

            }
            @Override
            public void onFailure(Call<WikiArticle> call, Throwable t) {
                Log.d("Error",t.getMessage());
                Toast.makeText(getApplicationContext(),"Error: "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void restartApp() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private void themeChangeFunc() {
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES) {
            changeThemeButton.setBackgroundResource(R.drawable.ic_sun);
        }
        changeThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    changeThemeButton.setBackgroundResource(R.drawable.ic_moon);
                    restartApp();
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    changeThemeButton.setBackgroundResource(R.drawable.ic_sun);
                    restartApp();
                }
            }
        });
    }

}