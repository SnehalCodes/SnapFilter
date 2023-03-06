package com.example.newproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    CardView SketchCard,blackandwhitecard,cartooncard,rainbowcard,reflectioncard;
    Toolbar toolbar;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeID();
        setSupportActionBar(toolbar);
        //toggle button
        actionBarDrawerToggle = new ActionBarDrawerToggle(this , drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //when an item is selected from menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home:
//                        Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                        setTitleColor(R.color.black);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });
    }

    private void initializeID() {
        SharedPreferences sharedPreferences = getSharedPreferences("FilterApp",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        navigationView = findViewById(R.id.navview);
        drawerLayout = findViewById(R.id.drawer);
        //adding customised toolbar
        toolbar = findViewById(R.id.toolbar);
        SketchCard = findViewById(R.id.sketch_card);
        blackandwhitecard = findViewById(R.id.bandw_card);
        cartooncard = findViewById(R.id.cartoon_card);
        rainbowcard = findViewById(R.id.rainbow_card);
        reflectioncard = findViewById(R.id.reflection_card);
        SketchCard.setOnClickListener(this);
        blackandwhitecard.setOnClickListener(this);
        cartooncard.setOnClickListener(this);
        rainbowcard.setOnClickListener(this);
        reflectioncard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.sketch_card:
                editor.putString("filterType", "Sketch");
                editor.commit();
                Intent intent = new Intent(MainActivity.this, ImgchooseActivity.class);
                startActivity(intent);
                break;
            case R.id.bandw_card:
                editor.putString("filterType", "BlackWhite");
                editor.commit();
                Intent intent1 = new Intent(MainActivity.this, ImgchooseActivity.class);
                startActivity(intent1);
                break;
            case R.id.cartoon_card:
                editor.putString("filterType", "Cartoon");
                editor.commit();
                Intent intent2 = new Intent(MainActivity.this,ImgchooseActivity.class);
                startActivity(intent2);
                break;
            case R.id.rainbow_card:
                editor.putString("filterType", "Rainbow");
                editor.commit();
                Intent intent3 = new Intent(MainActivity.this,ImgchooseActivity.class);
                startActivity(intent3);
                break;
            case R.id.reflection_card:
                editor.putString("filterType", "Reflection");
                editor.commit();
                Intent intent4 = new Intent(MainActivity.this,ImgchooseActivity.class);
                startActivity(intent4);
                break;
        }
    }
}