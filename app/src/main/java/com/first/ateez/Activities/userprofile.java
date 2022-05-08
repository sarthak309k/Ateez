package com.first.ateez.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.first.ateez.Models.User;
import com.first.ateez.databinding.ActivityUserprofileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class userprofile extends AppCompatActivity {
   ActivityUserprofileBinding binding;
   FirebaseAuth auth;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://ateez-9948d-default-rtdb.firebaseio.com/");
   FirebaseDatabase database;
   FirebaseStorage storage;
   FirebaseUser firebaseUser;
   Uri imageuri;
    ActivityResultLauncher activityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserprofileBinding.inflate(getLayoutInflater());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String phoneNumber=firebaseUser.getPhoneNumber();
        setContentView(binding.getRoot());


  binding.avatar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
            Intent intent=new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            gallaryActivityResultLauncher.launch(intent);
      }
  });

  binding.upload.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          if(imageuri!= null){
              StorageReference reference=storage.getReference().child("Profiles").child(auth.getCurrentUser().getPhoneNumber()).child(auth.getUid());
              reference.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                      reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                          @Override
                          public void onSuccess(Uri uri) {
                              String imgurl=uri.toString();
                              User user=new User(imgurl);
                              databaseReference.child("users").child(phoneNumber).child("imageProfile").setValue(imgurl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                  @Override
                                  public void onSuccess(Void unused) {
                                      Intent intent=new Intent(userprofile.this, chat_activity.class);
                                      startActivity(intent);
                                      finish();
                                  }

                              });
                          }
                      });
                  }
              });
          }
          else{

              User user=new User("null");
              databaseReference.child("users").child(phoneNumber).child("imageProfile").setValue("null").addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void unused) {
                      Intent intent = new Intent(userprofile.this, chat_activity.class);
                      startActivity(intent);
                      finish();

                  }
              });
          }
      }
  });



    }

  ActivityResultLauncher<Intent> gallaryActivityResultLauncher =registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
          new ActivityResultCallback<ActivityResult>() {
              @Override
              public void onActivityResult(ActivityResult result) {
                  if(result.getResultCode() == Activity.RESULT_OK){
                      Intent data=result.getData();
                      imageuri=data.getData();
                      binding.avatar.setImageURI(imageuri);
                  }
                  else{

                  }
              }
          });

}