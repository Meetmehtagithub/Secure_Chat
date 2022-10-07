package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpAuthentication extends AppCompatActivity {

    private EditText mGetOtp;
    private String mEnterOtp;
    private FirebaseAuth mFireBaseAuth;
    private ProgressBar mprogressBarForAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otp_authentication);
        TextView MChangeNumber = findViewById(R.id.changeNumbear);
        android.widget.Button mverifyOtp = findViewById(R.id.verifyOtp);
        mGetOtp=findViewById(R.id.getOtp);
        mprogressBarForAuth=findViewById(R.id.profressbarOfOtpAuth);

        mFireBaseAuth=FirebaseAuth.getInstance();
        MChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OtpAuthentication.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        mverifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEnterOtp=mGetOtp.getText().toString();
                if(mEnterOtp.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter Your Otp",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    mprogressBarForAuth.setVisibility(View.VISIBLE);
                    String CodeRecieved=getIntent().getStringExtra("otp");
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeRecieved,mEnterOtp);
                    SignWithPhoneAuthCreaddential(credential);
                }
            }
        });
    }

    private void SignWithPhoneAuthCreaddential(PhoneAuthCredential credential)
    {
        mFireBaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    mprogressBarForAuth.setVisibility(View.INVISIBLE);
                    //Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(OtpAuthentication.this,SetProfile.class);
                    Intent i;
                    i=getIntent();
                    String ii= i.getStringExtra("number");
                    intent.putExtra("number",ii);
                    startActivity(intent);
                    finish();
                }
                else
                {
                 if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                 {
                     mprogressBarForAuth.setVisibility(View.INVISIBLE);
                     Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                 }
                }
            }
        });
    }
}