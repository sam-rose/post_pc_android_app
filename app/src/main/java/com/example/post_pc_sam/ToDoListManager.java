package com.example.post_pc_sam;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ToDoListManager {
    ArrayList<TodoItem> all_tasks;
    SharedPreferences sp;
    Gson gson;

    public ToDoListManager(Context context) {
        all_tasks = new ArrayList<>();
        sp = context.getSharedPreferences("todoListFile", MODE_PRIVATE);
        gson = new Gson();
        int TodoListSize = sp.getInt("TodoListSize", 0);
        for (int i = 0; i < TodoListSize; i++) {
            String todo_task = sp.getString("todo_task_" + i, null);
            if (todo_task != null) {
                TodoItem todo = gson.fromJson(todo_task, TodoItem.class);
                all_tasks.add(todo);
            }
        }
        Log.d("ToDoListManager", "current list size: " + TodoListSize);
    }

    public ArrayList<TodoItem> getAll_tasks() {
        return all_tasks;
    }

    public void addItem(TodoItem toAdd) {
        all_tasks.add(toAdd);
        int newSize = all_tasks.size();
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("TodoListSize", newSize);
        editor.putString("todo_task_" + (newSize - 1), gson.toJson(toAdd));
        editor.apply();
    }

    public void deleteItem(int index) {
        all_tasks.remove(index);
        int newSize = all_tasks.size();
        int TodoListSize = sp.getInt("TodoListSize", 0);
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < TodoListSize; i++) {
            editor.remove("todo_task_" + i);
            if (i != TodoListSize - 1) {
                editor.putString("todo_task_" + i, gson.toJson(all_tasks.get(i)));
            }
        }
        editor.putInt("TodoListSize", newSize);
        editor.apply();

    }

    public boolean isItemDone(int index) {
        return all_tasks.get(index).isDone();
    }

    public void setItemDone(int index) {
        all_tasks.get(index).setDone(true);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("todo_task_" + index, gson.toJson(all_tasks.get(index)));
        editor.apply();
    }

    public String getItemText(int index) {
        return all_tasks.get(index).getTask();

    }
}
