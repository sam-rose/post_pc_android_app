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
        TodoItem.currentId = sp.getInt("current_todo_class_id", 0);;

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
        editor.putInt("current_todo_class_id", TodoItem.currentId);
        editor.apply();
    }

    public void deleteItem(int id) {
        int index = getItemIndexById(id);
        if (index == -1) {
            postIdLogError(id, "deleteItem");
            return;
        }
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

    public boolean isItemDone(int id) {
        int index = getItemIndexById(id);
        if (index == -1) {
            postIdLogError(id, "isItemDone");
            return false;
        }
        return all_tasks.get(index).isDone();
    }

    public void setItemDone(int id) {
        int index = getItemIndexById(id);
        if (index == -1) {
            postIdLogError(id, "setItemDone");
            return;
        }
        all_tasks.get(index).setDone(true);
        saveUpdatedItem(index);
    }

    public boolean setItemText(int id, String newTaskText) {
        int index = getItemIndexById(id);
        if (index == -1) {
            postIdLogError(id, "setItemText");
            return false;
        }
        all_tasks.get(index).setTask(newTaskText);
        saveUpdatedItem(index);
        return true;
    }

    void postIdLogError(int id, String functionName) {
        Log.e("ToDoListManager", "item id not found " + id + "  in " + functionName);
    }

    public String getItemText(int id) {
        int index = getItemIndexById(id);
        if (index == -1) {
            postIdLogError(id, "getItemText");
            return "";
        }
        return all_tasks.get(index).getTask();

    }

    public int getItemId(int index) {
        return all_tasks.get(index).getId();
    }

    public String getItemCreationTimeStamp(int id) {
        int index = getItemIndexById(id);
        if (index == -1) {
            postIdLogError(id, "getItemCreationTimeStamp");
            return "";
        }
        return all_tasks.get(index).getCreationTimestamp();
    }

    public String getItemEditedTimeStamp(int id) {
        int index = getItemIndexById(id);
        if (index == -1) {
            postIdLogError(id, "getItemEditedTimeStamp");
            return "";
        }
        return all_tasks.get(index).getEditTimestamp();
    }

    private int getItemIndexById(int id) {
        for (int i = 0; i < all_tasks.size(); i++) {
            if (all_tasks.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    void saveUpdatedItem(int index) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("todo_task_" + index, gson.toJson(all_tasks.get(index)));
        editor.apply();
    }
}
