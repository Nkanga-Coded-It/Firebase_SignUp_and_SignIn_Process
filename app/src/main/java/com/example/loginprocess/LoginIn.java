package com.example.loginprocess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

public class LoginIn extends AppCompatActivity {

    EditText emailId,password;
    Button signIn;
    TextView switchToSignUp;
    FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);

        mAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.siEmail);
        password = findViewById(R.id.siPassword);
        signIn = findViewById(R.id.siButton);
        switchToSignUp = findViewById(R.id.suPageButton);


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Toast.makeText(LoginIn.this, "You are logged In", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginIn.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginIn.this, "Please Log In", Toast.LENGTH_SHORT).show();
                }
            }
        };
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString().trim();
                String pwd = password.getText().toString().trim();


                if (email.isEmpty()) {
                    emailId.setError("Please enter email");
                    emailId.requestFocus();


                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailId.setError("Input Valid Email");
                    emailId.requestFocus();
                    return;

                } else if (pwd.isEmpty()) {
                    password.setError("Please enter password");
                    password.requestFocus();


                } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginIn.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginIn.this, "Log In failed", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(LoginIn.this, Home.class));

                            }
                        }
                    });

                } else {
                    Toast.makeText(LoginIn.this, "Error Occurred", Toast.LENGTH_SHORT).show();

                }
            }
        });


        switchToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginIn.this, SignUp.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
}