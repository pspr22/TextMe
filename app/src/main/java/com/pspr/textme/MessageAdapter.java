package com.pspr.textme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    List<ModelClass> list;
    String userName;

    boolean status;
    int send;
    int receive;

    public MessageAdapter(List<ModelClass> list, String userName) {
        this.list = list;
        this.userName = userName;

        status = false;
        send = 1;
        receive = 2;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if(viewType == send){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_send,parent,false);
        }
        else{

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_receive,parent,false);

        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        holder.text.setText(list.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        TextView text;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            if(status){
                text = itemView.findViewById(R.id.textsend);
            }
            else{
                text = itemView.findViewById(R.id.textreceive);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getFrom().equals(userName)){

            status = true;
            return send;
        }
        else{
            status = false;
            return receive;
        }
    }
}