package com.first.ateez.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.first.ateez.databinding.ActivityTodohomeBinding;

public class todohome extends AppCompatActivity {
    ActivityTodohomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTodohomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.createTask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
               Intent intent=new Intent(todohome.this,createatask.class);
               startActivity(intent);
                return true;
            }
        });
    }

}