package com.first.ateez.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.first.ateez.R;
import com.first.ateez.Models.User;
import com.first.ateez.Adapters.UserAdaptar;
import com.first.ateez.databinding.ActivityChatBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chat_activity extends AppCompatActivity {
    ActivityChatBinding binding;
    FirebaseDatabase database;
    ArrayList<User>users;
    UserAdaptar userAdaptar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database=FirebaseDatabase.getInstance();
        users=new ArrayList<>();
        userAdaptar=new UserAdaptar(this,users);

        selectmenu();

        binding.chatmenuview.setAdapter(userAdaptar);
        binding.chatmenuview.showShimmerAdapter();

        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    User user=snapshot1.getValue(User.class);
                    if(!user.getPhoneNumber().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())) {
                        users.add(user);
                    }
                    }
                binding.chatmenuview.hideShimmerAdapter();
                userAdaptar.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void selectmenu() {
        binding.bottomNavigationView2.setSelectedItemId(R.id.chats);
        binding.bottomNavigationView2.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch(id){

                    case R.id.story:
                        Toast.makeText(chat_activity.this, "story", Toast.LENGTH_SHORT).show();
                        Intent intent1=new Intent(chat_activity.this, Statusstory.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent1);
                        break;
/*
                    case R.id.groupchats:
                        Toast.makeText(chat_activity.this, "story", Toast.LENGTH_SHORT).show();
                        Intent intent2=new Intent(chat_activity.this,groupchats.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent2);
                        break;

                    case R.id.camera:
                        Toast.makeText(chat_activity.this, "story", Toast.LENGTH_SHORT).show();
                        Intent intent3=new Intent(chat_activity.this,camera.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent3);
                        break;

                    case R.id.calls:
                        Toast.makeText(chat_activity.this, "story", Toast.LENGTH_SHORT).show();
                        Intent intent4=new Intent(chat_activity.this,calls.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent4);
                        break;*/

                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.About:
                Toast.makeText(this, "About Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.NewGroups:
                Toast.makeText(this, "New Groups Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Settings:
                Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Invite:
                Toast.makeText(this, "Invite Clicked", Toast.LENGTH_SHORT).show();

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(chat_activity.this,Login.class);
                startActivity(intent);
                break;


        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topchatmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }



}