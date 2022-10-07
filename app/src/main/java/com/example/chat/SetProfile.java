package com.example.chat;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SetProfile extends AppCompatActivity {

    private ImageView mGetUserImageINImageView;
    private static final int sPick_Img=123;
    private Uri mImagePath;
    private EditText mGetUserName;
    private FirebaseAuth mfirebaseAuth;
    public String name;
    private StorageReference mstorageReference;
    private String mImageUriAcessToken;
    private FirebaseFirestore mFireBaseFireStore;
    public String token;
    private ProgressBar mprogressBarofSetProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// token

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    token = task.getResult();
                    // Get new FCM registration token
                    // Log and toast
                    System.out.println("token " + token);
                });
        setContentView(R.layout.activity_set_profile);
        mfirebaseAuth=FirebaseAuth.getInstance();
        FirebaseStorage mFireBaseStorage = FirebaseStorage.getInstance();
        mstorageReference= mFireBaseStorage.getReference();
        mFireBaseFireStore=FirebaseFirestore.getInstance();
        mGetUserName=findViewById(R.id.getUserName);
        CardView mGetUserImage = findViewById(R.id.getUserImage);
        mGetUserImageINImageView=findViewById(R.id.getUserImageInImageview);
        android.widget.Button mSaveProfile = findViewById(R.id.saveProfile);
        mprogressBarofSetProfile=findViewById(R.id.profressbarOfSaveProfile);
        mprogressBarofSetProfile.setVisibility(View.INVISIBLE);
        mGetUserImage.setOnClickListener(view -> {

            Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(intent,sPick_Img);
        });

        mSaveProfile.setOnClickListener(view -> {
            name=mGetUserName.getText().toString();
            if(name.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Name is Empty", Toast.LENGTH_SHORT).show();
            }
            else if(mImagePath==null)
            {
                Toast.makeText(getApplicationContext(), "Image is NOT selctead", Toast.LENGTH_SHORT).show();
            }
            else
            {
                mprogressBarofSetProfile.setVisibility(View.VISIBLE);
                try {
                    sendDataForNewUser();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();

                }
                mprogressBarofSetProfile.setVisibility(View.INVISIBLE);
                //Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(SetProfile.this,ChatActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void sendDataForNewUser()
    {
        sendDataToRealTimeDatabase();
    }

    private void sendDataToRealTimeDatabase()
    {
       // find error
        name=mGetUserName.getText().toString().trim();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference(Objects.requireNonNull(mfirebaseAuth.getUid()));
        userProfile MuserProfile= new userProfile(name,mfirebaseAuth.getUid());
        databaseReference.setValue(MuserProfile);
        sendImageToStorage();
    }
    private void sendImageToStorage()
    {
        StorageReference ImageRef= mstorageReference.child("Images").child(Objects.requireNonNull(mfirebaseAuth.getUid())).child("Profile Pic");
        Bitmap bimap=null;
        try {
            bimap=MediaStore.Images.Media.getBitmap(getContentResolver(),mImagePath);

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }
        // Array
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        assert bimap != null;
        bimap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] data =byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = ImageRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Task<Uri> url_get_failed = ImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    mImageUriAcessToken = uri.toString();
                    //Toast.makeText(getApplicationContext(), "URL get success", Toast.LENGTH_SHORT).show();
                    sendDataToCloudFireStore();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Url get Failed", Toast.LENGTH_SHORT).show();
                }
            });
            //Toast.makeText(getApplicationContext(), "Image is upload", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    });
    }
    private void sendDataToCloudFireStore() {

        Intent i;
        i=getIntent();
        String ii= i.getStringExtra("number");
        Toast.makeText(getApplicationContext(),ii,Toast.LENGTH_SHORT).show();
        DocumentReference documentReference=mFireBaseFireStore.collection("Users").document(Objects.requireNonNull(mfirebaseAuth.getUid()));
        Map<String,Object> userData= new HashMap<>();
        userData.put("name",name);
        userData.put("number",ii);
        userData.put("image",mImageUriAcessToken);
        userData.put("uid",mfirebaseAuth.getUid());
        userData.put("status","Online");
        userData.put("token",token);
        documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //Toast.makeText(getApplicationContext(), "Data store Successfully in Cloud", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==sPick_Img&&resultCode==RESULT_OK)
        {
            assert data != null;
            mImagePath=data.getData();
            mGetUserImageINImageView.setImageURI(mImagePath);
        }
    }
}