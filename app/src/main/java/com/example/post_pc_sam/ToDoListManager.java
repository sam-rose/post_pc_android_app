package com.example.post_pc_sam;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

interface ToDoListChangedCallBack {
    public void ToDoListChanged();
}

public class ToDoListManager {
    ArrayList<TodoItem> all_tasks;
    ToDoListChangedCallBack toUpdateOnListChanged;
    public ToDoListManager(ToDoListChangedCallBack toUpdateOnListChanged) {
        this.toUpdateOnListChanged = toUpdateOnListChanged;
        all_tasks = new ArrayList<>();
        loadTodoList("TodoList");
        addCollectionListener("TodoList");
    }

    private void loadTodoList(String collectionName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    all_tasks.clear();
                    int maxIdSeen = 0;
                    for (QueryDocumentSnapshot doc:  task.getResult()) {
                        TodoItem todoItem = doc.toObject(TodoItem.class);
                        if (maxIdSeen < todoItem.getId()) {
                            maxIdSeen = todoItem.getId();
                        }
                        all_tasks.add(todoItem);
                    }
                    TodoItem.currentId = maxIdSeen + 1;
                    toUpdateOnListChanged.ToDoListChanged();
                }
                else {
                    Log.d("ToDoListManager", "Error getting documents: " + task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ToDoListManager", "Error getting TodoList collection: " + e.getMessage());
            }
        });
    }

    private void addCollectionListener(String collectionName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionName).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null || queryDocumentSnapshots == null) {
                    Log.e("ToDoListManager", "Error getting TodoList collection from Listener");
                    return;
                }
                all_tasks.clear();
                int maxIdSeen = 0;
                for (QueryDocumentSnapshot doc:  queryDocumentSnapshots) {
                    TodoItem todoItem = doc.toObject(TodoItem.class);
                    if (maxIdSeen < todoItem.getId()) {
                        maxIdSeen = todoItem.getId();
                    }
                    all_tasks.add(todoItem);
                }
                TodoItem.currentId = maxIdSeen + 1;
                toUpdateOnListChanged.ToDoListChanged();
            }
        });
    }

    public ArrayList<TodoItem> getAll_tasks() {
        return all_tasks;
    }

    public void updateItem(final TodoItem toAdd, String collectionName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionName).document("" + toAdd.getId()).set(toAdd).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("ToDoListManager", "successfully added item to data base, id: " + toAdd.getId());
                toUpdateOnListChanged.ToDoListChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ToDoListManager", "Error adding item to data base, id: " + toAdd.getId() + ": " + e.getMessage());
            }
        });
    }

    public void addItem(TodoItem toAdd) {
        all_tasks.add(toAdd);
        updateItem(toAdd, "TodoList");
    }

    public void deleteItem(final int id) {
        int index = getItemIndexById(id);
        if (index == -1) {
            postIdLogError(id, "deleteItem");
            return;
        }
        all_tasks.remove(index);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("TodoList").document("" + id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("ToDoListManager", "successfully deleted item to data base, id: " + id);
                toUpdateOnListChanged.ToDoListChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ToDoListManager", "Error deleting item to data base, id: " + id + ": " + e.getMessage());
            }
        });
    }

    public boolean isItemDone(int id) {
        int index = getItemIndexById(id);
        if (index == -1) {
            postIdLogError(id, "isItemDone");
            return false;
        }
        return all_tasks.get(index).getIsDone();
    }

    public boolean setItemDone(int id, boolean isDone) {
        int index = getItemIndexById(id);
        if (index == -1) {
            postIdLogError(id, "setItemDone");
            return false;
        }
        all_tasks.get(index).updateIsDone(isDone);
        updateItem(all_tasks.get(index), "TodoList");
        return true;
    }

    public boolean setItemText(int id, String newTaskText) {
        int index = getItemIndexById(id);
        if (index == -1) {
            postIdLogError(id, "setItemText");
            return false;
        }
        all_tasks.get(index).updateTaskString(newTaskText);
        updateItem(all_tasks.get(index), "TodoList");
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
        return all_tasks.get(index).showStringFormatCreateTimeStamp();
    }

    public String getItemEditedTimeStamp(int id) {
        int index = getItemIndexById(id);
        if (index == -1) {
            postIdLogError(id, "getItemEditedTimeStamp");
            return "";
        }
        return all_tasks.get(index).showStringFormatEditTimeStamp();
    }

    private int getItemIndexById(int id) {
        for (int i = 0; i < all_tasks.size(); i++) {
            if (all_tasks.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
