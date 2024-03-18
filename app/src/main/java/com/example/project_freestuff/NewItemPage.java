package com.example.project_freestuff;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class NewItemPage extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private ImageView imageProduct;
    Button takePhoto, saveItem;
    ImageView productImage;
    EditText productName, productDescription;

    FirebaseDatabase mydatabase;
    DatabaseReference reference;

    private ActivityResultLauncher<Intent> cameraLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_page);

        productImage = findViewById(R.id.imageProduct);
        takePhoto = findViewById(R.id.btnTakePhoto);
        saveItem = findViewById(R.id.btnSave);
        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);

       takePhoto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               startActivityForResult(cameraIntent, REQUEST_CODE);
           }
       });

       saveItem.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mydatabase = FirebaseDatabase.getInstance();
               reference = mydatabase.getReference("items");

               String itemName = productName.getText().toString();
               String itemDescription = productDescription.getText().toString();
               String imageData = productImage.toString();

               if (itemName.isEmpty() || itemDescription.isEmpty() || productImage.getDrawable() == null){
                   Toast.makeText(NewItemPage.this, "Please, fill all fields", Toast.LENGTH_SHORT).show();
                   return;
               }else{
                   DatabaseReference newItemRef = reference.push();
                   String itemId = newItemRef.getKey().toString();

                   ItemModel item = new ItemModel(itemName, itemDescription, itemId, imageData);
                   String message = "New item created: "+itemId;
                   Toast.makeText(NewItemPage.this, message, Toast.LENGTH_SHORT).show();

                   reference.child(itemId).setValue(item);

                   Toast.makeText(NewItemPage.this, "Success",Toast.LENGTH_SHORT).show();

               }
           }
       });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            productImage.setImageBitmap(photo);
        }else{
            Toast.makeText(this, "Photo Error", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}