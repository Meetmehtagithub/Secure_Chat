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

    private EditText mGetPhoneNumber;
    private CountryCodePicker mCountryCodePicker;
    public String CountryCode;
    public String PhoneNumber;
    public FirebaseAuth firebaseAuth;
    private ProgressBar mProgressBarOfMain;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    public String codesent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mCountryCodePicker = findViewById(R.id.CountryCodePickker);
        Button mSendOtp = findViewById(R.id.SendOtp);
        mGetPhoneNumber=findViewById(R.id.getPhoneNumber);
        mProgressBarOfMain=findViewById(R.id.profressbarOfMain);
        firebaseAuth=FirebaseAuth.getInstance();
        CountryCode= mCountryCodePicker.getSelectedCountryCode();
        mProgressBarOfMain.setVisibility(View.INVISIBLE);
        mCountryCodePicker.setOnCountryChangeListener(() -> CountryCode= mCountryCodePicker.getSelectedCountryCode());

        mSendOtp.setOnClickListener(view -> {

            String Number;
            Number=mGetPhoneNumber.getText().toString();
            if(Number.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Pleace Enter Your Number", Toast.LENGTH_SHORT).show();
            }
            else if(Number.length()<10) {
                Toast.makeText(getApplicationContext(), "Pleace Enter Correct Number", Toast.LENGTH_SHORT).show();
            }
            else {
                mProgressBarOfMain.setVisibility(View.VISIBLE);
                PhoneNumber ="+"+ CountryCode+Number;

                PhoneAuthOptions phoneAuthOptions=PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(PhoneNumber)
                        .setTimeout(30L,TimeUnit.SECONDS)
                        .setActivity(MainActivity.this)
                        .setCallbacks(mCallBack)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
            }
        });
        mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                //Toast.makeText(getApplicationContext(),mGetPhoneNumber.getText().toString(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"OTP is sent",Toast.LENGTH_SHORT).show();
                mProgressBarOfMain.setVisibility(View.INVISIBLE);
                codesent=s;
                Intent intent=new Intent(MainActivity.this,OtpAuthentication.class);
                intent.putExtra("otp",codesent);
                intent.putExtra("number",mGetPhoneNumber.getText().toString());
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


