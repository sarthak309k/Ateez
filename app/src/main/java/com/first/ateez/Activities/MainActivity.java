package com.first.ateez.Activities;
import java.io.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


import com.first.ateez.Dashboard;
import com.first.ateez.Home;
import com.first.ateez.Notification;
import com.first.ateez.R;
import com.first.ateez.quotes;
import com.first.ateez.videos;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,new Home());
        transaction.commit();

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                switch (id){
                    case R.id.Dashboard:
                        transaction.replace(R.id.container,new Dashboard());

                        break;

                    case R.id.Home:
                        transaction.replace(R.id.container,new Home());
                        break;

                    case R.id.Notifications:
                        transaction.replace(R.id.container,new Notification());
                        break;

                    case R.id.videos:
                        transaction.replace(R.id.container,new videos());
                        break;

                    case R.id.Quotes:
                        transaction.replace(R.id.container,new quotes());
                        break;


                }
                transaction.commit();
                return true;
            }
        });


    }
}