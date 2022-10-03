package com.example.notes_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Notes extends AppCompatActivity {
    FloatingActionButton mcreatenotesfab;
    FirebaseAuth firebaseAuth;
    RelativeLayout mlogout;
    RecyclerView mrecycle;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<firebasemodel,noteViewHolder> NoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        getSupportActionBar().hide();
        mcreatenotesfab=findViewById(R.id.createnotefab);
        firebaseAuth=FirebaseAuth.getInstance();
        mlogout=findViewById(R.id.log);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        mrecycle=findViewById(R.id.recyclerview);
        mrecycle.setHasFixedSize(true);



        mcreatenotesfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Notes.this,CreateNotes.class));
            }
        });




//        ArrayList<firebasemodel>notes=new ArrayList<>();
//        notes.add(new firebasemodel("welcome","home"));
//        notes.add(new firebasemodel("welcome","home"));
//        notes.add(new firebasemodel("welcome","home"));
//        notes.add(new firebasemodel("welcome","home"));
//        for(String t:value.alluser )
//        firebaseadapter adapter=new firebaseadapter(notes,this);
//        mrecycle.setAdapter(adapter);
//        StaggeredGridLayoutManager mlayout=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
//        mrecycle.setLayoutManager(mlayout);


        mlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(Notes.this,mlogout);
                popupMenu.getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        firebaseAuth.signOut();
                        Notes.super.finish();
                        startActivity(new Intent(Notes.this,MainActivity.class));
                        return true;
                    }
                });
                popupMenu.show();

            }
        });
        Query query=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("mynotes").orderBy("title", Query.Direction.ASCENDING);
        //        Log.d("mynotes", String.valueOf(query));
        FirestoreRecyclerOptions<firebasemodel> allusernotes=new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();
//        Log.d("user", String.valueOf(allusernotes));
        NoteAdapter=new FirestoreRecyclerAdapter<firebasemodel, noteViewHolder>(allusernotes) {
            @Override
            protected void onBindViewHolder(@NonNull noteViewHolder holder, int position, @NonNull firebasemodel model) {
                ImageView mpopup=holder.itemView.findViewById(R.id.menupopup);
                holder.notetitle.setText(model.getTitle());
                holder.notecontent.setText(model.getContent());
                String docId=NoteAdapter.getSnapshots().getSnapshot(position).getId();
//                Log.d("title",model.getTitle());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(view.getContext(),details.class);
                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("content",model.getContent());
                        intent.putExtra("noteid",docId);
                        view.getContext().startActivity(intent);
                    }
                });
                mpopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Intent intent=new Intent(v.getContext(),editnoteactivity.class);
                                intent.putExtra("title",model.getTitle());
                                intent.putExtra("content",model.getContent());
                                intent.putExtra("noteid",docId);
                                v.getContext().startActivity(intent);
                                return false;
                            }
                        });
                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener((new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("mynotes").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(v.getContext(),"This note is deleted",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v.getContext(),"Failed to delete note",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return false;
                            }
                        }));
                        popupMenu.show();
                    }

                });
            }

            @NonNull
            @Override
            public noteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
                return new noteViewHolder(view);
            }
        };
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecycle.setLayoutManager(staggeredGridLayoutManager);
        mrecycle.setAdapter(NoteAdapter);


    }

    public class noteViewHolder extends RecyclerView.ViewHolder {
        TextView notetitle;
        TextView notecontent;
        LinearLayout mnote;

        public noteViewHolder(@NonNull View itemView) {
            super(itemView);
            notetitle=itemView.findViewById(R.id.notetitle);
//            notetitle.setText("Aryan");
            notecontent=itemView.findViewById(R.id.notecontent);
            mnote=itemView.findViewById(R.id.note);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        NoteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(NoteAdapter!=null){
            NoteAdapter.startListening();
        }
    }
}