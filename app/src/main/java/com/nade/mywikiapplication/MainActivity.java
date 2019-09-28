package com.nade.mywikiapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nade.mywikiapplication.api.JsonPlaceHolderApi;
import com.nade.mywikiapplication.models.Random;
import com.nade.mywikiapplication.models.WikiArticle;
import com.nade.mywikiapplication.utils.SharedPref;

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
    private ProgressBar progressBar;
    private TextView errorTextView;
    SharedPref sharedPref;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if (sharedPref.loadNightModeState()==true) {
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

    //initialising views
    private void initViews() {
        sqLiteDatabase = this.openOrCreateDatabase("DatabaseName", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + "offlineWiki" + " (Field1 TEXT);");

        changeThemeButton = (Button) findViewById(R.id.theme_button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        errorTextView = (TextView) findViewById(R.id.errorTextView);
        progressBar.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        loadJSON();
    }

    //loading json from url using retrofit
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

                saveOffline();
                progressBar.setVisibility(View.GONE);
                adapter = new DataAdapter(readRandomData);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<WikiArticle> call, Throwable t) {
                Log.d("Error",t.getMessage());
                progressBar.setVisibility(View.GONE);
                errorTextView.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Error: "+t.getMessage(), Toast.LENGTH_LONG).show();

                //fetch offline data and display it.
                fetchOffline();
            }
        });
    }

    //app restart for theming
    private void restartApp() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    //button on click function for theming
    private void themeChangeFunc() {
        if(sharedPref.loadNightModeState()==true) {
            changeThemeButton.setBackgroundResource(R.drawable.ic_sun);
        }
        changeThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPref.loadNightModeState()==true) {
                    sharedPref.setNightModeState(false);
                    changeThemeButton.setBackgroundResource(R.drawable.ic_moon);
                    restartApp();
                }
                else {
                    sharedPref.setNightModeState(true);
                    changeThemeButton.setBackgroundResource(R.drawable.ic_sun);
                    restartApp();
                }
            }
        });

        //long click listener for refresh
        changeThemeButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                restartApp();
                return false;
            }
        });
    }

    //this is to save data offline
    private void saveOffline() {
        StringBuilder str = null;
        str = new StringBuilder();
        for (Random random : readRandomData) {
            str.append(random.getTitle()+"|");
        }
        String result = str.toString();
        result=result.replace("'","*");
        sqLiteDatabase.execSQL("INSERT INTO " + "offlineWiki" + " (Field1) " + " VALUES ('" +result+ "');");
        sqLiteDatabase.close();
        //Toast.makeText(getApplicationContext(),"String result: "+result, Toast.LENGTH_LONG).show();
    }

    private void fetchOffline() {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + "offlineWiki" + " (Field1 TEXT);");
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM " + "offlineWiki" , null);

        int Column1 = c.getColumnIndex("Field1");
        c.moveToFirst();
        String result = new String();
        if (c!=null) {
            do {
                result = c.getString(Column1);
            }while(c.moveToNext());
        }
        result=result.replace("*","'");
        //Toast.makeText(getApplicationContext(),"String result: "+result, Toast.LENGTH_LONG).show();
        result=result.replace("|","\n");

        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText("Cannot connect to network. The last fetched data which is stored offline is: \n\n"+result);

    }
}