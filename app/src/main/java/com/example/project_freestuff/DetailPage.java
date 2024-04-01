package com.example.project_freestuff;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailPage extends AppCompatActivity {

    ImageView productImage;
    TextView productName;
    TextView productDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        //getting values from the intent
        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String imageUri = getIntent().getStringExtra("url");

        productName = findViewById(R.id.detailProductName);
        productDescription = findViewById(R.id.detailProductDescription);
        productImage = findViewById(R.id.imageProduct);

        productName.setText(name);
        productDescription.setText(description);

        Glide.with(DetailPage.this).load(imageUri).into(productImage);



    }
}