package com.example.project_freestuff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    GridLayout gridProducts;
    ImageButton addBtn;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridProducts = findViewById(R.id.gridLayoutProducts);
        addBtn = findViewById(R.id.addItem);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    Toast.makeText(MainActivity.this, "home Selected", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.settings) {
                    Toast.makeText(MainActivity.this, "settings Selected", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.search) {
                    Toast.makeText(MainActivity.this, "search Selected", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.share) {
                    Toast.makeText(MainActivity.this, "share Selected", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.favorites) {
                    Toast.makeText(MainActivity.this, "favorites Selected", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.account) {
                    Toast.makeText(MainActivity.this, "account Selected", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.rateus) {
                    Toast.makeText(MainActivity.this, "rateus Selected", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.sms) {
                    Toast.makeText(MainActivity.this, "sms Selected", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.logout) {
                    Toast.makeText(MainActivity.this, "logout Selected", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewItemPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
