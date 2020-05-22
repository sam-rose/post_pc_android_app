package com.example.post_pc_sam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditDoneTodoItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_done_todo_item);
        Intent intent = getIntent();
        final int todo_item_id = intent.getIntExtra("item_id", -1);
        if (todo_item_id == -1) {
            Log.e("NotDoneTodoItemActivity", "item id to edit not found");
            finish();
        }
        final ToDoListManager listManager = ((TodoApp) getApplicationContext()).listManager;

        TextView taskToEdit = (TextView)findViewById(R.id.done_task_text);
        taskToEdit.setText(listManager.getItemText(todo_item_id));

        Button UnDoneButton = (Button)findViewById(R.id.undone_button);
        UnDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listManager.setItemDone(todo_item_id, TodoItem.TASK_NOT_DONE);
                Intent intent = new Intent(EditDoneTodoItemActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button delteItemButton = (Button)findViewById(R.id.delete_done_item_button);
        delteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditDoneTodoItemActivity.this);
                builder.setTitle("Are You Sure to delete?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listManager.deleteItem(todo_item_id);
                        ToastMessage("The item was deleted successfully!");
                        Intent intent = new Intent(EditDoneTodoItemActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                builder.show();
            }
        });
    }

    public void ToastMessage(CharSequence  message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
