package com.example.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

public class messagesAdapter extends RecyclerView.Adapter {


    public byte[] enkey = {9,115,51,86,105,4,-31,-23,-68,88,17,20,3,-105,119,-53};
    public Cipher chiper,decipher;
    public SecretKeySpec secretKeySpec;
    public Context context;
    public ArrayList<Messages> messagesArrayList;
    public int ITEM_SEND=1;
    public int ITEM_RECIVE=2;

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

    @SuppressLint("GetInstance")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messages messages=messagesArrayList.get(position);
        System.out.println("ansss"+ Arrays.toString(enkey));
        try {
            chiper=Cipher.getInstance("AES");
            decipher=Cipher.getInstance("AES");
        }
        catch (Exception e)
        {
           System.out.println("error "+ e);
        }
        secretKeySpec=new SecretKeySpec(enkey,"AES");
         String aaa = messages.getMessage();
         aaa=(AESdec(aaa));
         messages.currenttime =AESdec(messages.currenttime);

        if(holder.getClass()==Senderviewholder.class)
        {
            Senderviewholder viewholder= (Senderviewholder)holder;
            viewholder.textViewmessage.setText(aaa);
            viewholder.timeofmessage.setText(messages.currenttime);
        }
        else
        {
            Recieverviewholder viewholder= (Recieverviewholder) holder;
            viewholder.textViewmessage.setText(aaa);
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
        if(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(messages.getSenderId()))
        {
            return ITEM_SEND;
        }
        else
        {
            return ITEM_RECIVE;
        }
    }

    static class  Senderviewholder extends RecyclerView.ViewHolder
    {
        TextView textViewmessage;
        TextView timeofmessage;
        public Senderviewholder(@NonNull View itemView) {
            super(itemView);

            textViewmessage=itemView.findViewById(R.id.sendmessage);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
        }
    }

    static class  Recieverviewholder extends RecyclerView.ViewHolder
    {
        TextView textViewmessage;
        TextView timeofmessage;
        public Recieverviewholder(@NonNull View itemView) {
            super(itemView);
            textViewmessage=itemView.findViewById(R.id.sendmessage);
            timeofmessage=itemView.findViewById(R.id.timeofmessage);
        }
    }

    private String AESdec(String string)
    {
        String returnstring="";
        try {

            byte[] enByte =string.getBytes(StandardCharsets.ISO_8859_1);
            byte[] dec;
            decipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
            dec=decipher.doFinal(enByte);
            returnstring=new String(dec);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return returnstring;
    }

}
