package com.example.post_pc_sam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TodoChangedCallBack {
    final Boolean TASK_NOT_DONE = false;
    final Boolean TASK_DONE = true;
    TodoListAdapter TodoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToDoListManager listManager = ((TodoApp) getApplicationContext()).listManager;
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            String editedTask = savedInstanceState.getString("editedTask", "");
            ((EditText)findViewById(R.id.task_edit_text)).setText(editedTask);
        }
        ImageButton addButton = (ImageButton) findViewById(R.id.add_task_button);
        TodoAdapter = new TodoListAdapter(this);
        TodoAdapter.setTodos(listManager.getAll_tasks());
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
                listManager.addItem(new TodoItem(task_to_add, TASK_NOT_DONE));
                TodoAdapter.setTodos(listManager.getAll_tasks());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("editedTask", ((EditText)findViewById(R.id.task_edit_text)).getText().toString());
    }

    @Override
    public void NotifyTaskChangedCallBack(int id) {
        ToDoListManager listManager = ((TodoApp) getApplicationContext()).listManager;
        if (listManager.isItemDone(id)) {
            return;
        }
        else {
            Intent intent = new Intent(this, EditNotDoneTodoItemActivity.class);
            intent.putExtra("item_id", id);
            startActivity(intent);
        }
        // TODO; find place for this logic
//        listManager.setItemDone(index);
//        TodoAdapter.setTodos(listManager.getAll_tasks());
//        ToastMessage("TODO " + listManager.getItemText(index) + " is now DONE. BOOM!");
    }

    public void ToastMessage(CharSequence  message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    @Override
    public void NotifyTaskDeleteCallBack(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are You Sure to delete?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ToDoListManager listManager = ((TodoApp)getApplicationContext()).listManager;
                listManager.deleteItem(id);
                TodoAdapter.setTodos(listManager.getAll_tasks());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                return;
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToDoListManager listManager = ((TodoApp) getApplicationContext()).listManager;
        TodoAdapter.setTodos(listManager.getAll_tasks());
    }
}
