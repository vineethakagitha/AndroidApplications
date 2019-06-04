package com.example.inclass09;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class CreateNewContact extends Activity {

    Contact c;
    EditText name;
    EditText email;
    EditText phone;
    ImageView pic;
    Uri imageuri;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("usercontacts");
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_contact);
        setTitle("Create New Contact");

        user = (User)getIntent().getExtras().get("User");
        myRef = myRef.child(user.key+"/Contacts");

        name = findViewById(R.id.nameEdit);
        email = findViewById(R.id.emailEdit);
        phone = findViewById(R.id.phoneEdit);
        pic = findViewById(R.id.picture);

        pic.setImageResource(R.drawable.ic_photo_camera_black_24dp);
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(CreateNewContact.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                ActivityCompat.requestPermissions(CreateNewContact.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
            }
        });



        Button sub = (Button)findViewById(R.id.submitButton);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = new Contact();
                if (name.getText().toString().equals("")) {
                    name.setError("Enter name");
                    return;
                }
                c.setName(name.getText().toString());
                if (email.getText().toString().equals("")) {
                    email.setError("Enter email");
                    return;
                }
                c.setEmail(email.getText().toString());
                if (phone.getText().toString().equals("")) {
                    phone.setError("Enter phone no");
                    return;
                }
                c.setPhone(phone.getText().toString());
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
                                c.picture = downloadUri.toString();
                                String key = myRef.push().getKey();
                                DatabaseReference exp = myRef.child(key);
                                c.setKey(key);
                                exp.setValue(c);
                                finish();
                            }
                        }
                    });
                }
                else{
                    String key = myRef.push().getKey();
                    DatabaseReference exp = myRef.child(key);
                    c.setKey(key);
                    exp.setValue(c);
                    finish();
                }

            }
        });

    }

    @Override
    public void finish() {
        // Prepare data intent
        Intent data = new Intent();
        data.putExtra("NewUser", user);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data);
        super.finish();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView im = (ImageView)findViewById(R.id.picture);
        if(resultCode != RESULT_CANCELED) {
            if (resultCode == RESULT_OK && data != null) {
                Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                im.setImageBitmap(selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = "";
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                path = MediaStore.Images.Media.insertImage(this.getContentResolver(), selectedImage, "Title", null);
                imageuri =  Uri.parse(path);
                Log.d("Demo","Image Uri "+imageuri.toString());
            }
        }
    }
}
