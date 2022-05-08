package com.first.ateez.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.first.ateez.Models.Task;
import com.first.ateez.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class createatask extends AppCompatActivity {
    String selecteddate;
    String pickedtime;
    int year;
    int month;
    int dayofmonth;
    int hour;
    int minute;
    int seconds;
    Calendar cal = Calendar.getInstance();
    DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener date;
    long min = 1;
    long max = 999999999;
    TimePickerDialog timePickerDialog;
    EditText tasktitle;
    EditText taskDescription;
    EditText setdate;
    EditText settime;
    EditText event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createatask);
        Button save=(Button) findViewById(R.id.createTask);

        tasktitle = (EditText) findViewById(R.id.addTaskTitle);
        taskDescription = (EditText) findViewById(R.id.addTaskDescription);
        setdate = (EditText) findViewById(R.id.taskDate);
        settime = (EditText) findViewById(R.id.taskTime);
        event = (EditText) findViewById(R.id.taskEvent);

        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour=cal.get(Calendar.HOUR_OF_DAY);
                minute=cal.get(Calendar.MINUTE);
                seconds=cal.get(Calendar.MILLISECOND);
                timePickerDialog=new TimePickerDialog(createatask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourofday, int minute) {
                        pickedtime=hourofday+":"+minute;
                        settime.setText(hourofday+":"+minute);
                    }
                },hour,minute,true);
           timePickerDialog.setTitle("Choose Time");
           timePickerDialog.show();
            }

        });


        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                year=cal.get(Calendar.YEAR);
                month=cal.get(Calendar.MONTH);
                dayofmonth=cal.get(Calendar.DAY_OF_MONTH);
                //Toast.makeText(createatask.this, "Pick a Date", Toast.LENGTH_SHORT).show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(createatask.this,date,year,month,dayofmonth);
                datePickerDialog.show();
            }
        });

        date=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                selecteddate=day+"/"+month+"/"+year;
                setdate.setText(selecteddate);
            }
        };




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTodo();
            }
        });

    }

    public void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        selecteddate=(sdf.format(cal.getTime()));
    }


    @SuppressLint("ClickableViewAccessibility")
    private void saveTodo() {

        int b = (int)(Math.random()*(max-min+1)+min);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        String key=firebaseDatabase.getReference("TodoList").push().getKey();
        Task task=new Task();
        task.setTaskTitle(tasktitle.getText().toString());
        task.setTaskId(b);
        task.setTaskDescription(taskDescription.getText().toString());
        task.setDate(selecteddate);
        task.setFirstAlarmTime(pickedtime);
        task.setEvent(event.getText().toString());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put( key, task.toFirebaseObject());
        firebaseDatabase.getReference("todoList").updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    finish();
                }
            }
        });




    }


}