package com.example.inclass09;

import android.support.annotation.NonNull;
import android.support.constraint.Placeholder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ContactAdaptor extends RecyclerView.Adapter<ContactAdaptor.ViewHolder> {
    User user;
    ArrayList<Contact> contacts;
    View view;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("usercontacts");


    public ContactAdaptor(ArrayList<Contact> contacts, User user){
        this.contacts = contacts;
        this.user = user;
    }

    @NonNull
    @Override
    public ContactAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact, viewGroup, false);
        ContactAdaptor.ViewHolder viewHolder =  new ContactAdaptor.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdaptor.ViewHolder viewHolder, final int i) {

        Contact c = contacts.get(i);
        viewHolder.fullName.setText(c.name);
        viewHolder.email.setText(c.email);
        viewHolder.phno.setText(c.phone);

        if(c.picture!=null && !c.picture.equals("")) {
            Picasso.get().load(c.picture)
                         .into(viewHolder.image);

        }else{
            viewHolder.image.setImageResource(R.mipmap.contact_image_foreground);
        }


        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myRef = myRef.child(user.key+"/Contacts/"+contacts.get(i).getKey());
                myRef.removeValue();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        if(contacts!=null)
         return contacts.size();
        else
            return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView fullName;
        TextView phno;
        TextView email;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            fullName = (TextView) itemView.findViewById(R.id.fullName);
            phno = (TextView) itemView.findViewById(R.id.phoneNo);
            email = (TextView) itemView.findViewById(R.id.email);
            image = (ImageView) itemView.findViewById(R.id.contactPic);




        }


    }
}
