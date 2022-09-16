package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class updateProfile extends AppCompatActivity {


    private EditText mnewuserusername;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private TextView mmovetoupdatadprofile;
    private FirebaseFirestore firebaseFirestore;
    private ImageView mgetnewuserimageviewinimageview;
    private StorageReference storageReference;
    private  String ImageURIacessToken;
    private androidx.appcompat.widget.Toolbar mtoolbarofupdateprofile;
    private ImageButton mbackButtonofupdateprofile;
    private FirebaseStorage firebaseStorage;
    private ProgressBar mprogressbarofupdateprofile;
    private Uri imagepath;
    Intent intent ;
    android.widget.Button updateprofilebutton;
    private static int PICK_IMG=123;
    String newName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        mtoolbarofupdateprofile =findViewById(R.id.toolBarofupdateProfileActivity);
        mbackButtonofupdateprofile=findViewById(R.id.backButtonOfupdateProfile);
        mgetnewuserimageviewinimageview=findViewById(R.id.getnewviewUserImageInImageview);
        mprogressbarofupdateprofile=findViewById(R.id.progressofupdateprofile);
        mprogressbarofupdateprofile.setVisibility(View.INVISIBLE);
        mnewuserusername=findViewById(R.id.getnewviewUserName);
        updateprofilebutton=findViewById(R.id.updateprofilebutton);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        intent=getIntent();
        setSupportActionBar(mtoolbarofupdateprofile);
        mbackButtonofupdateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mnewuserusername.setText(intent.getStringExtra("nameofuser"));
        DatabaseReference databaseReference =firebaseDatabase.getReference(firebaseAuth.getUid());
        updateprofilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newName=mnewuserusername.getText().toString();
                if(newName.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Name is empty",Toast.LENGTH_SHORT).show();
                }
                else if(imagepath!=null)
                {
                    mprogressbarofupdateprofile.setVisibility(View.VISIBLE);
                    userProfile muserprofile = new userProfile(newName,firebaseAuth.getUid());
                    databaseReference.setValue(muserprofile);
                    updateimagetostorage();
                    Toast.makeText(getApplicationContext(),"Update",Toast.LENGTH_SHORT).show();
                    mprogressbarofupdateprofile.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(updateProfile.this,ChatActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    mprogressbarofupdateprofile.setVisibility(View.VISIBLE);
                    userProfile muserprofile = new userProfile(newName,firebaseAuth.getUid());
                    databaseReference.setValue(muserprofile);
                    updatenameofcloudfirestorege();
                    Toast.makeText(getApplicationContext(),"Update",Toast.LENGTH_SHORT).show();
                    mprogressbarofupdateprofile.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(updateProfile.this,ChatActivity.class);
                    startActivity(intent);
                    finish();
                }



            }
        });

mgetnewuserimageviewinimageview.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent,PICK_IMG);
    }
});
    storageReference=firebaseStorage.getReference();
    storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        @Override
        public void onSuccess(Uri uri) {
            ImageURIacessToken=uri.toString();
            Picasso.get().load(uri).into(mgetnewuserimageviewinimageview);
        }
    });

    }

    private void updatenameofcloudfirestorege() {

        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String,Object> userData= new HashMap<>();
        userData.put("name",newName);
        userData.put("image",ImageURIacessToken);
        userData.put("uid",firebaseAuth.getUid());
        userData.put("status","Online");
        documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Profile update Successfully in Cloud", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void updateimagetostorage() {
        StorageReference ImageRef= storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Pic");
        Bitmap bimap=null;
        try {
            bimap=MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bimap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data =byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = ImageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageURIacessToken=uri.toString();
                        Toast.makeText(getApplicationContext(), "URL get success", Toast.LENGTH_SHORT).show();
                        updatenameofcloudfirestorege();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Url get Failed", Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(getApplicationContext(), "Image is update", Toast.LENGTH_SHORT).show();
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMG&&resultCode==RESULT_OK)
        {
            imagepath=data.getData();
            mgetnewuserimageviewinimageview.setImageURI(imagepath);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"user is offline",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference=firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        documentReference.update("status","Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"user is online",Toast.LENGTH_SHORT).show();
            }
        });
    }





}