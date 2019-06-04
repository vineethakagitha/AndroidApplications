package com.example.inclass09;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends Activity {
    EditText email;
    EditText pass;
    User user;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle("Login");

        mAuth = FirebaseAuth.getInstance();
        user = new User();
        email = (EditText)findViewById(R.id.lEmailEdit);
        pass = (EditText)findViewById(R.id.lPasswordEdit);
        Button login = (Button)findViewById(R.id.lloginButton);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setEmail(email.getText().toString());
                user.setPassword(pass.getText().toString());
                signIn(user);
            }
        });

        Button signup = (Button)findViewById(R.id.lSignUpButton);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,SignUp.class);
                startActivity(i);
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
                            Login.this.user.key = user.getUid();
                            Intent i = new Intent(Login.this,Contacts.class);
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
