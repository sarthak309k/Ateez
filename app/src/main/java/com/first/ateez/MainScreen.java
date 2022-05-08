package com.first.ateez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.first.ateez.Activities.Login;
import com.first.ateez.Activities.MainActivity;
import com.first.ateez.Activities.chat_activity;
import com.first.ateez.Activities.todohome;
import com.first.ateez.databinding.ActivityMainScreenBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainScreen extends AppCompatActivity {

    ActivityMainScreenBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.constraint.setOnTouchListener(new OnSwipeTouchListener(MainScreen.this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                    Intent intent = new Intent(MainScreen.this, chat_activity.class);
                    startActivity(intent);
                    Toast.makeText(MainScreen.this, "Swipe Left gesture detected", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(MainScreen.this, todohome.class);
                    startActivity(intent);
                    Toast.makeText(MainScreen.this, "Swipe Left gesture detected", Toast.LENGTH_SHORT).show();
                }
                }

        });



    }
}