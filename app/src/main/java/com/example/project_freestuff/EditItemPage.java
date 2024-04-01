package com.example.project_freestuff;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class EditItemPage extends AppCompatActivity {

    EditText name, description;
    ImageView productImage;
    Button saveEditedItem, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_page);

        name = findViewById(R.id.editProductName);
        description = findViewById(R.id.editProductDescription);
        productImage = findViewById(R.id.imageProduct);
        saveEditedItem = findViewById(R.id.btnSave);
        cancel = findViewById(R.id.btnCancel);

        //getting values from the intent
        String nameProduct = getIntent().getStringExtra("name");
        String descriptionProduct = getIntent().getStringExtra("description");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        //Intent intent = getIntent();

        name.setText(nameProduct);
        description.setText(descriptionProduct);
        //Load image from the Storage using the url received in the intent
        Glide.with(EditItemPage.this).load(imageUrl).into(productImage);


    }
}