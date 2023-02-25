package com.pspr.textme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class UpdateProfile extends AppCompatActivity{

    ImageView image;
    EditText userName;
    FloatingActionButton Update;
    FirebaseDatabase database;
    DatabaseReference  reference;
    FirebaseAuth auth;
    FirebaseUser user;
    Uri imageUri;
    boolean imageControl;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    String simage;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        image = findViewById(R.id.imageu);
        userName = findViewById(R.id.etUserNameu);
        Update = findViewById(R.id.fnextu);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        getUserInfo();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fileChooser();


            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                updateProfile();

            }
        });


    }

    public void updateProfile(){

        String username = userName.getText().toString();
        reference.child("users").child(user.getUid()).child("userName").setValue(username);
        if(imageControl){

            UUID randomId = UUID.randomUUID();
            String imageName = "images/"+randomId+".jpg";
            storageReference.child(imageName).putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            StorageReference myStorageRef = firebaseStorage.getReference(imageName);
                            myStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String filePath = uri.toString();
                                    reference.child("users").child(auth.getUid()).child("image").setValue(filePath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(UpdateProfile.this, "Image Upload Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(UpdateProfile.this, "Image Upload failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    });
        }
        else{
            reference.child("users").child(auth.getUid()).child("image").setValue(simage);
        }

        Intent i = new Intent(UpdateProfile.this,MainActivity.class);
        startActivity(i);
        finish();
    }

    public void getUserInfo(){

        reference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = snapshot.child("userName").getValue().toString();
                simage = snapshot.child("image").getValue().toString();
                userName.setText(name);
                if(simage.equals("null")){
                    image.setImageResource(R.drawable.pp);
                }
                else{
                    Picasso.get().load(simage).into(image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void fileChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){

            imageUri = data.getData();
            Picasso.get().load(imageUri).into(image);
            imageControl = true;
        }
        else{
            imageControl = false;
        }

    }



















}