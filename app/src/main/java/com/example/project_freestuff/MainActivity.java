package com.example.project_freestuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

public class MainActivity extends AppCompatActivity {
    GridLayout gridProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridProducts = findViewById(R.id.gridLayoutProducts);

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