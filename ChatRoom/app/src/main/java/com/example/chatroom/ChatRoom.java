package com.example.chatroom;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class ChatRoom extends Activity {

    User user;
    ArrayList<UserMessage> messages;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("userMessages");
    RecyclerView rv;
    UserMessageAdaptor adap;
    Uri imageuri;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_layout);
        setTitle("Chat Room");

        user = (User) getIntent().getExtras().get("User");
        TextView tv = (TextView)findViewById(R.id.userName);
        tv.setText(user.firstName+" "+user.lastName);
        messages = new ArrayList<>();

        rv = (RecyclerView)findViewById(R.id.messagesList);
        rv.setHasFixedSize(true); //for efficiency purpose
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        adap = new UserMessageAdaptor(messages);
        rv.setAdapter(adap);

        adap.notifyDataSetChanged();

        ImageButton addImg = (ImageButton)findViewById(R.id.addImageBtn);
        addImg.setImageResource(R.mipmap.add_image_foreground);
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });

        ImageButton logout = (ImageButton)findViewById(R.id.logoutBtn);
        logout.setImageResource(R.drawable.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 FirebaseAuth.getInstance().signOut();
                 finish();
            }
        });

        ImageButton send  = (ImageButton)findViewById(R.id.sendBtn);
        send.setImageResource(R.mipmap.send_foreground);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserMessage um = new UserMessage();
                um.setUser(user);
                final Message m = new Message();
                EditText enterMsg = (EditText)findViewById(R.id.enterMessage);
                if(enterMsg.getText().toString().equals("")) {
                         enterMsg.setError("Enter Message");
                         return;
                }
                m.setText(enterMsg.getText().toString());
                if(imageuri != null)
                {
                    final StorageReference ref = storageRef.child("images/"+ UUID.randomUUID().toString());
                    UploadTask up = ref.putFile(imageuri);
                    Task<Uri> urlTask = up.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                Uri downloadUri = task.getResult();
                                m.setImage(downloadUri.toString());
                                String key = myRef.push().getKey();
                                DatabaseReference exp = myRef.child(key);
                                um.setMessage(m);
                                um.setKey(key);
                                exp.setValue(um);
                            }
                        }
                    });
                }
                else{
                    m.setImage(null);
                    String key = myRef.push().getKey();
                    DatabaseReference exp = myRef.child(key);
                    um.setMessage(m);
                    um.setKey(key);
                    exp.setValue(um);
                }
                enterMsg.setText("");
            }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messages.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String key = d.getKey();
                    UserMessage c = d.getValue(UserMessage.class);
                    if(c!=null) {
                        messages.add(c);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageButton im = (ImageButton)findViewById(R.id.addImageBtn);
        if(resultCode != RESULT_CANCELED) {
            if (resultCode == RESULT_OK && data != null) {
                        imageuri =  data.getData();
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                        Picasso.get().load(imageuri.toString())
                                .into(im);
                        Log.d("Demo","Image Uri "+imageuri.toString());
            }

        }

    }


}
