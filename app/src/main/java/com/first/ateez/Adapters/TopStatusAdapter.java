package com.first.ateez.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.first.ateez.Activities.Statusstory;
import com.first.ateez.Models.Status;
import com.first.ateez.Models.UserStatus;
import com.first.ateez.R;
import com.first.ateez.databinding.ItemStatusBinding;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class TopStatusAdapter extends RecyclerView.Adapter<TopStatusAdapter.TopStatusViewholder> {
    Context context;
    ArrayList<UserStatus> userStatuses;

    public TopStatusAdapter(Context context,ArrayList<UserStatus> userStatuses)
    {
        this.context=context;
        this.userStatuses=userStatuses;
    }

    @NonNull
    @Override
    public TopStatusViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_status,parent,false);
        return new TopStatusViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopStatusViewholder holder, int position) {
        UserStatus userStatus=userStatuses.get(position);
        Status laststatus=userStatus.getStatuses().get(userStatus.getStatuses().size()-1);

        Glide.with(context).load(laststatus.getImgurl()).into(holder.binding.circlestatusimage);
        holder.binding.circularStatusView.setPortionsCount(userStatus.getStatuses().size());

     holder.binding.circularStatusView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             ArrayList<MyStory> myStories = new ArrayList<>();
             for(Status status:userStatus.getStatuses())
             {
                 myStories.add(new MyStory(status.getImgurl()));
             }
             new StoryView.Builder(((Statusstory)context).getSupportFragmentManager())
                     .setStoriesList(myStories) // Required
                     .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                     .setTitleText(userStatus.getName()) // Default is Hidden
                     .setSubtitleText("") // Default is Hidden
                     .setTitleLogoUrl(userStatus.getProfileimage()) // Default is Hidden
                     .setStoryClickListeners(new StoryClickListeners() {
                         @Override
                         public void onDescriptionClickListener(int position) {
                             //your action
                         }

                         @Override
                         public void onTitleIconClickListener(int position) {
                             //your action
                         }
                     }) // Optional Listeners
                     .build() // Must be called before calling show method
                     .show();
         }
     });
    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }

    public class TopStatusViewholder extends RecyclerView.ViewHolder{
       ItemStatusBinding binding;
        public TopStatusViewholder(@NonNull View itemView) {
            super(itemView);
            binding=ItemStatusBinding.bind(itemView);
        }
    }
}
