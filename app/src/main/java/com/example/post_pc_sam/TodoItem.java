package com.example.post_pc_sam;

public class TodoItem {
    private String task = "";
    private boolean isDone = false;

    public TodoItem(String task, boolean isDone) {
        this.task = task;
        this.isDone = isDone;
    }

    public String getTask() {
        return task;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
