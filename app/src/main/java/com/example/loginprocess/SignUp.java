package com.example.loginprocess;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    EditText emailId, password, ageId, nameId;
    Button signUp;
    TextView switchToSignIn;


    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();
        nameId = findViewById(R.id.suName);
        ageId = findViewById(R.id.suAge);
        emailId = findViewById(R.id.suEmail);
        password = findViewById(R.id.suPassword);


        signUp = findViewById(R.id.suButton);
        switchToSignIn = findViewById(R.id.siPageButton);



        switchToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, LoginIn.class);
                startActivity(intent);

            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameId.getText().toString().trim();
                String age = ageId.getText().toString().trim();
                String email = emailId.getText().toString().trim();
                String pwd = password.getText().toString().trim();



                if (email.isEmpty()) {
                    emailId.setError("Please enter email");
                    emailId.requestFocus();


                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailId.setError("Please Provide Valid Email");
                    emailId.requestFocus();


                } else if (pwd.isEmpty()) {
                    password.setError("Please enter password");
                    password.requestFocus();
                } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {


                    mAuth.createUserWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {


                                        if (task.isSuccessful()) {
                                            User user = new User(email, pwd, age, name);

                                            FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {


                                                    if (task.isSuccessful()) {

                                                        Toast.makeText(SignUp.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(SignUp.this, Home.class);
                                                        startActivity(intent);

                                                        //redirect to login page
                                                    } else {
                                                        Toast.makeText(SignUp.this, "Failed to register! Try again!!", Toast.LENGTH_LONG).show();


                                                    }

                                                }
                                            });






                                        } else {

                                            Toast.makeText(SignUp.this, "Failed!!", Toast.LENGTH_SHORT).show();


                                        }


                                    }
                                }


                            });

                }
            }
        });
    }
}

