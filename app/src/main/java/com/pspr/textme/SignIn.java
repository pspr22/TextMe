package com.pspr.textme;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

public class SignIn extends AppCompatActivity{

    EditText Phone;
    Button send;
    FirebaseAuth auth = FirebaseAuth.getInstance();



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Phone = findViewById(R.id.etphone);

        send = findViewById(R.id.send);
        getSupportActionBar().hide();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String userNumber = Phone.getText().toString();


                Intent i = new Intent(SignIn.this,otp.class);
                i.putExtra("number",userNumber);
                startActivity(i);
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null) {
            Intent i = new Intent(SignIn.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}