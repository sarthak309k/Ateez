package com.first.ateez.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.first.ateez.Adapters.TopStatusAdapter;
import com.first.ateez.Models.Status;
import com.first.ateez.Models.User;
import com.first.ateez.Models.UserStatus;
import com.first.ateez.R;
import com.first.ateez.databinding.ActivityStatusstoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Statusstory extends AppCompatActivity {
    ActivityStatusstoryBinding binding;
    TopStatusAdapter statusAdapter;
    ArrayList<UserStatus>userStatuses;
    ActivityResultLauncher<String>statuses;
    FirebaseDatabase database;
    ArrayList<User>users;
    AlertDialog alertDialog;
    User user;
    String phonennuber;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://ateez-9948d-default-rtdb.firebaseio.com/");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityStatusstoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        selectmenu();
        userStatuses=new ArrayList<>();
        database=FirebaseDatabase.getInstance();
        phonennuber= FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.statuslist.setLayoutManager(linearLayoutManager);

        statusAdapter=new TopStatusAdapter(this,userStatuses);

        binding.statuslist.setAdapter(statusAdapter);
        binding.statuslist.showShimmerAdapter();
        database.getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       user=snapshot.getValue(User.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        database.getReference().child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    userStatuses.clear();
                    for(DataSnapshot storySnapshot:snapshot.getChildren()){
                        UserStatus status=new UserStatus();
                        status.setName(storySnapshot.child("name").getValue(String.class));
                        status.setProfileimage(storySnapshot.child("profileimage").getValue(String.class));
                        status.setLastupdated(storySnapshot.child("lastupdated").getValue(Long.class));
                        ArrayList<Status>statuses =new ArrayList<>();

                        for(DataSnapshot statusSnapshot:storySnapshot.child("statuses").getChildren())
                        {
                            Status sampleStatus=statusSnapshot.getValue(Status.class);
                            statuses.add(sampleStatus);
                        }
                        status.setStatuses(statuses);

                        userStatuses.add(status);
                    }
                    binding.statuslist.hideShimmerAdapter();
                    statusAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        statuses=registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                     if(result!=null){
                         binding.progressBar2.setVisibility(View.VISIBLE);
                         Toast.makeText(Statusstory.this, "Loading", Toast.LENGTH_LONG).show();
                         FirebaseStorage storage=FirebaseStorage.getInstance();
                         Date date=new Date();
                         StorageReference reference=storage.getReference().child("status").child(phonennuber).child(date.getTime()+"");
                         reference.putFile(result).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                             @Override
                             public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                 reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                     @Override
                                     public void onSuccess(Uri uri) {
                                       try {
                                           UserStatus userStatus = new UserStatus();
                                           userStatus.setName(user.getName());
                                           userStatus.setProfileimage(user.getImageProfile());
                                           userStatus.setLastupdated(date.getTime());

                                           HashMap<String, Object> obj = new HashMap<>();
                                           obj.put("name", userStatus.getName());
                                           obj.put("profileimage", userStatus.getProfileimage());
                                           obj.put("lastupdated", userStatus.getLastupdated());

                                           String imageUrl = uri.toString();
                                           Status status = new Status(imageUrl, userStatus.getLastupdated());


                                           database.getReference().child("status").child(FirebaseAuth.getInstance().getUid()).updateChildren(obj);
                                           database.getReference().child("status").child(FirebaseAuth.getInstance().getUid()).child("statuses").push().setValue(status);
                                           binding.progressBar2.setVisibility(View.GONE);
                                           Toast.makeText(Statusstory.this, "Completed", Toast.LENGTH_LONG).show();
                                       }
                                       catch (NullPointerException e)
                                       {
                                           e.printStackTrace();
                                       }
                                     }
                                 });
                             }
                         });
                     }
                    }
                }
        );

        binding.addstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statuses.launch("image/*,videos/*");
            }
        });



    }



    private void selectmenu() {
        binding.bottom3.setSelectedItemId(R.id.story);
        binding.bottom3.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch(id){

                    case R.id.chats:
                        Toast.makeText(Statusstory.this, "chats", Toast.LENGTH_SHORT).show();
                        Intent intent1=new Intent(Statusstory.this,chat_activity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        break;

                }

                return true;
            }
        });
    }


}