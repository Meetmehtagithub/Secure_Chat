package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private EditText mviewusername;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    FirebaseFirestore firebaseFirestore;
    private ImageView mviewuserimageviewinimageview;
    StorageReference storageReference;
    private  String ImageURIacessToken;
    FirebaseStorage firebaseStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mviewuserimageviewinimageview=findViewById(R.id.viewUserImageInImageview);
        mviewusername=findViewById(R.id.viewUserName);
        TextView mmovetoupdatadprofile = findViewById(R.id.movetoupdateprofile);

        firebaseFirestore=FirebaseFirestore.getInstance();
        androidx.appcompat.widget.Toolbar mtoolbarofviewprofile = findViewById(R.id.toolBarofviewProfileActivity);
        ImageButton mbackButtonofviewprofile = findViewById(R.id.backButtonOfviewProfile);
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        setSupportActionBar(mtoolbarofviewprofile);
        mbackButtonofviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        storageReference=firebaseStorage.getReference();
        storageReference.child("Images").child(Objects.requireNonNull(firebaseAuth.getUid())).child("Profile Pic").getDownloadUrl().addOnSuccessListener(uri -> {

            ImageURIacessToken=uri.toString();

            Picasso.get().load(uri).into(mviewuserimageviewinimageview);

        });

        DatabaseReference databaseReference =firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile msuerprofile=snapshot.getValue(userProfile.class);
                assert msuerprofile != null;
                mviewusername.setText(msuerprofile.getUserName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"failed to fetch ",Toast.LENGTH_SHORT).show();
            }
        });

        mmovetoupdatadprofile.setOnClickListener(view -> {
            Intent intent=new Intent(ProfileActivity.this,updateProfile.class);

            intent.putExtra("nameofuser",mviewusername.getText().toString());
            startActivity(intent);

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(Objects.requireNonNull(firebaseAuth.getUid()));
        Task<Void> voidTask = documentReference.update("status", "offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "user is offline", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(Objects.requireNonNull(firebaseAuth.getUid()));
        documentReference.update("status","Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"user is online",Toast.LENGTH_SHORT).show();
            }
        });
    }
}