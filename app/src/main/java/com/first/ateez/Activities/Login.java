package com.first.ateez.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.first.ateez.Models.User;
import com.first.ateez.R;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.internal.FirebaseCustomAuthToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.util.ArrayList;

public class Login extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://ateez-9948d-default-rtdb.firebaseio.com/");
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<User> users;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        users=new ArrayList<>();

        final EditText username;
        final EditText password;
        final Button login;
        final Button signup ;
        username = findViewById(R.id.username);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            final String usernameTxt=username.getText().toString();
            final String passwordTxt=password.getText().toString();
            //String phonenumber=auth.getCurrentUser().getPhoneNumber();
            if(usernameTxt.isEmpty() || passwordTxt.isEmpty())
            {
                Toast.makeText(Login.this, "Please entre username or password", Toast.LENGTH_SHORT).show();
            }
            else{


                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(usernameTxt)) {

                                final String getPassword = snapshot.child(usernameTxt).child("password").getValue(String.class);

                                String hashpass = sha256(passwordTxt);
                                if (getPassword.equals(hashpass)) {
                                    startActivity(new Intent(Login.this, chat_activity.class));
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Wrong Paassword", Toast.LENGTH_SHORT).show();
                                }
                            }
                                     else{
                                        Toast.makeText(Login.this, "Wrong Mobile Number", Toast.LENGTH_SHORT).show();
                                    }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

            }


        }

        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, mobilenumber.class));
            }
        });
    }



    String base;
    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }








}