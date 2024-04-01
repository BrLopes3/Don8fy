package com.example.project_freestuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailPage extends AppCompatActivity {

    ImageView productImage;
    TextView productName;
    TextView productDescription;

    Button editItem, removeItem;

    //references from the Firebase
    private DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("items");
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference("images");

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
        editItem = findViewById(R.id.btnEdit);
        removeItem = findViewById(R.id.btnDelete);

        productName.setText(name);
        productDescription.setText(description);

        Glide.with(DetailPage.this).load(imageUri).into(productImage);

        editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailPage.this, EditItemPage.class);
                startActivity(intent);
                intent.putExtra("name", productName.toString());
                intent.putExtra("description", productDescription.toString());

            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
}