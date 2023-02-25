package com.pspr.textme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity{

    EditText et1,et2,et3,et4,et5,et6;
    Button verify;
    LinearLayout lh;
    String CodeSent;
    String userNumber;
    String userCode;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        et1  = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);
        et6 = findViewById(R.id.et6);
        verify = findViewById(R.id.iverify);
        lh = findViewById(R.id.lh);
        getSupportActionBar().hide();


        EditText[] edit = {et1, et2, et3, et4,et5,et6};

        et1.addTextChangedListener(new GenericTextWatcher(et1, edit));
        et2.addTextChangedListener(new GenericTextWatcher(et2, edit));
        et3.addTextChangedListener(new GenericTextWatcher(et3, edit));
        et4.addTextChangedListener(new GenericTextWatcher(et4, edit));
        et5.addTextChangedListener(new GenericTextWatcher(et5, edit));
        et6.addTextChangedListener(new GenericTextWatcher(et6, edit));


        Intent i = getIntent();
        userNumber = i.getStringExtra("number");
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91"+userNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(otp.this)
                .setCallbacks(mCallBacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signWithCode();
            }
        });


    }


    public void signWithCode(){

        userCode = et1.getText().toString()+et2.getText().toString()+et3.getText().toString()
                +et4.getText().toString()+et5.getText().toString()+et6.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeSent,userCode);
        signInPhoneAuthCredential(credential);
    }


    public void signInPhoneAuthCredential(PhoneAuthCredential credential){

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(otp.this,Details.class);
                            startActivity(i);
                            finish();
                        }
                        else{
                            Toast.makeText(otp.this, "Entered otp is not correct", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            CodeSent = s;
        }
    };



}