package com.example.notes_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class firebaseadapter extends RecyclerView.Adapter<firebaseadapter.viewHolder>{
    public firebaseadapter(ArrayList<firebasemodel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    ArrayList<firebasemodel>list;
    Context context;

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notes_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        firebasemodel firebasemodel=list.get(position);
        holder.mtext.setText(firebasemodel.getTitle());
        holder.mcontent.setText(firebasemodel.getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView mtext;
        TextView mcontent;

        public viewHolder(View itemView){
            super (itemView);
            mtext=itemView.findViewById(R.id.notetitle);
            mcontent=itemView.findViewById(R.id.notecontent);
        }
    }


}
