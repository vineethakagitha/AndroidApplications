package com.example.chatroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Login extends Activity {

    EditText email;
    EditText pass;
    User user;
    HashMap<String,User> users;
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("usercontacts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle("Login");
        users = new HashMap<>();
        user = new User();
        mAuth = FirebaseAuth.getInstance();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String key = d.getKey();
                    User c = d.getValue(User.class);
                    if(c!=null) {
                        users.put(key,c);
                    }
                }
                if(mAuth.getCurrentUser() != null)
                {
                    user = users.get(mAuth.getCurrentUser().getUid());
                    Intent i = new Intent(Login.this,ChatRoom.class);
                    i.putExtra("User",user);
                    startActivityForResult(i,0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        email = (EditText)findViewById(R.id.lEmailEdit);
        pass = (EditText)findViewById(R.id.lPasswordEdit);
        Button login = (Button)findViewById(R.id.lloginButton);

        Button signup = (Button)findViewById(R.id.lSignUpButton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,SignUp.class);
                startActivity(i);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setEmail(email.getText().toString());
                user.setPassword(pass.getText().toString());
                signIn(user);
            }
        });



    }

    void signIn(User u)
    {
        mAuth.signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("demo", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Login.this.user.key = user.getUid();
                            Login.this.user = users.get(user.getUid());
                            Intent i = new Intent(Login.this,ChatRoom.class);
                            i.putExtra("User",Login.this.user);
                            startActivityForResult(i,0);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                            email.setError("Invalid email");
                            pass.setError("Invalid password");
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            if (resultCode == 0) {
                FirebaseAuth.getInstance().signOut();
                email.setText("");
                pass.setText("");
            }
        }
    }

}
