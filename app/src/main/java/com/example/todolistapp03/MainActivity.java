package com.example.todolistapp03;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageButton imageButton;
    ArrayList<Note> notes ,noteFilter;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;

    Button btnShowDone,btnShowNotDone,btnShowByDate,btnAllTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteFilter = new ArrayList<>();
        notes = new ArrayList<>();

        btnAllTask = findViewById(R.id.btn_show_all_tasks);
        btnAllTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNotes();
            }
        });


        btnShowNotDone = findViewById(R.id.btn_show_not_done);
        btnShowNotDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Filter notes to show only the ones that are not done
                noteFilter = filterNotDoneNotes(notes);
                // Update RecyclerView with filtered list
                updateRecyclerView(noteFilter);
            }
        });
        btnShowByDate = findViewById(R.id.btn_show_by_day);
        btnShowByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEnterDateDialog();
            }
        });

        btnShowDone = findViewById(R.id.btn_show_done);
        btnShowDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Filter notes to show only the ones that are done
                noteFilter = filterDoneNotes(notes);
                // Update RecyclerView with filtered list
                updateRecyclerView(noteFilter);
            }
        });

        imageButton = findViewById(R.id.img_add);
        imageButton.setOnClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View viewInput = inflater.inflate(R.layout.note_input,null,false);
            EditText edtDate = viewInput.findViewById(R.id.input_edt_date);
            EditText edtDescription = viewInput.findViewById(R.id.input_edt_description);
            EditText edtUrgency = viewInput.findViewById(R.id.input_edt_urgency);
            EditText edtDone = viewInput.findViewById(R.id.input_edt_done);
            EditText edtMoveToDate = viewInput.findViewById(R.id.input_edt_move);


            edtUrgency.setHint("Enter urgency(0-Regular 1-Urgent 2-Very Urgent)");
            edtDone.setHint("Enter task status(0-No 1-Yes 2-On Hold)");
            edtMoveToDate.setHint("If the task moved to another date, write it down  otherwise keep empty");



            new AlertDialog.Builder(MainActivity.this)
                    .setView(viewInput)
                    .setTitle("Add Note")
                    .setPositiveButton("Add", (dialog, which) -> {
                        try {
                            String date = edtDate.getText().toString();
                            String description = edtDescription.getText().toString();
                            String urgency = edtUrgency.getText().toString();
                            String done = edtDone.getText().toString();
                            String moveToDate = edtMoveToDate.getText().toString();

                            Note note = new Note(date, description, urgency, done, moveToDate);
                            boolean isInserted = new NoteHandler(MainActivity.this).create(note);

                            if (isInserted) {
                                Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                                loadNotes();
                            } else {
                                Toast.makeText(MainActivity.this, "Problem saving the note", Toast.LENGTH_SHORT).show();
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "An error occurred while adding the note", Toast.LENGTH_SHORT).show();
                        } finally {
                            dialog.dismiss();
                        }
                    }).show();



        });
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (noteFilter.isEmpty()) {
                    Note deletedNote = notes.get(position);
                    try (NoteHandler noteHandler = new NoteHandler(MainActivity.this)) {
                        noteHandler.delete(deletedNote.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error occurred while deleting note", Toast.LENGTH_SHORT).show();
                    }
                    notes.remove(position);



                }
                else{
                    Note deletedNote = noteFilter.get(position);
                    try (NoteHandler noteHandler = new NoteHandler(MainActivity.this)) {
                        noteHandler.delete(deletedNote.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error occurred while deleting note", Toast.LENGTH_SHORT).show();
                    }

                }
                if(!noteFilter.isEmpty()){
                    noteFilter.remove(position);
                }

                noteAdapter.notifyItemRemoved(position);
            }







        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        loadNotes();
    }
    public ArrayList<Note> readNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        try (NoteHandler handler = new NoteHandler(this)) {
            notes = handler.readNotes();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error occurred while reading notes", Toast.LENGTH_SHORT).show();
        }
        return notes;
    }


    public void loadNotes(){
        notes = readNotes();
        noteAdapter = new NoteAdapter(notes,
                this ,
                (position, view) -> editNode(notes.get(position).getId(),view));
        recyclerView.setAdapter(noteAdapter);
    }

    private void editNode(int noteId, View view) {
        try (NoteHandler noteHandler = new NoteHandler(this)) {
            Note note = noteHandler.readSingleNote(noteId);
            Intent intent = new Intent(this, EditNoteActivity.class);
            intent.putExtra("date", note.getDate());
            intent.putExtra("description", note.getDescription());
            intent.putExtra("urgency", note.getUrgency());
            intent.putExtra("done", note.getDone());
            intent.putExtra("moveToDate", note.getMoveToDate());
            intent.putExtra("id", note.getId());

            // Specify shared element transition
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "expanding_transitions");

            // Start activity with shared element transition
            startActivityForResult(intent,1, activityOptionsCompat.toBundle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadNotes();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private ArrayList<Note> filterDoneNotes(ArrayList<Note> notes) {
        ArrayList<Note> doneNotes = new ArrayList<>();
        for (Note note : notes) {
            if ("1".equals(note.getDone())) {
                doneNotes.add(note);
            }
        }
        return doneNotes;
    }

    private void updateRecyclerView(ArrayList<Note> filteredNotes) {
        noteAdapter = new NoteAdapter(filteredNotes, this,
                (position, view) -> editNode(filteredNotes.get(position).getId(), view));
        recyclerView.setAdapter(noteAdapter);
    }

    private ArrayList<Note> filterNotDoneNotes(ArrayList<Note> notes) {
        ArrayList<Note> notDoneNotes = new ArrayList<>();
        for (Note note : notes) {
            if ("0".equals(note.getDone())||"2".equals(note.getDone())) {
                notDoneNotes.add(note);
            }
        }
        return notDoneNotes;
    }

    private void showEnterDateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.enter_date, null);
        EditText edtEnterDate = view.findViewById(R.id.edt_enter_date);
        builder.setView(view);
        builder.setTitle("Enter Date");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredDate = edtEnterDate.getText().toString();
                noteFilter = filterNotesByDate(notes, enteredDate);
                updateRecyclerView(noteFilter);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private ArrayList<Note> filterNotesByDate(ArrayList<Note> notes, String enteredDate) {
        ArrayList<Note> filteredNotes = new ArrayList<>();
        // Loop through all notes
        for (Note note : notes) {
            if(note.getMoveToDate().equals("")){
                if (note.getDate().equals(enteredDate)) {
                    // Add the note to the filtered list if it matches
                    filteredNotes.add(note);
                }

            }
            else{
                if(note.getMoveToDate().equals(enteredDate)){
                    filteredNotes.add(note);
                }

            }

        }
        return filteredNotes;
    }




}




