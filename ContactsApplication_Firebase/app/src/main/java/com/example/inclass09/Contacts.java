package com.example.inclass09;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

public class Contacts extends Activity {

    User user;
    RecyclerView rv;
    ContactAdaptor adap;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("usercontacts");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    ArrayList<Contact> contacts;
    ImageButton logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);
        setTitle("Contacts");
        logout = (ImageButton)findViewById(R.id.logout);
        logout.setImageResource(R.drawable.logout);
        user = (User)getIntent().getExtras().get("User");
        contacts = new ArrayList<>();
        myRef = myRef.child(user.key+"/Contacts");
        rv = (RecyclerView)findViewById(R.id.contactList);
        rv.setHasFixedSize(true); //for efficiency purpose

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        adap = new ContactAdaptor(contacts,user);
        rv.setAdapter(adap);

        adap.notifyDataSetChanged();

        Button createContact = (Button)findViewById(R.id.createBtn);
        createContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Contacts.this,CreateNewContact.class);
                i.putExtra("User",user);
                startActivityForResult(i,1);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            contacts.clear();
                                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                                String key = d.getKey();
                                                Contact c = d.getValue(Contact.class);
                                                if(c!=null) {
                                                    contacts.add(c);
                                                }
                                            }
                                            adap.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data.hasExtra("NewUser")) {

            }
        }
    }

    @Override
    public void finish() {
        // Prepare data intent
        Intent data = new Intent();
        setResult(0);
        super.finish();
    }


}
