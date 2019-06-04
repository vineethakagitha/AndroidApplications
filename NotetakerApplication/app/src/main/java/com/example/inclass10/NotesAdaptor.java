package com.example.inclass10;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotesAdaptor extends RecyclerView.Adapter<NotesAdaptor.ViewHolder>{

    View view;
    ArrayList<Note> notes;
    DeleteNoteI dn;


    public NotesAdaptor(ArrayList<Note> notes,DeleteNoteI dn) {
        this.notes = notes;
        this.dn = dn;
    }

    @NonNull
    @Override
    public NotesAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note, viewGroup, false);
        ViewHolder viewHolder =  new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdaptor.ViewHolder viewHolder, int i) {
        Note n = notes.get(i);
        viewHolder.noteText.setText(n.getText());
        viewHolder.del.setImageResource(R.drawable.delete);
        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dn.delNote(n.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView noteText;
        ImageButton del;

        public ViewHolder(View itemView) {
            super(itemView);
            noteText = (TextView) itemView.findViewById(R.id.noteTextView);
            del = (ImageButton)itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface DeleteNoteI{
        public void delNote(String id);
    }

}
