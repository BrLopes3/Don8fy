package com.example.project_freestuff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpPage extends AppCompatActivity {

    Button btncontinue, btnsignin;
    EditText name, email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        btncontinue = findViewById(R.id.continuebtn);
        btnsignin = findViewById(R.id.signInbtn);

        name = findViewById(R.id.usernametxt);
        email = findViewById(R.id.emailtxt);
        password = findViewById(R.id.passwordtxt);

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(intent);
            }
        });

        btncontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String useremail = email.getText().toString();
                String userpassword = password.getText().toString();

                Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                intent.putExtra("email", useremail);
                intent.putExtra("password", userpassword);
                startActivity(intent);

            }
        });



    }
}