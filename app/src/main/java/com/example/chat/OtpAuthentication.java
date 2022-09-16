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

    TextView MChangeNumber;
    EditText MGetOtp;
    android.widget.Button MverifyOtp;
    String EnterOtp;
    FirebaseAuth FireBaseAuth;
    ProgressBar MprogressBarForAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otp_authentication);
        MChangeNumber=findViewById(R.id.changeNumbear);
        MverifyOtp=findViewById(R.id.verifyOtp);
        MGetOtp=findViewById(R.id.getOtp);
        MprogressBarForAuth=findViewById(R.id.profressbarOfOtpAuth);

        FireBaseAuth=FirebaseAuth.getInstance();
        MChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OtpAuthentication.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        MverifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnterOtp=MGetOtp.getText().toString();
                if(EnterOtp.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter Your Otp",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    MprogressBarForAuth.setVisibility(View.VISIBLE);
                    String CodeRecieved=getIntent().getStringExtra("otp");
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeRecieved,EnterOtp);
                    SignWithPhoneAuthCreaddential(credential);
                }
            }
        });
    }

    private void SignWithPhoneAuthCreaddential(PhoneAuthCredential credential)
    {
        FireBaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    MprogressBarForAuth.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
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
                     MprogressBarForAuth.setVisibility(View.INVISIBLE);
                     Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                 }
                }
            }
        });
    }
}