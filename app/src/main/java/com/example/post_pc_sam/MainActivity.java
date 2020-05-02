package com.example.post_pc_sam;

import android.content.Context;
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
    ArrayList<TodoItem> all_tasks = new ArrayList<>();
    final Boolean TASK_NOT_DONE = false;
    final Boolean TASK_DONE = true;
    TodoListAdapter TodoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            all_tasks.clear();
            int numOfTods = savedInstanceState.getInt("num_of_todos");
            for (int i = 0; i < numOfTods; i++) {
                String todo_task = savedInstanceState.getString("todo_task_" + i);
                Boolean isDone = savedInstanceState.getBoolean("todo_isDone_" + i);
                all_tasks.add(new TodoItem(todo_task, isDone));
            }
        }

        setContentView(R.layout.activity_main);
        ImageButton addButton = (ImageButton) findViewById(R.id.add_task_button);
        TodoAdapter = new TodoListAdapter(this);
        TodoAdapter.setTodos(all_tasks);
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
                    Context context = getApplicationContext();
                    CharSequence text = "you can't create an empty TODO item, oh silly!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return;
                }
                all_tasks.add(new TodoItem(task_to_add, TASK_NOT_DONE));
                TodoAdapter.setTodos(all_tasks);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("num_of_todos", all_tasks.size());
        for (int i = 0; i < all_tasks.size(); i++) {
            outState.putString("todo_task_" + i, all_tasks.get(i).getTask());
            outState.putBoolean("todo_isDone_" + i, all_tasks.get(i).isDone());
        }
    }

    @Override
    public void NotifyTaskChangedCallBack(int pos) {
        TodoItem todo = all_tasks.get(pos);
        if (todo.isDone()) {
            return;
        }
        todo.setDone(TASK_DONE);
        TodoAdapter.setTodos(all_tasks);
        Context context = getApplicationContext();
        CharSequence text = "TODO " + todo.getTask() + " is now DONE. BOOM!";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
