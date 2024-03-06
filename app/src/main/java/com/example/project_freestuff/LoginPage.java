package com.example.project_freestuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginPage extends AppCompatActivity {
    Button btnLogIn, btnSignUp;

    EditText email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        btnLogIn = findViewById(R.id.login);
        btnSignUp = findViewById(R.id.signUpbtn);
        email = findViewById(R.id.emailtxt);
        password = findViewById(R.id.passwordtxt);

        //Retrieve email and password from the signup page
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String usermail = extras.getString("email");
            String userpassword = extras.getString("password");

            email.setText(usermail);
            password.setText(userpassword);
        }



        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                startActivity(intent);
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
}