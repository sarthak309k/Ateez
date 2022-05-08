package com.first.ateez.Activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.lang.UCharacter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.first.ateez.Adapters.MessagesAdapter;
import com.first.ateez.Models.message;
import com.first.ateez.R;
import com.first.ateez.databinding.ActivityChatSingleBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class chatSingle extends AppCompatActivity {
    ActivityChatSingleBinding binding;
   ActivityResultLauncher<String> pickimage;
    MessagesAdapter adapter;
    ArrayList<message> messages;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;


    String senderRoom,receiverRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatSingleBinding.inflate(getLayoutInflater());
        auth=FirebaseAuth.getInstance();
        setContentView(binding.getRoot());

        messages=new ArrayList<>();
        String name=getIntent().getStringExtra("name");

        String receiveruid=getIntent().getStringExtra("uid");
        String senderuid = auth.getUid();

        senderRoom = senderuid +receiveruid;
        receiverRoom=receiveruid + senderuid;
        adapter=new MessagesAdapter(this,messages,senderRoom,receiverRoom);

        binding.recyclerviewmsg.setAdapter(adapter);

        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        database.getReference().child("chats").child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    message msg=snapshot1.getValue(message.class);
                    msg.setMsgId(snapshot1.getKey());
                    messages.add(msg);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageTxt=binding.messageBox.getText().toString();
                binding.messageBox.setText("");
                Date date=new Date();
                String randomkey=database.getReference().push().getKey();
                message message=new message(messageTxt, senderuid , date.getTime());
                HashMap<String,Object>lastmsgobj = new HashMap<>();
                lastmsgobj.put("lastMsg",message.getMessage());
                lastmsgobj.put("lastMsgTime",date.getTime());
                database.getReference().child("chats").child(senderRoom).updateChildren(lastmsgobj);
                database.getReference().child("chats").child(receiverRoom).updateChildren(lastmsgobj);

                database.getReference().child("chats").child(senderRoom).child("messages").child(randomkey).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("chats").child(receiverRoom).child("messages").child(randomkey).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }

                        });
                    }
                });
            }
        });
        pickimage=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                 if(result!=null)
                 {
                     Uri getdat=result;
                     Calendar calendar=Calendar.getInstance();
                     StorageReference reference=storage.getReference().child("chats").child(calendar.getTimeInMillis()+"");
                     reference.putFile(getdat).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                         @Override
                         public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                             if(task.isSuccessful())
                             {
                                 reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                     @Override
                                     public void onSuccess(Uri uri) {
                                         String filepath=uri.toString();
                                         String messageTxt=binding.messageBox.getText().toString();
                                         binding.messageBox.setText("");
                                         Date date=new Date();
                                         String randomkey=database.getReference().push().getKey();
                                         message message=new message(messageTxt, senderuid , date.getTime());
                                         message.setImageUrl(filepath);
                                         message.setMessage("photo");
                                         HashMap<String,Object>lastmsgobj = new HashMap<>();
                                         lastmsgobj.put("lastMsg",message.getMessage());
                                         lastmsgobj.put("lastMsgTime",date.getTime());
                                         database.getReference().child("chats").child(senderRoom).updateChildren(lastmsgobj);
                                         database.getReference().child("chats").child(receiverRoom).updateChildren(lastmsgobj);

                                         database.getReference().child("chats").child(senderRoom).child("messages").child(randomkey).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void unused) {
                                                 database.getReference().child("chats").child(receiverRoom).child("messages").child(randomkey).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                     @Override
                                                     public void onSuccess(Void unused) {

                                                     }

                                                 });
                                             }
                                         });
                                     }
                                 });
                             }
                         }
                     });
                 }
            }
        });

        binding.attachment.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                pickimage.launch("*/*");

            }
        });


        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}