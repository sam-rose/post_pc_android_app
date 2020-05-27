package com.example.post_pc_sam;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TodoItemClickedCallBack, ToDoListChangedCallBack {
    TodoListAdapter TodoAdapter;
    final String CallBackRegisterName = "mainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TodoApp app = (TodoApp) getApplicationContext();
        app.registerToDoListChangedCallBack(this, CallBackRegisterName);
        ToDoListManager listManager = ((TodoApp) getApplicationContext()).listManager;
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            String editedTask = savedInstanceState.getString("editedTask", "");
            ((EditText)findViewById(R.id.task_edit_text)).setText(editedTask);
        }
        ImageButton addButton = (ImageButton) findViewById(R.id.add_task_button);
        TodoAdapter = new TodoListAdapter(this);
        RecyclerView TodoRecycler = (RecyclerView) findViewById(R.id.recyclerView);
        TodoRecycler.setAdapter(TodoAdapter);
        TodoRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText task = (EditText) findViewById(R.id.task_edit_text);
                String task_to_add = task.getText().toString();
                task.setText("");
                if (task_to_add.isEmpty()){
                    CharSequence message = "you can't create an empty TODO item, oh silly!";
                    ToastMessage(message);
                    return;
                }
                ToDoListManager listManager = ((TodoApp) getApplicationContext()).listManager;
                listManager.addItem(new TodoItem(task_to_add, TodoItem.TASK_NOT_DONE));
                //(listManager.getAll_tasks());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("editedTask", ((EditText)findViewById(R.id.task_edit_text)).getText().toString());
    }

    @Override
    public void NotifyTodoItemClickedCallBack(int id) {
        ToDoListManager listManager = ((TodoApp) getApplicationContext()).listManager;
        if (listManager.isItemDone(id)) {
            Intent intent = new Intent(this, EditDoneTodoItemActivity.class);
            intent.putExtra("item_id", id);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, EditNotDoneTodoItemActivity.class);
            intent.putExtra("item_id", id);
            startActivity(intent);
        }
    }

    public void ToastMessage(CharSequence  message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToDoListChanged();
    }

    @Override
    public void ToDoListChanged() {
        ToDoListManager listManager = ((TodoApp) getApplicationContext()).listManager;
        TodoAdapter.setTodos(listManager.getAll_tasks());
    }

    @Override
    protected void onPause() {
        super.onPause();
        TodoApp app = (TodoApp) getApplicationContext();
        app.unRegisterToDoListChangedCallBack(CallBackRegisterName);
    }
}
