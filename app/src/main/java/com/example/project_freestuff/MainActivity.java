package com.example.project_freestuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    GridLayout gridProducts;

    ImageButton addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridProducts = findViewById(R.id.gridLayoutProducts);
        addBtn = findViewById(R.id.addItem);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewItemPage.class);
                startActivity(intent);
                finish();
            }
        });

        for (int i=0; i<gridProducts.getChildCount(); i++){
            final View child = gridProducts.getChildAt(i);
            final int index = i;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, DetailPage.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }
}