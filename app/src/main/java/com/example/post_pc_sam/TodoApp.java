package com.example.post_pc_sam;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TodoApp extends Application {
    public ToDoListManager listManager;

    @Override
    public void onCreate() {
        super.onCreate();
        listManager = new ToDoListManager(this);
    }
}
