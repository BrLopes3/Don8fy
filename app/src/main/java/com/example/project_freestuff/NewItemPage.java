package com.example.project_freestuff;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.net.Uri;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;


import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class NewItemPage extends AppCompatActivity implements OnMapReadyCallback {

    ImageView productImage;
    EditText name, description;
    Button takePhoto, saveItem, back;
    Uri imageUri;
    String itemPosition;

    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap locationMap;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_page);

        productImage = findViewById(R.id.imageProduct);
        name = findViewById(R.id.productName);
        description = findViewById(R.id.productDescription);
        takePhoto = findViewById(R.id.btnTakePhoto);
        saveItem = findViewById(R.id.btnSave);
        back = findViewById(R.id.btnBack);

        //google maps
        FrameLayout frameLayout = findViewById(R.id.maps);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();


        //request for camera runtime permission
        if (ContextCompat.checkSelfPermission(NewItemPage.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(NewItemPage.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //function to upload the image into the Firebase Storage
                uploadImage(productImage);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewItemPage.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;

                    SupportMapFragment mapFragment = SupportMapFragment.newInstance();

                    getSupportFragmentManager().beginTransaction().replace(R.id.maps, mapFragment).commit();

                    mapFragment.getMapAsync(NewItemPage.this);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else{
                Toast.makeText(this, "Location permission is denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            productImage.setImageBitmap(bitmap);
        }
    }

    private void uploadImage(ImageView image){
        //Get the Bitmap from the ImageView
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        if (drawable == null){
            Toast.makeText(NewItemPage.this, "No Image Uploaded", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap bitmap = drawable.getBitmap();

        //convert Bitmap into an Uri using a function
        imageUri = getImageUri(bitmap);

        //Upload the image to Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("images/"+UUID.randomUUID().toString());

        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(NewItemPage.this, "Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                //Get the Url of the uploaded image
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String itemName = name.getText().toString();
                        String itemDescription = description.getText().toString();
                        String uriImage = uri.toString();
                        uploadItem(itemName, itemDescription, uriImage, itemPosition);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NewItemPage.this, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NewItemPage.this, "Fail on Upload Image", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadItem(String name, String description, String imageUrl, String map){

        //Initialize Firebase RealTime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("items");

        //upload the itemModel object to the Database
        String itemId = databaseRef.push().getKey(); //generate a unique ID for the item
        if (itemId != null){
            //instantiate the ItemModel class
            ItemModel itemModel = new ItemModel(itemId, name, description, imageUrl, map);
            databaseRef.child(itemId).setValue(itemModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(NewItemPage.this, "New Item Saved!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(NewItemPage.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewItemPage.this, "Error: New Item NOT Saved!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Image", null);
        return Uri.parse(path);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        locationMap = googleMap;

        LatLng mapPosition = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        locationMap.addMarker(new MarkerOptions().position(mapPosition).title("Object Location"));
        locationMap.moveCamera(CameraUpdateFactory.newLatLng(mapPosition));

        itemPosition = Double.toString(mapPosition.latitude) + "," + Double.toString(mapPosition.longitude);


    }
}