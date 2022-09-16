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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SetProfile extends AppCompatActivity {

    private CardView MGetUserImage;
    private ImageView MGetUserImageINImageView;
    private static int Pick_Img=123;
    private Uri ImagePath;
    private EditText MGetUserName;
    private android.widget.Button MSaveProfile;
    private FirebaseAuth firebaseAuth;
    private String name;
    private FirebaseStorage FireBaseStorage;
    private StorageReference storageReference;
    private String ImageUriAcessToken;
    private FirebaseFirestore FireBaseFireStore;
    ProgressBar MprogressBarofSetProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_set_profile);

        firebaseAuth=FirebaseAuth.getInstance();
        FireBaseStorage=FireBaseStorage.getInstance();
        storageReference=FireBaseStorage.getReference();
        FireBaseFireStore=FirebaseFirestore.getInstance();
        MGetUserName=findViewById(R.id.getUserName);
        MGetUserImage=findViewById(R.id.getUserImage);
        MGetUserImageINImageView=findViewById(R.id.getUserImageInImageview);
        MSaveProfile=findViewById(R.id.saveProfile);
        MprogressBarofSetProfile=findViewById(R.id.profressbarOfSaveProfile);
        MprogressBarofSetProfile.setVisibility(View.INVISIBLE);
        MGetUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,Pick_Img);
            }
        });

        MSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=MGetUserName.getText().toString();
                if(name.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Name is Empty", Toast.LENGTH_SHORT).show();
                }
                else if(ImagePath==null)
                {
                    Toast.makeText(getApplicationContext(), "Image is NOT selctead", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    MprogressBarofSetProfile.setVisibility(View.VISIBLE);
                    try {
                        sendDataForNewUser();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();

                    }
                    MprogressBarofSetProfile.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(SetProfile.this,ChatActivity.class);
                    startActivity(intent);
                    finish();
                }
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
        name=MGetUserName.getText().toString().trim();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        userProfile MuserProfile= new userProfile(name,firebaseAuth.getUid());
        databaseReference.setValue(MuserProfile);
        sendImageToStorage();
    }
    private void sendImageToStorage()
    {
        StorageReference ImageRef= storageReference.child("Images").child(firebaseAuth.getUid()).child("Profile Pic");
        Bitmap bimap=null;
        try {
            bimap=MediaStore.Images.Media.getBitmap(getContentResolver(),ImagePath);

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
                        ImageUriAcessToken=uri.toString();
                        Toast.makeText(getApplicationContext(), "URL get success", Toast.LENGTH_SHORT).show();
                        sendDataToCloudFireStore();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Url get Failed", Toast.LENGTH_SHORT).show();
                    }
                });

                Toast.makeText(getApplicationContext(), "Image is upload", Toast.LENGTH_SHORT).show();
            }


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
        DocumentReference documentReference=FireBaseFireStore.collection("Users").document(firebaseAuth.getUid());
        Map<String,Object> userData= new HashMap<>();
        userData.put("name",name);
        userData.put("number",ii);
        userData.put("image",ImageUriAcessToken);
        userData.put("uid",firebaseAuth.getUid());
        userData.put("status","Online");

        documentReference.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Data store Successfully in Cloud", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Pick_Img&&resultCode==RESULT_OK)
        {
            ImagePath=data.getData();
            MGetUserImageINImageView.setImageURI(ImagePath);
        }
    }
}