package com.example.notes_tab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerNotesAdapter extends RecyclerView.Adapter<RecyclerNotesAdapter.ViewHolder> {
    Context context;
    ArrayList<ContactNoteRow> notesArray;

    RecyclerNotesAdapter(Context context, ArrayList<ContactNoteRow> notesArray) {
        this.context = context;
        this.notesArray = notesArray;
    }

    @NonNull
    @Override
    public RecyclerNotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerNotesAdapter.ViewHolder holder, int position) {
        holder.noteTitle.setText(notesArray.get(position).title);
        holder.noteContent.setText(notesArray.get(position).content);

        holder.noteContent.getRootView().setOnClickListener(view -> {
            Intent intent = new Intent(context, NewNoteActivity.class);
                intent.putExtra("title",holder.noteTitle.getText().toString());
                intent.putExtra("content",holder.noteContent.getText().toString());
                intent.putExtra("pos",holder.getAdapterPosition());
            ((AppCompatActivity)context).startActivityForResult(intent, 1000000);
        });
        holder.noteContent.getRootView().setOnLongClickListener(view -> {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Delete this note?")
                            .setPositiveButton("Yes", (a, b) -> {
                                notesArray.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());

                                MainActivity.updateData();
                            })
                            .setNegativeButton("No", null)
                            .show();
                    return false;
                }
        );

    }

    @Override
    public int getItemCount() {
        return notesArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.cardNoteTitle);
            noteContent = itemView.findViewById(R.id.cardNoteContent);
        }
    }

}
