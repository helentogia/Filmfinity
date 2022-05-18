package com.example.filmfinity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private FirebaseAuth firebaseAuth;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Button buttonAdd, buttonRemove, buttonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        firebaseAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.admin_activity_drawer_layout);
        navigationView = findViewById(R.id.admin_activity_nav_view);
        toolbar = findViewById(R.id.admin_activity_toolbar);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonRemove = findViewById(R.id.buttonRemove);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add = new Intent(AdminActivity.this, AddMovieActivity.class);
                startActivity(add);
                finish();
            }
        });

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent remove = new Intent(AdminActivity.this, RemoveMovieActivity.class);
                startActivity(remove);
                finish();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent update = new Intent(AdminActivity.this, UpdateMovieActivity.class);
                startActivity(update);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.openDrawer(GravityCompat.START);
        } else {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_add:
                Intent add = new Intent(AdminActivity.this, AddMovieActivity.class);
                startActivity(add);
                finish();
                break;
            case R.id.nav_remove:
                Intent remove = new Intent(AdminActivity.this, RemoveMovieActivity.class);
                startActivity(remove);
                finish();
                break;
            case R.id.nav_update:
                Intent update = new Intent(AdminActivity.this, UpdateMovieActivity.class);
                startActivity(update);
                finish();
                break;
            case R.id.nav_admin_info:
                break;
            case R.id.nav_admin_logout:
                firebaseAuth.signOut();
                Intent signout = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(signout);
                finish();
                break;
        }
        return false;
    }
}