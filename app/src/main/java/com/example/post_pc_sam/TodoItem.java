package com.example.post_pc_sam;

import java.sql.Timestamp;

public class TodoItem {
    static int currentId = 0;
    private int id;
    private String task = "";
    private boolean isDone = false;
    private Timestamp creationTimestamp;
    private Timestamp editTimestamp;

    public TodoItem(String task, boolean isDone) {
        this.task = task;
        this.isDone = isDone;
        this.id = TodoItem.currentId++;
        long milliTime = System.currentTimeMillis();
        creationTimestamp = new Timestamp(milliTime );
        editTimestamp = new Timestamp(milliTime);
    }

    public String getTask() {
        return task;
    }

    public boolean isDone() {
        return isDone;
    }

    public String getCreationTimestamp() {
        return creationTimestamp.toString();
    }

    public void setTask(String task) {
        this.task = task;
        setEditTimestamp(System.currentTimeMillis());
    }

    public String getEditTimestamp() {
        return editTimestamp.toString();
    }

    public int getId() {
        return id;
    }

    private void setEditTimestamp(long editedTime) {
        this.editTimestamp.setTime(editedTime);
    }

    public void setDone(boolean done) {
        isDone = done;
        setEditTimestamp(System.currentTimeMillis());
    }
}
