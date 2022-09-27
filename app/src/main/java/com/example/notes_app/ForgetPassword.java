package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private EditText mrecoveryemail;
    private RelativeLayout mrecover;
    private TextView mgoback;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().hide();
        mrecoveryemail=findViewById(R.id.recoveryemail);
        mrecover=findViewById(R.id.recover);
        mgoback=findViewById(R.id.goback);
        firebaseAuth=FirebaseAuth.getInstance();
        mgoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ForgetPassword.this,MainActivity.class);
                startActivity(intent);
            }
        });
        mrecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=mrecoveryemail.getText().toString().trim();
                if(mail.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Your Mail First",Toast.LENGTH_SHORT).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Password reset mail sent",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgetPassword.this,MainActivity.class));
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Please check your Email",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}