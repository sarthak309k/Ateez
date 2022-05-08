package com.first.ateez.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.first.ateez.Activities.chatSingle;
import com.first.ateez.Models.User;
import com.first.ateez.R;
import com.first.ateez.databinding.ConversationMenuBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.okhttp.internal.DiskLruCache;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserAdaptar extends RecyclerView.Adapter<UserAdaptar.UsersViewHolder> {
   Context context;
    ArrayList<User> users;
    public UserAdaptar(Context context, ArrayList<User> users){
           this.context=context;
           this.users=users;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.conversation_menu,parent,false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
     User user=users.get(position);
     String senderId= FirebaseAuth.getInstance().getUid();
     String senderRoom=senderId+user.getUid();

        FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                    long time;
                    time = snapshot.child("lastMsgTime").getValue(Long.class);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat=new SimpleDateFormat("hh:mm a");
                    holder.binding.msgTime.setText(dateFormat.format(new Date(time)));

                    holder.binding.lastMsg.setText(lastMsg);
                }else{
                    holder.binding.lastMsg.setText("Tap to chat");
                }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

     holder.binding.usernamedisp.setText(user.getName());
     //holder.binding.profiledisp.setImageResource();
     Glide.with(context).load(user.getImageProfile())
             .placeholder(R.drawable.ic_baseline_account_circle_24)
             .into(holder.binding.profiledisp);

     holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent=new Intent(context, chatSingle.class);
             intent.putExtra("name",user.getName());
             intent.putExtra("uid",user.getUid());
             context.startActivity(intent);
         }
     });


      
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder{
        ConversationMenuBinding binding;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ConversationMenuBinding.bind(itemView);
        }
    }
}


