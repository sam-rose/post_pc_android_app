package com.example.post_pc_sam;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class TodoApp extends Application implements ToDoListChangedCallBack{
    public ToDoListManager listManager;
    HashMap<String, ToDoListChangedCallBack> entitiesToUpdate;

    @Override
    public void onCreate() {
        super.onCreate();
        listManager = new ToDoListManager(this);
        entitiesToUpdate = new HashMap<>();
    }

    @Override
    public void ToDoListChanged() {
        for (ToDoListChangedCallBack toUpdate: entitiesToUpdate.values()) {
            toUpdate.ToDoListChanged();
        }
    }

    public void registerToDoListChangedCallBack(ToDoListChangedCallBack toRegister, String name) {
        entitiesToUpdate.put(name, toRegister);
    }

    public void unRegisterToDoListChangedCallBack(String name) {
        entitiesToUpdate.remove(name);
    }
}
