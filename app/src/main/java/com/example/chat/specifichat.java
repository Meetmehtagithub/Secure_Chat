package com.example.chat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

public class specifichat extends AppCompatActivity {


    public FirebaseDatabase firebaseDatabase1;
    public String a;
    private EditText  mgetmessage;
    private ImageButton msendmessagebutton;
    private CardView msendmessagecardview;
    private androidx.appcompat.widget.Toolbar mtoolbarofspecificchat;
    public ImageView imageViewofspecificuser;
    private TextView mnameofspecificuser;
    public String entermessage;
    public Intent intent;
    private String mrecievername,msendername,mrecieveruid,msenderuid;
    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public String senderroom,reciverroom;
    private ImageButton mbackbuttonofspecificchat,fi;
    private Random r = new Random();
    private RecyclerView mmessagerecycleview;
    public String currenttime;
    public String fcmMessage;
    public Calendar calendar;
    public SimpleDateFormat simpleDateFormat;
    public RequestQueue requestQueue;
    public String token;
    public String URL="https://fcm.googleapis.com/fcm/send";
    public messagesAdapter messagesAdapter;
    public ArrayList<Messages> messagesArrayList;
    public byte[] enkey = {9,115,51,86,105,4,-31,-23,-68,88,17,20,3,-105,119,-53};
    public Cipher chiper,decipher;
    public SecretKeySpec secretKeySpec;



    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_specifichat);
        requestQueue = Volley.newRequestQueue(this);
        mgetmessage=findViewById(R.id.getmessage);
        msendmessagecardview=findViewById(R.id.cardviewofsendmessage);
        fi=findViewById(R.id.file);
        msendmessagebutton=findViewById(R.id.imageviewofsendmessage);
        firebaseDatabase1=FirebaseDatabase.getInstance();
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
        token=getIntent().getStringExtra("token");
        senderroom=msenderuid+mrecieveruid;
        reciverroom=mrecieveruid+msenderuid;
        intent=getIntent();
        intent=getIntent();


        DatabaseReference databaseReference1 =firebaseDatabase.getReference(Objects.requireNonNull(firebaseAuth.getUid()));
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile msuerprofile=snapshot.getValue(userProfile.class);
                assert msuerprofile != null;
                msendername = msuerprofile.getUserName();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"failed to fetch ",Toast.LENGTH_SHORT).show();
            }
        });
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
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    mmessagerecycleview.scrollBy(0,1000000000);
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
            @SuppressLint("GetInstance")
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
                            Date date=new Date();
                            currenttime=simpleDateFormat.format(calendar.getTime());
                            secretKeySpec=new SecretKeySpec(enkey,"AES");
                            try {
                                chiper=Cipher.getInstance("AES");
                                decipher=Cipher.getInstance("AES");
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                            }
                            fcmMessage=entermessage;
                            entermessage = AESen(entermessage);
                            currenttime = AESen(currenttime);
                            Messages messages=new Messages(entermessage, firebaseAuth.getUid(),date.getTime(),currenttime);
                            firebaseDatabase=FirebaseDatabase.getInstance();
                            firebaseDatabase.getReference().child("chats").child(senderroom).child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firebaseDatabase.getReference().child("chats").child(reciverroom).child("messages").push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            mmessagerecycleview.scrollBy(0,1000000000);
                                            try {



                                                FCMsend.pushNotification(specifichat.this,token,msendername,fcmMessage);

                                            }
                                            catch (Exception e)
                                            {
                                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }
                            });
                            mgetmessage.setText("");

                        }
                    }
        });

    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onStop() {
        super.onStop();
        if(messagesAdapter!=null)
        {
            messagesAdapter.notifyDataSetChanged();
        }
    }
    private String AESen(String string)
    {
        byte[] stringByte=string.getBytes(StandardCharsets.UTF_8);
        byte[] encByte=new byte[stringByte.length];
        try {
            chiper.init(Cipher.ENCRYPT_MODE,secretKeySpec);
            encByte=chiper.doFinal(stringByte);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        String returnstring="";

        returnstring = new String(encByte, StandardCharsets.ISO_8859_1);
        return returnstring;
    }

    static long convertToLong(byte[] bytes)
    {
        long value = 0L;

        // Iterating through for loop
        for (byte b : bytes) {
            // Shifting previous value 8 bits to right and
            // add it with next value
            value = (value << 8) + (b & 255);
        }

        return value;
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