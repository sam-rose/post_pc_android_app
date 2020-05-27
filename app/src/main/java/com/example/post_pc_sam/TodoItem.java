package com.example.post_pc_sam;

import java.io.Serializable;
import java.sql.Timestamp;

public class TodoItem implements Serializable {
    static int currentId = 0;
    public int id;
    public String task = "";
    public boolean isDone = false;
    public long creationTimestamp;
    public long editTimestamp;
    static public final Boolean TASK_NOT_DONE = false;
    static public final Boolean TASK_DONE = true;

    public TodoItem(String task, boolean isDone) {
        this.task = task;
        this.isDone = isDone;
        this.id = TodoItem.currentId++;
        long milliTime = System.currentTimeMillis();
        creationTimestamp = milliTime;
        editTimestamp = milliTime;
    }
    public TodoItem(){}

    public String getTask() {
        return task;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public String showStringFormatCreateTimeStamp() {
        Timestamp timestamp = new Timestamp(creationTimestamp);
        return timestamp.toString();
    }

    public String showStringFormatEditTimeStamp() {
        Timestamp timestamp = new Timestamp(editTimestamp);
        return timestamp.toString();
    }

    public void updateTaskString(String task) {
        this.task = task;
        editTimestamp = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void updateIsDone(boolean done) {
        isDone = done;
        editTimestamp = System.currentTimeMillis();
    }
}
