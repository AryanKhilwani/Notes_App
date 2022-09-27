package com.example.notes_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends AppCompatActivity {
    private EditText msignupemail,msignuppassword,mconfirmpassword;
    private RelativeLayout msignup;
    private TextView mgotologin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        msignupemail=findViewById(R.id.signupemail);
        msignuppassword=findViewById(R.id.signuppassword);
        msignup=findViewById(R.id.signup);
        mgotologin=findViewById(R.id.gotologin);
        mconfirmpassword=findViewById(R.id.confirmpassword);
        firebaseAuth=FirebaseAuth.getInstance();


        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Signup.this,MainActivity.class);
                startActivity(intent);
            }
        });

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=msignupemail.getText().toString().trim();
                String password=msignuppassword.getText().toString().trim();
                String pass=mconfirmpassword.getText().toString().trim();
                if(mail.isEmpty() || password.isEmpty() || pass.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please fill up all the fields",Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<7){
                    Toast.makeText(getApplicationContext(),"Password should be greater than 7 characters",Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(pass)){
                    Toast.makeText(getApplicationContext(),"Password doesn't match, Try Again",Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(Signup.this, "Registration Successful",Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            } else {
                                Toast.makeText(Signup.this, "Registration Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Signup.this, "Verification mail sent",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(Signup.this,MainActivity.class));
                }
            });
        }
        else{
            Toast.makeText(Signup.this, "Failed to send Verification mail",Toast.LENGTH_SHORT).show();
        }
    }
}