package com.example.todolistapp03;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class NoteHandler extends DatabaseHelper {

    public NoteHandler(Context context) {
        super(context);
    }

    public boolean create(Note note) {

        ContentValues values = new ContentValues();
        values.put("date", note.getDate());
        values.put("description", note.getDescription());
        values.put("urgency", note.getUrgency());
        values.put("done", note.getDone());
        values.put("moveToDate", note.getMoveToDate());

        SQLiteDatabase db = this.getWritableDatabase();
        boolean flag = db.insert("Note", null, values) > 0;
        db.close();
        return flag;

    }

    public ArrayList<Note> readNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        String[] columns = {"id", "date", "description", "urgency", "done", "moveToDate"};
        String sqlQuery = "SELECT * FROM Note ORDER BY id ASC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int dateIndex = cursor.getColumnIndex("date");
                int descriptionIndex = cursor.getColumnIndex("description");
                int urgencyIndex = cursor.getColumnIndex("urgency");
                int doneIndex = cursor.getColumnIndex("done");
                int moveToDateIndex = cursor.getColumnIndex("moveToDate");

                if (idIndex != -1 && dateIndex != -1 && descriptionIndex != -1 && urgencyIndex != -1 && doneIndex != -1 && moveToDateIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String date = cursor.getString(dateIndex);
                    String description = cursor.getString(descriptionIndex);
                    String urgency = cursor.getString(urgencyIndex);
                    String done = cursor.getString(doneIndex);
                    String moveToDate = cursor.getString(moveToDateIndex);

                    Note note = new Note(date, description, urgency, done, moveToDate);
                    note.setId(id);
                    notes.add(note);
                } else {
                    Log.e("readNotes", "One or more columns are missing in the cursor");                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return notes;
    }


    public Note readSingleNote(int id){
        Note note = null;
        String sqlQuery = "SELECT * FROM Note WHERE id="+id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery,null);

        if(cursor.moveToFirst()){
            @SuppressLint("Range")
            int noteId = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
            @SuppressLint("Range")
            String date = cursor.getString(cursor.getColumnIndex("date"));
            @SuppressLint("Range")
            String description = cursor.getString(cursor.getColumnIndex("description"));
            @SuppressLint("Range")
            String urgency = cursor.getString(cursor.getColumnIndex("urgency"));
            @SuppressLint("Range")
            String done = cursor.getString(cursor.getColumnIndex("done"));
            @SuppressLint("Range")
            String moveToDate = cursor.getString(cursor.getColumnIndex("moveToDate "));

            note = new Note(date, description, urgency, done, moveToDate);
            note.setId(noteId);

        }
        cursor.close();
        db.close();
        return note;
    }

    public boolean update(Note note){
        ContentValues values = new ContentValues();
        values.put("date", note.getDate());
        values.put("description", note.getDescription());
        values.put("urgency", note.getUrgency());
        values.put("done", note.getDone());
        values.put("moveToDate", note.getMoveToDate());
        SQLiteDatabase db = this.getWritableDatabase();
        boolean flag = db.update("Note",values,"id='"+note.getId()+"'",null)>0;
        db.close();
        return flag;

    }

    public  boolean delete(int id){
        boolean flag;
        SQLiteDatabase db = this.getWritableDatabase();
        flag = db.delete("Note","id='"+id+"'",null)>0;
        return flag;
    }
}

