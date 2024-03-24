package com.example.project_freestuff;

import android.content.Context;
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
import android.net.Uri;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.ByteArrayOutputStream;

public class NewItemPage extends AppCompatActivity {

    private static final int REQUEST_CODE = 22;
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

               String itemName = productName.getText().toString();
               String itemDescription = productDescription.getText().toString();
               Bitmap photo = ((BitmapDrawable) productImage.getDrawable()).getBitmap();
               Uri imageUri = getImageUri(getApplicationContext(), photo);

               if (itemName.isEmpty() || itemDescription.isEmpty() || productImage == null){
                   Toast.makeText(NewItemPage.this, "Please, fill all fields", Toast.LENGTH_SHORT).show();
                   return;
               }else{
                       // Upload the image to Firebase Storage
                       uploadImageToFirebaseStorage(imageUri, itemName, itemDescription);
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
            Toast.makeText(NewItemPage.this, "Photo Error", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri, String itemName, String itemDescription){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images");
        // Create a unique filename
        String imageName = "image" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child(imageName);

        //upload image to the Firebase Storage
        imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            // Image uploaded successfully, get the download URL
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Use the image URL as imageData
                String imageUrl = uri.toString();
                saveItemToFirebaseDatabase(itemName, itemDescription, imageUrl);
                Toast.makeText(NewItemPage.this, "Image uploaded", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(NewItemPage.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
            });
            }).addOnFailureListener(e -> {
            Toast.makeText(NewItemPage.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
        });

    }

    private void saveItemToFirebaseDatabase(String itemName, String itemDescription, String imageUrl) {

        // Initialize Firebase database and reference
        mydatabase = FirebaseDatabase.getInstance();
        reference = mydatabase.getReference("items");

        // Push a new item to the database
        DatabaseReference newItemRef = reference.push();
        String itemId = newItemRef.getKey();

        // Create an ItemModel object
        ItemModel item = new ItemModel(itemName, itemDescription, itemId, imageUrl);

        // Save item to Firebase database
        reference.child(itemId).setValue(item);

        // Display success message
        Toast.makeText(NewItemPage.this, "Item saved successfully", Toast.LENGTH_SHORT).show();

    }

    // Method to convert Bitmap to Uri
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
}