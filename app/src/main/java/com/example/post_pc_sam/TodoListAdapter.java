package com.example.post_pc_sam;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

interface TodoItemClickedCallBack {
    void NotifyTodoItemClickedCallBack(int index);
}

class TodoItemViewHolder extends RecyclerView.ViewHolder{

    TextView Todo;
    public TodoItemViewHolder(@NonNull View itemView) {
        super(itemView);
        Todo = itemView.findViewById(R.id.todo_textView);
    }
}

public class TodoListAdapter extends RecyclerView.Adapter<TodoItemViewHolder> {
    private ArrayList<TodoItem> all_tasks = new ArrayList<>();
    TodoItemClickedCallBack toCallOnTodoChanged = null;


    public TodoListAdapter(TodoItemClickedCallBack toCallOnTodoChanged) {
        this.toCallOnTodoChanged = toCallOnTodoChanged;
    }

    @NonNull
    @Override
    public TodoItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.todo_item, viewGroup, false);

        final TodoItemViewHolder holder = new TodoItemViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toCallOnTodoChanged != null) {
                    toCallOnTodoChanged.NotifyTodoItemClickedCallBack(all_tasks.get(holder.getAdapterPosition()).getId());
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoItemViewHolder todoItemViewHolder, int i) {
        TodoItem todo = all_tasks.get(i);
        todoItemViewHolder.Todo.setText(todo.getTask());
        todoItemViewHolder.Todo.setAlpha(todo.getIsDone()? 0.3f : 1);
    }

    @Override
    public int getItemCount() {
        return all_tasks.size();
    }

    void setTodos(ArrayList<TodoItem> Todos){
        all_tasks.clear();
        all_tasks.addAll(Todos);
        notifyDataSetChanged();
    }
}
