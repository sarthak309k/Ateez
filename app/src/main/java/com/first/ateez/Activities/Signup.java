package com.first.ateez.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.first.ateez.R;
import com.first.ateez.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.security.MessageDigest;

public class Signup extends AppCompatActivity {
     DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://ateez-9948d-default-rtdb.firebaseio.com/");
     FirebaseAuth auth;
     FirebaseDatabase database;
     FirebaseStorage storage;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_signup);
            final EditText firstname=findViewById(R.id.firstname);
            final EditText Surname=findViewById(R.id.Surname);
            final EditText usrname=findViewById(R.id.usrname);
            final EditText password1=findViewById(R.id.password1);
            final EditText conpassword=findViewById(R.id.password2);
            final Button register= findViewById(R.id.register);
            final TextView loginnow=findViewById(R.id.loginnow);
            final EditText emailid=findViewById(R.id.emailid);

            auth=FirebaseAuth.getInstance();
      register.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
               final String space=" ";
               final String firstnameTxt=firstname.getText().toString();
               final String SurnameTxt=Surname.getText().toString();
               final String password1Txt=password1.getText().toString();
               final String conpasswordTxt=conpassword.getText().toString();
               final String usernameTxt=usrname.getText().toString();
               final String emailIdTxt=emailid.getText().toString();
               final String phoneNumber=getIntent().getStringExtra("phoneNumber");
               if(emailIdTxt.isEmpty() || firstnameTxt.isEmpty() || SurnameTxt.isEmpty() || usernameTxt.isEmpty() ||  password1Txt.isEmpty() || conpasswordTxt.isEmpty())
               {
                    Toast.makeText(Signup.this, "Unable to submit check all fields are filled", Toast.LENGTH_SHORT).show();
               }
               else if(!password1Txt.equals(conpasswordTxt))
               {
                    Toast.makeText(Signup.this, "Password is not matching", Toast.LENGTH_LONG).show();
               }
               else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                              if(snapshot.hasChild(usernameTxt))
                              {
                                   Toast.makeText(Signup.this, "Contact is already registered", Toast.LENGTH_LONG).show();
                              }
                              else{

                                   String name=firstnameTxt+space+SurnameTxt;
                                   String emailid=sha256(emailIdTxt);
                                   String phone=auth.getCurrentUser().getPhoneNumber();
                                   String uid=auth.getUid();
                                   String password=sha256(password1Txt);
                                   String username=usernameTxt;
                                   String imageProfile="null";
                                   User user=new User(uid, name, username, phone, emailid,password,imageProfile);
                                   databaseReference.child("users").child(phone).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                             Intent intent=new Intent(Signup.this, userprofile.class);
                                             startActivity(intent);
                                        }
                                   });
                                   /*
                                   databaseReference.child("users").child(usernameTxt).child("fullname").setValue(firstnameTxt+space+SurnameTxt);
                                   databaseReference.child("users").child(usernameTxt).child("Password").setValue(password1Txt);
                                   databaseReference.child("users").child(usernameTxt).child("Emailid").setValue(emailIdTxt);*/
                                   Toast.makeText(Signup.this, "Registration Completed Succesfully ", Toast.LENGTH_LONG).show();
                                   Intent intent=new Intent(Signup.this,userprofile.class);
                                   startActivity(intent);
                                   finish();

                              }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                    });

               }

          }
     });



     loginnow.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
               finish();
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