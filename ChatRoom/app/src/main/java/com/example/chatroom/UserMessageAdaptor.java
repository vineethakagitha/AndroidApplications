package com.example.chatroom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

public class UserMessageAdaptor extends RecyclerView.Adapter<UserMessageAdaptor.ViewHolder>
{
    ArrayList<UserMessage> messages;
    View view;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("userMessages");
    ConstraintLayout msglayout;

    public UserMessageAdaptor(ArrayList<UserMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_layout, viewGroup, false);
        msglayout = view.findViewById(R.id.messageLayout);
        ViewHolder viewHolder =  new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

//        TextView txtMsg;
//        TextView fromUser;
//        TextView time;
//        ImageView image;
//        ImageButton del;

        final UserMessage um = messages.get(i);
        User user = um.getUser();
        Message msg = um.getMessage();
        viewHolder.txtMsg.setText(msg.text);
        viewHolder.fromUser.setText(user.firstName);
        PrettyTime p = new PrettyTime();
        viewHolder.time.setText(p.format(um.getDate()));
        //Log.d("demo","Image for "+i+" :"+msg.getImage() );
        if(msg.getImage() != null)
        {

            Picasso.get().load(msg.getImage())
                    .resize(171,128)
                    .into(viewHolder.image);
        }
        else{
            //collapse imageview
            Log.d("demo","No Image");
            viewHolder.image.setVisibility(View.GONE);
           // viewHolder.image.setMaxWidth(1);
          //  viewHolder.image.setMaxHeight(1);
//            if(msglayout!=null)
//            {
//                Log.d("demo","fhfb");
//                ViewGroup vg = (ViewGroup) msglayout;
//                vg.removeView(viewHolder.image);
//            }

           // viewHolder.image.setVisibility(View.GONE);
        }
        viewHolder.del.setImageResource(R.drawable.delete);
        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete
                myRef.child(um.getKey()).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
         return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtMsg;
        TextView fromUser;
        TextView time;
        ImageView image;
        ImageButton del;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMsg = (TextView) itemView.findViewById(R.id.textMsg);
            fromUser = (TextView) itemView.findViewById(R.id.fromUser);
            time = (TextView) itemView.findViewById(R.id.prettyTime);
            image = (ImageView) itemView.findViewById(R.id.msgImage);
            del = (ImageButton)itemView.findViewById(R.id.deleteBtn);
        }
    }

}
