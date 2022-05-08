package com.first.ateez.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.first.ateez.R;
import com.first.ateez.databinding.ActivityOtpverifyBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class otpverify extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://ateez-9948d-default-rtdb.firebaseio.com/");
    ActivityOtpverifyBinding binding;
    FirebaseAuth auth;
    String backendotp;
    String phoneNumber;
    Button verifyotpbtn;
    EditText otpbox1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityOtpverifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.otpbox1.requestFocus();
        auth = FirebaseAuth.getInstance();
        phoneNumber = getIntent().getStringExtra("phonenbox");
        binding.phoneLable.setText("Verify +91" + phoneNumber);
        otpbox1=findViewById(R.id.otpbox1);
        verifyotpbtn = (Button) findViewById(R.id.otpverifybtn);


        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91" + phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(otpverify.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        binding.verifyotpprg.setVisibility(View.VISIBLE);
                        binding.otpverifybtn.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        binding.otpverifybtn.setVisibility(View.VISIBLE);
                        binding.verifyotpprg.setVisibility(View.INVISIBLE);
                        Toast.makeText(otpverify.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verify, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verify, forceResendingToken);
                        backendotp = verify;
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        requestPermission();
        new Otpreceive().setSetotp(otpbox1);
        binding.otpverifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  if (!otpbox1.getText().toString().trim().isEmpty()) {
                    //String otpentered = binding.otpbox1.getText().toString();



                        binding.verifyotpprg.setVisibility(View.VISIBLE);
                        binding.otpverifybtn.setVisibility(View.INVISIBLE);
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(backendotp, binding.otpbox1.getText().toString());

                        auth.signInWithCredential(phoneAuthCredential)
                                .addOnCompleteListener(task -> {
                                    binding.verifyotpprg.setVisibility(View.GONE);
                                    binding.otpverifybtn.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(otpverify.this, "Loggedin", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), Signup.class);
                                intent.putExtra("phoneNumber","91+"+phoneNumber);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                                    }
                                    else {
                                        Toast.makeText(otpverify.this, "please enter all numbers", Toast.LENGTH_SHORT).show();
                                    }
                                });

                //} else {
                  //  Toast.makeText(otpverify.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                //}
            }});
        }

    private void requestPermission() {
        if(ContextCompat.checkSelfPermission(otpverify.this,Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(otpverify.this,new String[]{
                    Manifest.permission.RECEIVE_SMS
            },100);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_ENTER:
                binding.otpbox1.requestFocus();
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}





