package com.example.project_freestuff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends AppCompatActivity {
    Button btnLogIn, btnSignUp;

    EditText email, password;
    ImageButton btnEye;

    String userName;
    boolean isPasswordVisible = false;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        btnLogIn = findViewById(R.id.login);
        btnSignUp = findViewById(R.id.signUpbtn);
        btnEye = findViewById(R.id.eyebtn);
        email = findViewById(R.id.emailtxt);
        password = findViewById(R.id.passwordtxt);
        mAuth =  FirebaseAuth.getInstance();

        //Retrieve email and password from the signup page
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            userName = extras.getString("name");
            String usermail = extras.getString("email");
            String userpassword = extras.getString("password");

            email.setText(usermail);
            password.setText(userpassword);
        }

        //configurate the toggle button to see the password
        btnEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPasswordVisible){
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnEye.setImageResource(R.drawable.eyeopen);
                }else{
                    password.setTransformationMethod(null);
                    btnEye.setImageResource(R.drawable.eyeclosed);
                }
                isPasswordVisible = !isPasswordVisible;
            }
        });



        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, SignUpPage.class);
                startActivity(intent);
            }
        });

    }


    private void loginUser() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if(userEmail.isEmpty() || userPassword.isEmpty()){
            Toast.makeText(LoginPage.this, "all fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //search the user in the database
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if(currentUser != null){
                        String uid = currentUser.getUid();
                        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    String userName = snapshot.child("name").getValue(String.class);
                                    String userPassword = snapshot.child("password").getValue(String.class);
                                    String userEmail = snapshot.child("email").getValue(String.class);

                                    Toast.makeText(LoginPage.this, "Login Successfull",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                    intent.putExtra("name",userName);
                                    intent.putExtra("password",userPassword);
                                    intent.putExtra("email", userEmail);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(LoginPage.this, "User data not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(LoginPage.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(LoginPage.this, "User Not Authenticated",Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(LoginPage.this, "Login Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}

