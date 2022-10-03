package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class editnoteactivity extends AppCompatActivity {
    Intent data;
    EditText meditnote,meditcontent;
    FloatingActionButton esavenote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnoteactivity);
        meditcontent=findViewById(R.id.editcontent);
        meditnote=findViewById(R.id.editnote);
        esavenote=findViewById(R.id.esavenote);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Toolbar toolbar=findViewById(R.id.etoolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        data=getIntent();
        meditnote.setText(data.getStringExtra("title"));
        meditcontent.setText(data.getStringExtra("content"));
        esavenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ntitle=meditnote.getText().toString();
                String ncontent=meditcontent.getText().toString();
                if(ntitle.isEmpty()||ncontent.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Both Fields are necessary",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("mynotes").document(data.getStringExtra("noteid"));
                    Map<String,Object> note=new HashMap<>();
                    note.put("title",ntitle);
                    note.put("content",ncontent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Note updated",Toast.LENGTH_SHORT).show();
                            editnoteactivity.super.finish();
                            startActivity(new Intent(editnoteactivity.this,Notes.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to update",Toast.LENGTH_SHORT).show();
                        }
                    });



                }
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}