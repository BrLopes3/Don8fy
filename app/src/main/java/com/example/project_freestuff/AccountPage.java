package com.example.project_freestuff;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountPage extends AppCompatActivity {

    EditText nameUser,  passUser;

    TextView emailUser;

    Button editUser, deleteUser, back;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        nameUser = findViewById(R.id.nametxt);
        emailUser = findViewById(R.id.emailtxt);
        passUser = findViewById(R.id.passwordtxt);

        editUser = findViewById(R.id.edit);
        deleteUser = findViewById(R.id.delete);
        back = findViewById(R.id.btnBack);


        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String userName = extras.getString("name");
            String usermail = extras.getString("email");
            String userpassword = extras.getString("password");

            nameUser.setText(userName);
            emailUser.setText(usermail);
            passUser.setText(userpassword);
        }

        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserAccount();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountPage.this, MainActivity.class);
                startActivity(intent);

            }
        });


    }

    private void updateUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            String newName = nameUser.getText().toString();
            String newPassword = passUser.getText().toString();

            if(!TextUtils.isEmpty(newName)){
                usersRef.child(user.getUid()).child("name").setValue(newName);
            }

            if(!TextUtils.isEmpty(newPassword)){
                user.updatePassword(newPassword);
                usersRef.child(user.getUid()).child("password").setValue(newPassword);
            }
            Toast.makeText(AccountPage.this, "User Updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteUserAccount() {

        FirebaseUser user = mAuth.getCurrentUser();
        if(user!= null){
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        //delete also from the database
                        usersRef.child(user.getUid()).removeValue();
                        Toast.makeText(AccountPage.this, "User account deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AccountPage.this, SignUpPage.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(AccountPage.this, "Failed to delete the user", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}