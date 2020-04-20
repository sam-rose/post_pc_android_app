package com.example.post_pc_sam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int task_id = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton addButton = (ImageButton) findViewById(R.id.add_task_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText task = (EditText) findViewById(R.id.task_edit_text);
                String task_to_add = task.getText().toString();
                task.setText("");
                TextView all_tasks = (TextView) findViewById(R.id.all_tasks_text_view);
                String existing_tasks = all_tasks.getText().toString();
                if (existing_tasks.isEmpty()){
                    all_tasks.setText(task_id + ". " + task_to_add);
                }
                else {
                    all_tasks.setText(existing_tasks + "\n" + task_id + ". " + task_to_add);
                }
                task_id++;

            }
        });
    }



}
