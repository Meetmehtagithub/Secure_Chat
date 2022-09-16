package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class specifichat extends AppCompatActivity {



EditText  mgetmessage;
ImageButton msendmessagebutton;
CardView msendmessagecardview;
androidx.appcompat.widget.Toolbar mtoolbarofspecificchat;
ImageView imageViewofspecificuser;
TextView mnameofspecificuser;
private String entermessage;
Intent intent;
String mrecievername,sendername,mrecieveruid,msenderuid;
private FirebaseAuth firebaseAuth;
FirebaseDatabase firebaseDatabase;
String senderroom,reciverroom;
ImageButton mbackbuttonofspecificchat,fi;

RecyclerView mmessagerecycleview;
String currenttime;
Calendar calendar;
SimpleDateFormat simpleDateFormat;

    messagesAdapter messagesAdapter;
    ArrayList<Messages> messagesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specifichat);

        mgetmessage=findViewById(R.id.getmessage);
        msendmessagecardview=findViewById(R.id.cardviewofsendmessage);
        fi=findViewById(R.id.file);
        msendmessagebutton=findViewById(R.id.imageviewofsendmessage);

        mtoolbarofspecificchat=findViewById(R.id.toolBarofspecificchat);
        mnameofspecificuser=findViewById(R.id.nameofspecificchatuser);
        imageViewofspecificuser=findViewById(R.id.specificchatuserimageviewofuser);
        mbackbuttonofspecificchat=findViewById(R.id.backButtonOfspecificchat);
        messagesArrayList=new ArrayList<>();
        mmessagerecycleview=findViewById(R.id.recycleviewofspecific);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessagerecycleview.setLayoutManager(linearLayoutManager);
        messagesAdapter=new messagesAdapter(specifichat.this,messagesArrayList);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        calendar=Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");

        msenderuid=firebaseAuth.getUid();
        mrecieveruid=getIntent().getStringExtra("reciveseruid");
        mrecievername=getIntent().getStringExtra("name");
        senderroom=msenderuid+mrecieveruid;
        reciverroom=mrecieveruid+msenderuid;
        intent=getIntent();
        intent=getIntent();
        setSupportActionBar(mtoolbarofspecificchat);
        mtoolbarofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"toolbar is clicked",Toast.LENGTH_SHORT).show();

            }
        });

    fi.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(getApplicationContext(),"clicked" ,Toast.LENGTH_SHORT).show();
        }
    });


            DatabaseReference databaseReference = firebaseDatabase.getReference().child("chats").child(senderroom).child("messages");
            messagesAdapter = new messagesAdapter(specifichat.this, messagesArrayList);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    messagesArrayList.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Messages messages = snapshot1.getValue(Messages.class);

                        messagesArrayList.add(messages);
                    }
                    messagesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        mmessagerecycleview.setAdapter(messagesAdapter);








        mbackbuttonofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mnameofspecificuser.setText(mrecievername);
        String uri = getIntent().getStringExtra("imageuri");
        if(uri.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"null is recieved",Toast.LENGTH_SHORT).show();
        }
        else{
            Picasso.get().load(uri).into(imageViewofspecificuser);
        }
        msendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);
                entermessage=mgetmessage.getText().toString();
                if(entermessage.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"enter the message first",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Date data=new Date();
                    currenttime=simpleDateFormat.format(calendar.getTime());
                    Messages messages=new Messages(entermessage, firebaseAuth.getUid(),data.getTime(),currenttime);
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats").child(senderroom).child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseDatabase.getReference().child("chats").child(reciverroom).child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mmessagerecycleview.scrollBy(0,1000000000);
                                }
                            });
                        }
                    });
                    mgetmessage.setText("");

                }
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(messagesAdapter!=null)
        {
            messagesAdapter.notifyDataSetChanged();
        }
    }
}