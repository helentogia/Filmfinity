package com.example.filmfinity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton drama, comedy, action, crime, horror, thriller, adventure, animation,scifi;
    Button next;
    ImageView back;
    List<String> categoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        categoriesList = new ArrayList<String>();

        drama = (ImageButton) findViewById(R.id.dramaCategory);
        comedy = (ImageButton) findViewById(R.id.comedyCategory);
        action = (ImageButton) findViewById(R.id.actionCategory);
        crime = (ImageButton) findViewById(R.id.crimeCategory);
        horror = (ImageButton) findViewById(R.id.horrorCategory);
        thriller = (ImageButton) findViewById(R.id.thrillerCategory);
        adventure = (ImageButton) findViewById(R.id.adventureCategory);
        animation = (ImageButton) findViewById(R.id.animationCategory);
        scifi = (ImageButton) findViewById(R.id.scifiCategory);
        next = (Button) findViewById(R.id.categoriesNext);
        back = (ImageView) findViewById(R.id.categoriesBack);

        drama.setOnClickListener(this);
        comedy.setOnClickListener(this);
        action.setOnClickListener(this);
        crime.setOnClickListener(this);
        horror.setOnClickListener(this);
        thriller.setOnClickListener(this);
        adventure.setOnClickListener(this);
        animation.setOnClickListener(this);
        scifi.setOnClickListener(this);
        next.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.categoriesBack:
                Intent backIntent = new Intent(CategoriesActivity.this, RegistrationActivity.class);
                startActivity(backIntent);
                finish();
                break;
            case R.id.dramaCategory:
                Toast.makeText(CategoriesActivity.this, "Drama", Toast.LENGTH_SHORT).show();
                categoriesList.add("Drama");
                break;
            case R.id.comedyCategory:
                Toast.makeText(CategoriesActivity.this, "Comedy", Toast.LENGTH_SHORT).show();
                categoriesList.add("Comedy");
                break;
            case R.id.actionCategory:
                Toast.makeText(CategoriesActivity.this, "Action", Toast.LENGTH_SHORT).show();
                categoriesList.add("Action");
                break;
            case R.id.crimeCategory:
                Toast.makeText(CategoriesActivity.this, "Crime", Toast.LENGTH_SHORT).show();
                categoriesList.add("Crime");
                break;
            case R.id.horrorCategory:
                Toast.makeText(CategoriesActivity.this, "Horror", Toast.LENGTH_SHORT).show();
                categoriesList.add("Horror");
                break;
            case R.id.thrillerCategory:
                Toast.makeText(CategoriesActivity.this, "Thriller", Toast.LENGTH_SHORT).show();
                categoriesList.add("Thriller");
                break;
            case R.id.adventureCategory:
                Toast.makeText(CategoriesActivity.this, "Adventure", Toast.LENGTH_SHORT).show();
                categoriesList.add("Adventure");
                break;
            case R.id.animationCategory:
                Toast.makeText(CategoriesActivity.this, "Animation", Toast.LENGTH_SHORT).show();
                categoriesList.add("Animation");
                break;
            case R.id.scifiCategory:
                Toast.makeText(CategoriesActivity.this, "Sci-Fi", Toast.LENGTH_SHORT).show();
                categoriesList.add("Sci-Fi");
                break;
            case R.id.categoriesNext:
                String str = String.join(",", categoriesList);

                // Add favorite categories to user database

                Intent nextIntent = new Intent(CategoriesActivity.this, FavoriteMoviesActivity.class);
                startActivity(nextIntent);
                finish();
                break;
        }
    }
}