package com.pspr.textme;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

   List<String> userList;
   String userName;
   Context mContext;

   FirebaseDatabase database;
   DatabaseReference reference;

    public UserAdapter(List<String> userList, String userName, Context mContext) {
        this.userList = userList;
        this.userName = userName;
        this.mContext = mContext;

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        reference.child("users").child(userList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String otherName = snapshot.child("userName").getValue().toString();
                String imageURl = snapshot.child("image").getValue().toString();

                holder.pName.setText(otherName);

                if(imageURl.equals("null")){
                    holder.pImage.setImageResource(R.drawable.pp);
                }
                else{
                    Picasso.get().load(imageURl).into(holder.pImage);
                }


                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(mContext,Chat.class);
                        intent.putExtra("userName",userName);
                        intent.putExtra("otherName",otherName);
                        mContext.startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView pName;
        ImageView pImage;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pName = itemView.findViewById(R.id.pname);
            pImage = itemView.findViewById(R.id.imagep);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }


}
