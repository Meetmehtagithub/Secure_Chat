package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;





public class MainActivity extends AppCompatActivity {





    EditText MGetPhoneNumber;
    android.widget.Button MSendOtp;
    CountryCodePicker MCountryCodePicker;
    String CountryCode;
    String PhoneNumber;
    FirebaseAuth firebaseAuth;
    ProgressBar MProgressBarOfMain;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks MCallBack;
    String codesent;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






        setContentView(R.layout.activity_main);
        MCountryCodePicker = findViewById(R.id.CountryCodePickker);
        MSendOtp = findViewById(R.id.SendOtp);
        MGetPhoneNumber=findViewById(R.id.getPhoneNumber);
        MProgressBarOfMain=findViewById(R.id.profressbarOfMain);
        firebaseAuth=FirebaseAuth.getInstance();
        CountryCode= MCountryCodePicker.getSelectedCountryCode();
        MProgressBarOfMain.setVisibility(View.INVISIBLE);
        MCountryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                CountryCode= MCountryCodePicker.getSelectedCountryCode();
            }
        });

        MSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Number;
                Number=MGetPhoneNumber.getText().toString();
                if(Number.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Pleace Enter Your Number", Toast.LENGTH_SHORT).show();
                }
                else if(Number.length()<10) {
                    Toast.makeText(getApplicationContext(), "Pleace Enter Correct Number", Toast.LENGTH_SHORT).show();
                }
                else {
                    MProgressBarOfMain.setVisibility(View.VISIBLE);
                    PhoneNumber ="+"+ CountryCode+Number;

                    PhoneAuthOptions phoneAuthOptions=PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(PhoneNumber)
                            .setTimeout(30L,TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(MCallBack)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
                }
            }
        });
        MCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(),MGetPhoneNumber.getText().toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"OTP is sent",Toast.LENGTH_SHORT).show();
                MProgressBarOfMain.setVisibility(View.INVISIBLE);
                codesent=s;
                Intent intent=new Intent(MainActivity.this,OtpAuthentication.class);
                intent.putExtra("otp",codesent);
                intent.putExtra("number",MGetPhoneNumber.getText().toString());
                startActivity(intent);
            }
        };
    }




    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent intent=new Intent(MainActivity.this,ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}


