package com.example.project_freestuff;

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

public class LoginPage extends AppCompatActivity {
    Button btnLogIn, btnSignUp;

    EditText email, password;
    ImageButton btnEye;

    boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        btnLogIn = findViewById(R.id.login);
        btnSignUp = findViewById(R.id.signUpbtn);
        btnEye = findViewById(R.id.eyebtn);
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
                Log.d("Debug", "Button clicked"); // Adicione este log
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

