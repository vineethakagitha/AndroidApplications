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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.support.constraint.Constraints.TAG;

public class SignUp extends Activity {


    private FirebaseAuth mAuth;
    EditText fname;
    EditText lname;
    EditText email;
    EditText pass;
    EditText cPass;
    User u;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("usercontacts");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        setTitle("Sign Up");

        mAuth = FirebaseAuth.getInstance();

        fname = (EditText)findViewById(R.id.firstNameEdit);
        lname = (EditText)findViewById(R.id.lastNameEdit);
        email = (EditText)findViewById(R.id.emailEdit);
        pass = (EditText)findViewById(R.id.passwordEdit);
        cPass = (EditText)findViewById(R.id.confirmPasswordEdit);

        Button cancel = (Button)findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button sign = (Button)findViewById(R.id.signUpButton);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u = new User();
                if (!pass.getText().toString().equals(cPass.getText().toString())) {
                    cPass.setError("Passwords do not match");
                    return;
                }
                if (fname.getText().toString().equals("")) {
                    fname.setError("Enter first name");
                    return;
                }
                u.setFirstName(fname.getText().toString());
                if (lname.getText().toString().equals("")) {
                    lname.setError("Enter last name");
                    return;
                }
                u.setLastName(lname.getText().toString());
                if (email.getText().toString().equals("")) {
                    email.setError("Enter email");
                    return;
                }
                u.setEmail(email.getText().toString());
                if (pass.getText().toString().equals("")) {
                    pass.setError("Enter password");
                    return;
                }
                u.setPassword(pass.getText().toString());
                createUser(u);
            }
        });

    }

    void createUser(User user)
    {
        mAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            u.setKey(user.getUid());
                            myRef = myRef.child(u.key);
                            myRef.setValue(u);
                            Intent i = new Intent(SignUp.this,ChatRoom.class);
                            i.putExtra("User",u);
                            SignUp.this.startActivityForResult(i,0);

                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }
    }



}
