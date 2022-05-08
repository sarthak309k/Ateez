package com.first.ateez.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.first.ateez.Models.message;
import com.first.ateez.R;
import com.first.ateez.databinding.ReceivemsgBinding;
import com.first.ateez.databinding.SendmsgBinding;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter{
    FirebaseUser auth;
    Context context;
    ArrayList <message> messages;
    final int ITEM_SENT= 1;
    final int ITEM_RECEIVE= 2 ;
    String senderRoom;
    String receiverRoom;

    public MessagesAdapter(Context context, ArrayList<message> messages,String senderRoom,String receiverRoom) {
       this.context=context;
       this.messages=messages;
       this.senderRoom=senderRoom;
       this.receiverRoom=receiverRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SENT){
            View view= LayoutInflater.from(context).inflate(R.layout.sendmsg,parent,false);
            return new sentViewHolder(view);
        }
        else{
            View view=LayoutInflater.from(context).inflate(R.layout.receivemsg,parent,false);
            return new reciverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        message msg= messages.get(position);
        auth=FirebaseAuth.getInstance().getCurrentUser();
        if(auth.getUid().equals(msg.getSenderid())){
            return ITEM_SENT;
        }
        else{
            return ITEM_RECEIVE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        message msg=messages.get(position);
        int reactions[]=new int[]{
            R.drawable.hearteyes,
                    R.drawable.love,
                    R.drawable.laugh,
                    R.drawable.fire,
                    R.drawable.sad,
                    R.drawable.sadcry,
                    R.drawable.angry
        };
        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();
        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if (pos<0){
                return false;
            }
            if(holder.getClass()==sentViewHolder.class){
                sentViewHolder viewHolder=(sentViewHolder)holder;
                viewHolder.binding.feelings.setImageResource(reactions[pos]);
                viewHolder.binding.feelings.setVisibility(View.VISIBLE);
            }else{
                reciverViewHolder viewHolder=(reciverViewHolder) holder;
                viewHolder.binding.feelingsrec.setImageResource(reactions[pos]);
                viewHolder.binding.feelingsrec.setVisibility(View.VISIBLE);

            }
            msg.setFeeling(pos);
            FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages").child(msg.getMsgId()).setValue(msg);
            FirebaseDatabase.getInstance().getReference().child("chats").child(receiverRoom).child("messages").child(msg.getMsgId()).setValue(msg);
            return true; // true is closing popup, false is requesting a new selection
        });



        if(holder.getClass()==sentViewHolder.class){

            sentViewHolder viewHolder= (sentViewHolder)holder;

            if(msg.getMessage().equals("photo"))
            {
                viewHolder.binding.chatimage.setVisibility(View.VISIBLE);
                viewHolder.binding.messagesend.setVisibility(View.GONE);
                Glide.with(context).load(msg.getImageUrl()).placeholder(R.drawable.placeholder).into(viewHolder.binding.chatimage);
            }
            viewHolder.binding.messagesend.setText(msg.getMessage());
            if(msg.getFeeling()>=0){
                //msg.setFeeling(reactions[(int) msg.getFeeling()]);
                viewHolder.binding.feelings.setImageResource(reactions[msg.getFeeling()]);
                viewHolder.binding.feelings.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.binding.feelings.setVisibility(View.GONE);
            }
            viewHolder.binding.messagesend.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    popup.onTouch(view,motionEvent);
                    return true;
                }
            });
        }
        else {

            reciverViewHolder viewHolder=(reciverViewHolder)holder;

            if(msg.getMessage().equals("photo"))
            {
                viewHolder.binding.recchatimage.setVisibility(View.VISIBLE);
                viewHolder.binding.messagerec.setVisibility(View.GONE);
                Glide.with(context).load(msg.getImageUrl()).placeholder(R.drawable.placeholder).into(viewHolder.binding.recchatimage);
            }
            viewHolder.binding.messagerec.setText(msg.getMessage());
            if(msg.getFeeling()>=0){
                //msg.setFeeling(reactions[(int) msg.getFeeling()]);
                viewHolder.binding.feelingsrec.setImageResource(reactions[msg.getFeeling()]);
                viewHolder.binding.feelingsrec.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.binding.feelingsrec.setVisibility(View.GONE);
            }
            viewHolder.binding.messagerec.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    popup.onTouch(view,motionEvent);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class sentViewHolder extends RecyclerView.ViewHolder{
        SendmsgBinding binding;
        public sentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=SendmsgBinding.bind(itemView);
        }
    }
    public class reciverViewHolder extends RecyclerView.ViewHolder{
        ReceivemsgBinding binding;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ReceivemsgBinding.bind(itemView);
        }
    }
}
