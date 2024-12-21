package com.example.todolistapp03;


public class Note {
    private String date;
    private String description;
    private String urgency;
    private String Done;
    private String moveToDate;
    private int id;

    public Note(String date, String description, String urgency, String done, String moveToDate) {
        this.date = date;
        this.description = description;
        this.urgency = urgency;
        Done = done;
        this.moveToDate = moveToDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getDone() {
        return Done;
    }

    public void setDone(String done) {
        Done = done;
    }

    public String getMoveToDate() {
        return moveToDate;
    }

    public void setMoveToDate(String moveToDate) {
        this.moveToDate = moveToDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
