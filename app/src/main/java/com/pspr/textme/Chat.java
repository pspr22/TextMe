package com.pspr.textme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat extends AppCompatActivity {

    EditText ctext;
    TextView cname;
    FloatingActionButton fab;
    RecyclerView rvchat;
    ImageView imgBack;
    String otherName, Username;

    FirebaseDatabase database;
    DatabaseReference reference;

    MessageAdapter adapter;
    List<ModelClass> list;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ctext = findViewById(R.id.ctext);
        cname = findViewById(R.id.cname);
        fab = findViewById(R.id.fab);
        rvchat = findViewById(R.id.rvchat);
        imgBack = findViewById(R.id.imgback);

        rvchat.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        Intent i = getIntent();
        otherName = i.getStringExtra("otherName");
        Username = i.getStringExtra("userName");

        cname.setText(otherName);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Chat.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String meassage = ctext.getText().toString();
                if (!meassage.equals("")) {
                    sendMessage(meassage);
                    ctext.setText("");
                }
            }
        });


        getMessage();

    }


    public void sendMessage(String message) {

        String key = reference.child("Messages").child(otherName).push().getKey();
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("message", message);
        messageMap.put("from", Username);
        reference.child("Messages").child(Username).child(otherName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    reference.child("Messages").child(otherName).child(Username).child(key).setValue(messageMap);
                }

            }
        });

    }

    public void getMessage(){
        reference.child("Messages").child(Username).child(otherName)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ModelClass modelClass = snapshot.getValue(ModelClass.class);
                list.add(modelClass);
                adapter.notifyDataSetChanged();
                rvchat.scrollToPosition(list.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adapter = new MessageAdapter(list,Username);
        rvchat.setAdapter(adapter);

    }
}