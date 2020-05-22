package com.example.post_pc_sam;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditNotDoneTodoItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_not_done_todo_item);
        Intent intent = getIntent();
        final int todo_item_id = intent.getIntExtra("item_id", -1);
        if (todo_item_id == -1) {
            Log.e("NotDoneTodoItemActivity", "item id to edit not found");
            finish();
        }
      final ToDoListManager listManager = ((TodoApp) getApplicationContext()).listManager;

        EditText taskToEdit = (EditText)findViewById(R.id.change_task_text);
        taskToEdit.setText(listManager.getItemText(todo_item_id));

        TextView creationTimeStamp = (TextView)findViewById(R.id.create_time_stamp);
        TextView lastEditedTimeStamp = (TextView)findViewById(R.id.edit_time_stamp);

        creationTimeStamp.setText("created: " + listManager.getItemCreationTimeStamp(todo_item_id));
        lastEditedTimeStamp.setText("last edited: " + listManager.getItemEditedTimeStamp(todo_item_id));

        Button DoneButton = (Button)findViewById(R.id.edit_done_button);
        DoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listManager.setItemDone(todo_item_id);
                Intent intent = new Intent(EditNotDoneTodoItemActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button ApplyTextButton = (Button)findViewById(R.id.apply_change_text_button);
        ApplyTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText taskToEdit = (EditText)findViewById(R.id.change_task_text);
                listManager.setItemText(todo_item_id, taskToEdit.getText().toString());
                ToastMessage("The item was updated successfully!");
            }
        });



    }

    public void ToastMessage(CharSequence  message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
