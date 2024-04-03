package com.example.project_freestuff;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity {

    Button btnregister, btnsignin;
    ImageButton btneye, btneye2;
    EditText txtname, txtemail, txtpassword, txtconfirmpassword;
    private FirebaseAuth mAuth;

    boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        // Inicialize o FirebaseApp
        FirebaseApp.initializeApp(this);


        btnregister = findViewById(R.id.registerbtn);
        btnsignin = findViewById(R.id.signInbtn);
        btneye = findViewById(R.id.eyebtn);
        btneye2 = findViewById(R.id.eyebtn2);

        mAuth = FirebaseAuth.getInstance();


        txtname = findViewById(R.id.fullnametxt);
        txtemail = findViewById(R.id.emailtxt);
        txtpassword = findViewById(R.id.passwordtxt);
        txtconfirmpassword = findViewById(R.id.confirmpasswordtxt);


        //configurate the toggle button to see the password
        btneye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPasswordVisible){
                    txtpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btneye.setImageResource(R.drawable.eyeopen);
                }else{
                    txtpassword.setTransformationMethod(null);
                    btneye.setImageResource(R.drawable.eyeclosed);
                }
                isPasswordVisible = !isPasswordVisible;
            }
        });

        btneye2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPasswordVisible){
                    txtconfirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btneye2.setImageResource(R.drawable.eyeopen);
                }else{
                    txtconfirmpassword.setTransformationMethod(null);
                    btneye2.setImageResource(R.drawable.eyeclosed);
                }
                isPasswordVisible = !isPasswordVisible;
            }
        });

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser();
            }
        });

    }
    private void registerUser() {
        String userName = txtname.getText().toString().trim();
        String userEmail = txtemail.getText().toString().trim();
        String userPassword = txtpassword.getText().toString().trim();
        String userConfirmPassword = txtconfirmpassword.getText().toString().trim();

        //Initialize Firebase RealTime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");


        if (userName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userConfirmPassword.isEmpty()) {
            Toast.makeText(this, "All the field should be filled!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userPassword.length() < 8) {
            txtpassword.setError("Password should be more than 8 char");
            txtpassword.requestFocus();
            return;
        }
        if (!userPassword.equals(userConfirmPassword)) {
            txtconfirmpassword.setError("Password and confirmation is not match");
            txtconfirmpassword.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            txtemail.setError("Please Put the valid Email address");
            txtemail.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(userEmail,userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignUpPage.this,
                                    "Register Success :) ", Toast.LENGTH_SHORT).show();
                            UserModel user = new UserModel(userName, userEmail, userPassword);
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String userId = currentUser.getUid();
                            //String userId = databaseRef.push().getKey();
                            databaseRef.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(SignUpPage.this,LoginPage.class);
                                    intent.putExtra("name",user.name);
                                    intent.putExtra("password",user.password);
                                    intent.putExtra("email", user.email);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpPage.this, "Failed in save new User", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else{
                            Toast.makeText(SignUpPage.this, "Register Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}