package com.example.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class messagesAdapter extends RecyclerView.Adapter {


    Context context;
    ArrayList<Messages> messagesArrayList;

    int ITEM_SEND=1;
    int ITEM_RECIVE=2;

    public messagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sendercharlayout,parent,false);
            return new Senderviewholder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.recycerercharlayout,parent,false);
            return new Recieverviewholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Messages messages=messagesArrayList.get(position);
        if(holder.getClass()==Senderviewholder.class)
        {
            Senderviewholder viewholder= (Senderviewholder)holder;
            viewholder.textViewmessage.setText(messages.getMessage());
            viewholder.timeofmessage.setText(messages.currenttime);
        }
        else
        {
            Recieverviewholder viewholder= (Recieverviewholder) holder;
            viewholder.textViewmessage.setText(messages.getMessage());
            viewholder.timeofmessage.setText(messages.currenttime);
        }




    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages=messagesArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId()))
        {
            return ITEM_SEND;
        }
        else
        {
            return ITEM_RECIVE;
        }
    }


    class  Senderviewholder extends RecyclerView.ViewHolder
    {
        TextView textViewmessage;
        TextView timeofmessage;
        public Senderviewholder(@NonNull View itemView) {
            super(itemView);

            textViewmessage=itemView.findViewById(R.id.sendmessage);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
        }

    }

    class  Recieverviewholder extends RecyclerView.ViewHolder
    {
        TextView textViewmessage;
        TextView timeofmessage;
        public Recieverviewholder(@NonNull View itemView) {
            super(itemView);

            textViewmessage=itemView.findViewById(R.id.sendmessage);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
        }

    }




}
