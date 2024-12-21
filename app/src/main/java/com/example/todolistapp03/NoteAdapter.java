package com.example.todolistapp03;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    ArrayList<Note> notes;
    Context context;
    ItemClicked itemClicked;
    ViewGroup parent;
    public NoteAdapter( ArrayList<Note> notes, Context context,ItemClicked itemClicked){
        this.notes = notes;
        this.context = context;
        this.itemClicked = itemClicked;


    }


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_holder,parent,false);
        this.parent = parent;
        return new NoteHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.date.setText(notes.get(position).getDate());
        holder.description.setText(notes.get(position).getDescription());

        String urgencyText = getUrgencyText(notes.get(position).getUrgency());
        holder.urgency.setText(urgencyText);

        String doneText = getDoneText(notes.get(position).getDone());
        holder.done.setText(doneText);

        holder.moveToDade.setText(notes.get(position).getMoveToDate());

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView description;
        TextView urgency;
        TextView done;
        TextView moveToDade;
        ImageView imageEdit;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_date);
            description = itemView.findViewById(R.id.txt_description);
            urgency = itemView.findViewById(R.id.txt_urgency);
            done = itemView.findViewById(R.id.txt_Done);
            moveToDade = itemView.findViewById(R.id.txt_move);
            imageEdit = itemView.findViewById(R.id.img_edit);

            itemView.setOnClickListener(v -> {
                if(description.getMaxLines()==1){
                    description.setMaxLines(Integer.MAX_VALUE);
                }
                else{
                    description.setMaxLines(1);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(parent);
                }
            });
            imageEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Note clickedNote = notes.get(position);
                        Intent intent = new Intent(context, EditNoteActivity.class);
                        intent.putExtra("date", clickedNote.getDate());
                        intent.putExtra("description", clickedNote.getDescription());
                        intent.putExtra("urgency", clickedNote.getUrgency());
                        intent.putExtra("done", clickedNote.getDone());
                        intent.putExtra("moveToDate", clickedNote.getMoveToDate());
                        intent.putExtra("id", clickedNote.getId());
                        context.startActivity(intent);
                    }
                }
            });



        }
    }
    private String getUrgencyText(String urgency) {
        String urgencyText;
        switch (urgency) {
            case "0":
                urgencyText = "Urgency is regular";
                break;
            case "1":
                urgencyText = "Urgent";
                break;
            case "2":
                urgencyText = "Very Urgent";
                break;
            default:
                urgencyText = "Urgency status is unknown";
                break;
        }
        return urgencyText;
    }
    private String getDoneText(String done) {
        String doneText;
        switch (done) {
            case "0":
                doneText = "Task not done";
                break;
            case "1":
                doneText = "Task done";
                break;
            case "2":
                doneText = " Task On Hold";
                break;
            default:
                doneText = " Task status unknown";
                break;
        }
        return doneText;
    }
    interface ItemClicked{
        void onClick(int position,View view);
    }
}
