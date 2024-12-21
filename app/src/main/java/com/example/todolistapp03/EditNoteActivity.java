package com.example.todolistapp03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNoteActivity extends AppCompatActivity {

    EditText edt_date, edt_description,edt_urgency,edt_done,edt_move_to_date;
    Button btn_save,btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent intent = getIntent();

        edt_date = findViewById(R.id.edt_date);
        edt_description = findViewById(R.id.edt_description);
        edt_urgency = findViewById(R.id.edt_urgency);
        edt_done = findViewById(R.id.edt_done);
        edt_move_to_date = findViewById(R.id.edt_move);


        btn_cancel = findViewById(R.id.btn_cancel);
        btn_save = findViewById(R.id.btn_save);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note(
                        edt_date.getText().toString(),
                        edt_description.getText().toString(),
                        edt_urgency.getText().toString(),
                        edt_done.getText().toString(),
                        edt_move_to_date.getText().toString()
                );
                note.setId(intent.getIntExtra("id", 1));


                if(new NoteHandler(EditNoteActivity.this).update(note)){
                    Toast.makeText(EditNoteActivity.this,"Note updated",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                } else {
                    Toast.makeText(EditNoteActivity.this,"Failed to update note",Toast.LENGTH_SHORT).show();

                    setResult(RESULT_CANCELED);
                }

                finish();
            }
        });




        edt_date.setText(intent.getStringExtra("date"));
        edt_description.setText(intent.getStringExtra("description"));
        edt_urgency.setText(intent.getStringExtra("urgency"));
        edt_done.setText(intent.getStringExtra("done"));
        edt_move_to_date.setText(intent.getStringExtra("moveToDate"));



    }
    @Override
    public void onBackPressed() {
        // Hide buttons
        btn_save.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);
        super.onBackPressed();
    }
}


