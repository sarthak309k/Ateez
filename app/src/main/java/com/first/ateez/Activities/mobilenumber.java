package com.first.ateez.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.first.ateez.databinding.ActivityMobilenumberBinding;
import com.google.firebase.auth.FirebaseAuth;

public class mobilenumber extends AppCompatActivity {
    ActivityMobilenumberBinding binding;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMobilenumberBinding.inflate(getLayoutInflater());
        binding.phonebox.requestFocus();
        auth=FirebaseAuth.getInstance();
        setContentView(binding.getRoot());

       /* if(auth.getCurrentUser()!=null)
        {
            Intent intent=new Intent(mobilenumber.this,chat_activity.class);
            startActivity(intent);
        }*/

        binding.sendotpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.phonebox.getText().toString().trim().isEmpty()) {
                   if((binding.phonebox.getText().toString().trim()).length()==10) {
                       binding.sendotpprg.setVisibility(View.VISIBLE);
                       binding.sendotpbtn.setVisibility(View.INVISIBLE);
                       Intent intent = new Intent(mobilenumber.this, otpverify.class);
                       intent.putExtra("phonenbox", binding.phonebox.getText().toString());
                       startActivity(intent);
                      /* PhoneAuthOptions options=PhoneAuthOptions.newBuilder(auth)
                               .setPhoneNumber("+91"+binding.phonebox.getText().toString())
                               .setTimeout(60L, TimeUnit.SECONDS)
                               .setActivity(mobilenumber.this)
                               .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                   @Override
                                   public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                       binding.sendotpprg.setVisibility(view.VISIBLE);
                                       binding.sendotpbtn.setVisibility(view.INVISIBLE);
                                   }

                                   @Override
                                   public void onVerificationFailed(@NonNull FirebaseException e) {
                                       binding.sendotpprg.setVisibility(view.VISIBLE);
                                       binding.sendotpbtn.setVisibility(view.INVISIBLE);
                                       Toast.makeText(mobilenumber.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                   }

                                   @Override
                                   public void onCodeSent(@NonNull String verify, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                       super.onCodeSent(verify, forceResendingToken);
                                       Intent intent = new Intent(mobilenumber.this, otpverify.class);
                                       intent.putExtra("phonenbox", binding.phonebox.getText().toString());
                                       intent.putExtra("verify",verify);
                                       startActivity(intent);
                                   }
                               }).build();
                       PhoneAuthProvider.verifyPhoneNumber(options);*/

                   }
                   else{
                       Toast.makeText(mobilenumber.this, "Enter correct mobile number", Toast.LENGTH_SHORT).show();
                   }
                }
                else
                {
                    Toast.makeText(mobilenumber.this, "Enter mobile number", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}