package com.example.actionplannerapplication.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.actionplannerapplication.AddNewTask;
import com.example.actionplannerapplication.MainActivity;
import com.example.actionplannerapplication.Model.ActionModel;
import com.example.actionplannerapplication.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder> {
    private List<ActionModel> todoList;
    private MainActivity activity;
    private FirebaseFirestore firestore;

    /**
     * Constructor that takes to parameters
     * @param todoList
     * @param mainActivity
     */
    public ActionAdapter(List<ActionModel> todoList, MainActivity mainActivity){
        this.todoList = todoList;
        activity = mainActivity;
    }


    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task , parent, false);
        firestore = FirebaseFirestore.getInstance();

        return new ActionViewHolder(view);
    }

    public void deleteTask(int position){
        ActionModel actionModel = todoList.get(position);
        firestore.collection("task").document(actionModel.TaskId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public Context getContext(){
        return activity;
    }

    public void editTask(int position){
        ActionModel actionModel = todoList.get(position);
        // When we edit a task we have to set the existing data to the edit text by passing it
        // from the adapter class to the addNewTask class.
        Bundle bundle = new Bundle();
        bundle.putString("task", actionModel.getTask());
        bundle.putString("due", actionModel.getDue());
        bundle.putString("id", actionModel.TaskId);

        //Create an instance of addNewTask
        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        //
        addNewTask.show(activity.getSupportFragmentManager(), addNewTask.getTag());

    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        //Instance of the model class and use position to return the correct item
        ActionModel actionModel = todoList.get(position);
        holder.mCheckBox.setText(actionModel.getTask());
        holder.mDueDateTv.setText("Due On " + actionModel.getDue());
        // Takes a boolean value
        holder.mCheckBox.setChecked(toBoolean(actionModel.getStatus()));

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("task").document(actionModel.TaskId).update("status", 1);
                } else {
                   firestore.collection("task").document(actionModel.TaskId).update("status", 0);
                }
            }
        });
    }

    private boolean toBoolean(int status){
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    /**
     * Class that collects the values from the each_task.xml file. One checkbox and one textview
     */
    public class ActionViewHolder extends RecyclerView.ViewHolder{
        TextView mDueDateTv;
        CheckBox mCheckBox;
        public ActionViewHolder(@NonNull View itemView){
            super(itemView);

            mDueDateTv = itemView.findViewById(R.id.due_date_tv);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);
        }
    }
}
