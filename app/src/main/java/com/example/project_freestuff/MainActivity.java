package com.example.project_freestuff;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageButton addBtn, userAccount;
    private ImageListAdapter adapter;
    private ArrayList<ItemModel> itemList;

    String userName, usermail, userpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Retrieve user name, email and password from the login page
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            userName = extras.getString("name");
            Toast.makeText(MainActivity.this, "Hello "+userName, Toast.LENGTH_LONG).show();
            usermail = extras.getString("email");
            userpassword = extras.getString("password");

        }

        addBtn = findViewById(R.id.addBtn);
        userAccount = findViewById(R.id.account);

        //Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();
        adapter = new ImageListAdapter(MainActivity.this, itemList);
        recyclerView.setAdapter(adapter);

        // Retrieve items from Firebase
        adapter.getItems();

        adapter.setOnItemClickListener(new ImageListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(ItemModel item) {
                // Open DetailActivity with item details
                Intent intent = new Intent(MainActivity.this, DetailPage.class);
                intent.putExtra("name", item.name);  // Pass item object to DetailPage
                intent.putExtra("description", item.description);
                intent.putExtra("url", item.imageUri);
                intent.putExtra("itemId", item.itemId);
                intent.putExtra("positionMap", item.positionMap);
                startActivity(intent);
                finish();
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

        userAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                DatabaseReference userRef = database.getReference("users").child(mAuth.getCurrentUser().getUid());

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                           userName = dataSnapshot.child("name").getValue(String.class);
                           usermail = dataSnapshot.child("email").getValue(String.class);
                           userpassword = dataSnapshot.child("password").getValue(String.class);


                            Intent intent = new Intent(MainActivity.this, AccountPage.class);
                            intent.putExtra("name", userName);
                            intent.putExtra("email", usermail);
                            intent.putExtra("password", userpassword);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Error when reading user data.", Toast.LENGTH_SHORT).show();
                    }
                });
            }



        });
    }


}
